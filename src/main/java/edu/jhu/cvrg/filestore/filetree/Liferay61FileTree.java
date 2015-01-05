package edu.jhu.cvrg.filestore.filetree;

import java.util.List;

import org.apache.log4j.Logger;

import edu.jhu.cvrg.filestore.enums.EnumFileStoreType;
import edu.jhu.cvrg.filestore.exception.FSException;
import edu.jhu.cvrg.filestore.main.FileStoreFactory;
import edu.jhu.cvrg.filestore.main.FileStorer;
import edu.jhu.cvrg.filestore.model.FSFile;
import edu.jhu.cvrg.filestore.model.FSFolder;

public class Liferay61FileTree extends FileTree {

	private static final long serialVersionUID = -3353749155173931410L;
	
	private long waveformRootFolderId = 0L;
	private long userRootFolderId = 0L;
	private long userId = 0L;
	private FileNode treeRoot;
	private FileStorer filestore;
	private String extentionFilter = "hea";

	public Liferay61FileTree(String[] args) {

		//args[0] = GROUP ID
		this.userId = Long.valueOf(args[1]);
		//args[2] = COMPANY ID
		
		filestore = FileStoreFactory.returnFileStore(EnumFileStoreType.LIFERAY_61, args);
		
		if (args.length > 3) {
			waveformRootFolderId = findFolderIDByName(args[3]);
		}
		if (args.length > 4) {
			userRootFolderId = findFolderIDByName(args[4]);
			if (userRootFolderId == 0L) {
				userRootFolderId = createUserFolder().getId();
			}
		}
		buildTree();
	}

	@Override
	protected void buildTree() {

		FSFolder rootFolder = getUserRootFolder();

		if (rootFolder != null) {
			treeRoot = new FileNode(null, rootFolder.getName(), rootFolder.getId());
			addChildren(rootFolder, treeRoot);

		} else {
			getLog().error(waveformRootFolderId + " folder does not exist");
		}
	}
	
	@Override
	public void addFolder(long parentNodeUuid, String newFolderName) {
		
		FileNode parentNode = findNodeByUuid(parentNodeUuid);
		
		long parentFolderId = parentNode.getUuid();
		try {
			FSFolder newFolder = filestore.addFolder(parentFolderId, newFolderName);
			new FileNode(parentNode, newFolder.getName(), newFolder.getId());
			
		} catch (FSException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void deleteNode(long uuid){

		FileNode node = findNodeByUuid(uuid);
		if(node == null){
			return;
		}
		try {
			long folderIdToDelete = -1; 
			if(node.isFolder()){
				folderIdToDelete = node.getUuid();
			}
			else{
				folderIdToDelete = node.getParent().getUuid();
			}
			
			filestore.deleteFolder(folderIdToDelete);
		} catch (FSException e) {
			e.printStackTrace();
		}
	}
	
	private FileNode findNodeByUuid(long nodeId){
		
		if(treeRoot.getUuid() == nodeId){
			return treeRoot;
		}
		
		return checkChildNodes(treeRoot, nodeId);
	}
	
	private FileNode checkChildNodes(FileNode parentNode, long nodeId){
		
		FileNode searchNode = null;
		
		if(parentNode.getChildren() == null){
			return null;
		}
		
		for(FileNode node : parentNode.getChildren()){
			if(node.getUuid() == nodeId){
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

	private FSFolder getUserRootFolder(){
		try {
			return filestore.getFolder(userRootFolderId);
		} catch (FSException e) {
			e.printStackTrace();
			getLog().warn("User root folder does not exist.  Creating.");
			return createUserFolder();
		}
	}

	private FSFolder createUserFolder() {

		try {
			return filestore.addFolder(waveformRootFolderId, String.valueOf(userId));
		} catch (FSException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private long searchFolderTreeByName(long parentFolderId, String folderName){

		try {	
			List<FSFolder> folders = filestore.getFolders(parentFolderId);

			if(folders == null){
				return 0L;
			}
			
			for (FSFolder folder : folders) {

				if (folder.getName().equals(folderName)) {
					return folder.getId();
				}
				else{
					long folderId = searchFolderTreeByName(folder.getId(), folderName);
					if(folderId != 0L){
						return folderId;
					}
				}
			}

		} catch (FSException e) {
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

	private void addChildren(FSFolder parentFolder, FileNode parentNode) {

		try {
			for (FSFile file : filestore.getFiles(parentFolder.getId(), true)) {
				if(extentionFilter == null || extentionFilter.equalsIgnoreCase(file.getExtension())){
					new FileNode(parentNode, file.getName(), file.getId(), false);	
				}
			}
		} catch (FSException e) {
			e.printStackTrace();
		}

		try {
			for (FSFolder childFolder : filestore.getFolders(parentFolder.getId())) {
				addChildren(childFolder, new FileNode(parentNode, childFolder.getName(), childFolder.getId(), true));
			}

		} catch (FSException e) {
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