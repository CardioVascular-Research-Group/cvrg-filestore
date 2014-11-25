package edu.jhu.cvrg.filestore.filetree;

import java.util.UUID;

public abstract class FileTree {
	
	public abstract void addFolder(UUID parentNodeUuid, String newFolderName);
	
	protected abstract void buildTree();
	
	public abstract void deleteNode(UUID uuid);
	
	public abstract FileNode getRoot();
}
