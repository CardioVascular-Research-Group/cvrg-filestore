package edu.jhu.cvrg.filestore.model;

public class FSFolder extends FSBase{

	private static final long serialVersionUID = 8712782518984177089L;

	public FSFolder(long folderId, String folderName, long parentId) {
		this.id = folderId;
		this.name = folderName;
		this.parentId = parentId;
	}
}
