package edu.jhu.cvrg.filestore.model;
/*
Copyright 2015 Johns Hopkins University Institute for Computational Medicine

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
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