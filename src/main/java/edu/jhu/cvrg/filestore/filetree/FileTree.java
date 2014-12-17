package edu.jhu.cvrg.filestore.filetree;

import java.io.Serializable;


public abstract class FileTree implements Serializable{
	
	private static final long serialVersionUID = -5767559856956618378L;

	public abstract void addFolder(long parentNodeUuid, String newFolderName);
	
	protected abstract void buildTree();
	
	public abstract void deleteNode(long uuid);
	
	public abstract FileNode getRoot();
}
