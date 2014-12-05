package edu.jhu.cvrg.filestore.filetree;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.liferay.faces.portal.context.LiferayFacesContext;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil;

import edu.jhu.cvrg.waveform.utility.ResourceUtility;

public class Liferay61FileTree extends FileTree {

	private long waveformRootFolderId = 0L;
	private long userRootFolderId = 0L;
	private long groupId = 0L;
	private long userId = 0L;
	private FileNode treeRoot;

	public Liferay61FileTree(String[] args) {

		this.groupId = Long.valueOf(args[0]);
		this.userId = Long.valueOf(args[1]);

		if (args.length > 2) {
			waveformRootFolderId = findFolderIDByName(args[2]);
		}
		if (args.length > 3) {
			userRootFolderId = findFolderIDByName(args[3]);
			if (userRootFolderId == 0L) {
				userRootFolderId = createUserFolder(userId).getFolderId();
			}
		}
		buildTree();
	}

	@Override
	protected void buildTree() {

		Folder rootFolder = getUserRootFolder();

		if (rootFolder != null) {
			treeRoot = new FileNode(null, rootFolder.getName(), rootFolder.getFolderId(), 0L);
			addChildren(rootFolder, treeRoot);

		} else {
			getLog().error(waveformRootFolderId + " folder does not exist");
		}
	}
	
	@Override
	public void addFolder(UUID parentNodeUuid, String newFolderName) {
		ServiceContext serviceContext = LiferayFacesContext.getInstance().getServiceContext();
		FileNode parentNode = findNodeByUuid(parentNodeUuid);
		new FileNode(parentNode, newFolderName, 0L, 0L);
		
		long parentFolderId = parentNode.getDocumentRecordId();
		try {
			DLAppLocalServiceUtil.addFolder(userId, groupId, parentFolderId, newFolderName, "", serviceContext);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void deleteNode(UUID uuid){

		FileNode node = findNodeByUuid(uuid);
		if(node == null){
			return;
		}
		if(node.isFolder()){
			long folderId = findFolderIDByName(node.getName());
			try {
				DLAppLocalServiceUtil.deleteFolder(folderId);
			} catch (PortalException e) {
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		else{
			FileNode parentNode = node.getParent();
			long parentFolderId = findFolderIDByName(parentNode.getName());
			long fileEntryId = findFileIDByName(node.getName(), parentFolderId);
			try {
				DLAppLocalServiceUtil.deleteFileEntry(fileEntryId);
			} catch (PortalException e) {
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
	}
	
	private long findFileIDByName(String fileName, long parentFolderId){
		try {
			List<FileEntry> files = DLAppLocalServiceUtil.getFileEntries(0, parentFolderId);
			for (FileEntry fileEntry : files) {
				if (fileEntry.getTitle().equals(fileName)) {
					return fileEntry.getFileEntryId();
				}
			}
		} catch (SystemException e) {
			getLog().error("Unable to retrieve folder " + fileName);
			e.printStackTrace();
		} catch (PortalException e) {
			e.printStackTrace();
		}

		return 0L;
	}
	
	private FileNode findNodeByUuid(UUID nodeId){
		
		if(treeRoot.getUuid().toString().equals(nodeId.toString())){
			return treeRoot;
		}
		
		return checkChildNodes(treeRoot, nodeId);
	}
	
	private FileNode checkChildNodes(FileNode parentNode, UUID nodeId){
		
		FileNode searchNode = null;
		
		if(parentNode.getChildren() == null){
			return null;
		}
		
		for(FileNode node : parentNode.getChildren()){
			if(node.getUuid().equals(nodeId)){
				return node;
			}
			else{
				searchNode = checkChildNodes(node, nodeId);
				if(searchNode != null){
					return searchNode;
				}
			}
		}
		return searchNode;
	}

	private Folder getUserRootFolder(){
		
		try {
			return DLAppLocalServiceUtil.getFolder(userRootFolderId);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			getLog().warn("User root folder does not exist.  Creating.");
			return createUserFolder(userId);
		}
		return null;
	}

	private Folder createUserFolder(long userId) {

		ServiceContext serviceContext = LiferayFacesContext.getInstance().getServiceContext();

		try {
			return DLAppLocalServiceUtil.addFolder(userId, groupId,	waveformRootFolderId, String.valueOf(userId), "", serviceContext);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private long searchFolderTreeByName(long parentFolderId, String folderName){

		try {	
			List<Folder> folders = DLAppLocalServiceUtil.getFolders(ResourceUtility.getCurrentGroupId(), parentFolderId);

			if(folders == null){
				return 0L;
			}
			
			for (Folder folder : folders) {

				if (folder.getName().equals(folderName)) {
					return folder.getFolderId();
				}
				else{
					long folderId = searchFolderTreeByName(folder.getFolderId(), folderName);
					if(folderId != 0L){
						return folderId;
					}
				}
			}

		} catch (SystemException e){
			getLog().error("Unable to retrieve folder " + folderName);
			e.printStackTrace();
		} catch (PortalException e) {
			getLog().error("Unable to retrieve folder " + folderName);
			e.printStackTrace();
		}
		return 0L;
	}

	private long findFolderIDByName(String folderName) {

		return searchFolderTreeByName(0L, folderName);
		
//		int folderCount;
//		try {
//			folderCount = DLFolderLocalServiceUtil.getDLFoldersCount();
//
//			List<DLFolder> folders = DLFolderLocalServiceUtil.getDLFolders(0, folderCount - 1);
//			for (DLFolder folder : folders) {
//				System.out.println("Checking against folder " + folder.getName());
//				if (folder.getName().equals(folderName)) {
//					System.out.println("Found folder.");
//					return folder.getFolderId();
//				}
//			}
//
//		} catch (SystemException e) {
//			getLog().error("Unable to retrieve folder " + folderName);
//			e.printStackTrace();
//		}
//		System.out.println("Didn't find folder.");
//		return 0L;
	}

	private void addChildren(Folder parentFolder, FileNode parentNode) {

		try {
			for (FileEntry file : DLAppLocalServiceUtil.getFileEntries(parentFolder.getGroupId(), parentFolder.getFolderId())) {
				new FileNode(parentNode, file.getTitle(), file.getFileEntryId(), 0L, false);
			}
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}

		try {
			for (Folder childFolder : DLAppLocalServiceUtil.getFolders(parentFolder.getGroupId(), parentFolder.getFolderId())) {
				addChildren(childFolder, new FileNode(parentNode, childFolder.getName(), childFolder.getFolderId(), 0L, true));
			}

		} catch (SystemException e) {
			e.printStackTrace();
		} catch (PortalException e) {
			e.printStackTrace();
		}
	}

	private Logger getLog() {
		return Logger.getLogger(this.getClass());
	}

	@Override
	public FileNode getRoot() {
		if (this.treeRoot == null) {
			buildTree();
		}
		return this.treeRoot;
	}
}