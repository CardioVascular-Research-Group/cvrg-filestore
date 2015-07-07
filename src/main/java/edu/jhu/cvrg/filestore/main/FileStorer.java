package edu.jhu.cvrg.filestore.main;

import java.util.List;

import edu.jhu.cvrg.filestore.exception.FSException;
import edu.jhu.cvrg.filestore.model.FSFile;
import edu.jhu.cvrg.filestore.model.FSFolder;

public abstract class FileStorer {
	
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
