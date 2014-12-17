package edu.jhu.cvrg.filestore.model;

import java.io.Serializable;

public class FSBase implements Serializable{

	private static final long serialVersionUID = -4617441943217609099L;
	
	protected long id;
	protected String name;
	protected long parentId;
	
	public long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public long getParentId() {
		return parentId;
	}

}
