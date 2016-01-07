package edu.jhu.cvrg.filestore.main;
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

import java.io.Serializable;
import java.util.List;

import edu.jhu.cvrg.filestore.exception.FSException;
import edu.jhu.cvrg.filestore.model.FSFile;
import edu.jhu.cvrg.filestore.model.FSFolder;

public abstract class FileStorer implements Serializable{
	
	private static final long serialVersionUID = -8345295759269870166L;
	
	public abstract FSFolder getFolder(long folderId) throws FSException;
	public abstract FSFile getFile(long fileId, boolean referenceOnly) throws FSException;
	public abstract FSFile getFileByNameAndFolder(long folderId, String fileName, boolean referenceOnly) throws FSException;
	
	public abstract List<FSFolder> getFolders(long folderId) throws FSException;
	public abstract List<FSFile> getFiles(long folderId, boolean referenceOnly) throws FSException;
	
	
	public abstract FSFile addFile(long parentFolderId, String fileName, byte[] fileData, boolean shared) throws FSException;
	public abstract FSFolder addFolder(long parentFolderId, String folderName, boolean shared) throws FSException;
	
	public abstract void deleteFile(long fileId) throws FSException;
	public abstract void deleteFolder(long folderId) throws FSException;
}