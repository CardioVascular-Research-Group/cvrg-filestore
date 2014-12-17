package edu.jhu.cvrg.filestore.filetree;


public abstract class FileTree {
	
	public abstract void addFolder(long parentNodeUuid, String newFolderName);
	
	protected abstract void buildTree();
	
	public abstract void deleteNode(long uuid);
	
	public abstract FileNode getRoot();
}
