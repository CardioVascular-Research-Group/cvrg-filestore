package edu.jhu.cvrg.filestore.main;

import java.util.List;

import edu.jhu.cvrg.filestore.exception.FSException;
import edu.jhu.cvrg.filestore.model.FSFile;
import edu.jhu.cvrg.filestore.model.FSFolder;

public class HadoopFileStorer extends FileStorer {

	@Override
	public FSFolder getFolder(long folderId) throws FSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FSFile getFile(long fileId, boolean referenceOnly) throws FSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FSFolder> getFolders(long folderId) throws FSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FSFile> getFiles(long folderId, boolean referenceOnly) throws FSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FSFile addFile(long parentFolderId, String fileName, byte[] fileData)
			throws FSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FSFolder addFolder(long parentFolderId, String folderName)
			throws FSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFile(long fileId) throws FSException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteFolder(long folderId) throws FSException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FSFile getFileByNameAndFolder(long folderId, String fileName,
			boolean referenceOnly) throws FSException {
		// TODO Auto-generated method stub
		return null;
	}


}
