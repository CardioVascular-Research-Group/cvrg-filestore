package edu.jhu.cvrg.filestore.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class FSFile extends FSBase{

	private static final long serialVersionUID = 8668969702658016794L;
	
	private String extension;
	private byte[] fileData;
	private long fileSize;

	public FSFile(long fileId, String fileName, String extension, long parentId, byte[] fileData, long fileSize) {
		this.id = fileId;
		this.name = fileName;
		this.extension = extension;
		this.parentId = parentId;
		this.fileData = fileData;
		this.fileSize = fileSize;
	}

	public String getExtension() {
		return extension;
	}
	public InputStream getFileDataAsInputStream(){
		if(fileData == null){
			return null;
		}
		return new ByteArrayInputStream(fileData);
	}

	public byte[] getFileDataAsBytes() {
		return fileData;
	}

	public long getFileSize() {
		return fileSize;
	}
	
	public String getNameWithoutExtension(){
		
		return this.getName().substring(0, this.getName().lastIndexOf(extension)-1);
	}
}
