package edu.jhu.cvrg.filestore.filetree;

import java.util.Collection;
import java.util.List;

import edu.jhu.cvrg.data.dto.VirtualNodeDTO;
import edu.jhu.cvrg.data.factory.Connection;
import edu.jhu.cvrg.data.factory.ConnectionFactory;
import edu.jhu.cvrg.data.util.DataStorageException;
import edu.jhu.cvrg.filestore.enums.EnumFileStoreType;
import edu.jhu.cvrg.filestore.exception.FSException;
import edu.jhu.cvrg.filestore.main.FileStoreFactory;
import edu.jhu.cvrg.filestore.main.FileStorer;
import edu.jhu.cvrg.filestore.model.FSFolder;

public class VirtualFileTree extends FileTree {

	private static final long serialVersionUID = -5282697283470114180L;
	
	private FileNode treeRoot;
	private long userId;
	private FileStorer storer;
	
	private static String ROOT_FOLDER_NAME = "Eureka";

	public VirtualFileTree(String[] args) {
		//args[1] = USER ID
		this.userId = Long.valueOf(args[1]);
		
		storer = FileStoreFactory.returnFileStore(EnumFileStoreType.LIFERAY_61, args);
	}

	@Override
	protected void buildTree() {
		
		try {
			
			long rootFolderId = this.getFolderIdByName(storer, 0L, ROOT_FOLDER_NAME.toLowerCase());
			
			if(rootFolderId > 0L){
				Connection con = ConnectionFactory.createConnection();
				
				Collection<VirtualNodeDTO> virtualNodes = con.getAllVirtualNodesByUser(this.userId);
				
				this.treeRoot = new FileNode(null, ROOT_FOLDER_NAME, rootFolderId, true, EnumFileStoreType.VIRTUAL_DB);
				
				this.exploreNode(treeRoot, virtualNodes);
			}
			
		} catch (DataStorageException e) {
			e.printStackTrace();
		}
	}
	
	private void exploreNode(FileNode treeRoot, Collection<VirtualNodeDTO> children){
		for (VirtualNodeDTO nodeDto : children) {
			FileNode folderNode = new FileNode(treeRoot, nodeDto.getName(), nodeDto.getId(), nodeDto.isFolder(), EnumFileStoreType.VIRTUAL_DB);
			if(nodeDto.getChildren() != null){
				exploreNode(folderNode, nodeDto.getChildren());
			}
			
		}
	}
	
	private long getFolderIdByName(FileStorer filestore, long parentFolderId, String folderName){

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
					long folderId = getFolderIdByName(filestore, folder.getId(), folderName);
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
	
	@Override
	public FileNode getRoot() {
		if (this.treeRoot == null) {
			buildTree();
		}
		return this.treeRoot;
	}

	@Override
	public FileNode getFileNodeByName(String fileName) {
		return null;
	}

	@Override
	public void addFolder(long parentNodeUuid, String newFolderName) {
	}

	@Override
	public void deleteNode(long uuid) {
	}

}
