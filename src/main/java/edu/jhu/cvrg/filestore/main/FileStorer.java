package edu.jhu.cvrg.filestore.main;

import edu.jhu.cvrg.filestore.model.ECGFile;

public abstract class FileStorer {
	
	public abstract boolean storeFile(ECGFile file, String folderPath, String userId);
	
	public abstract boolean storeFiles();
	
	public abstract boolean fileExists(String filename, String userIdentifier, String groupIdentifier);	
}
