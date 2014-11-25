package edu.jhu.cvrg.filestore.main;

import edu.jhu.cvrg.filestore.enums.EnumFileStoreType;

public class FileStoreFactory {
	
	private FileStoreFactory(){}
	
	public static FileStorer returnFileStore(EnumFileStoreType fileStore, String[] args){

		switch(fileStore){
		case LIFERAY_61:
			return new Liferay61FileStorer(args);
		case HADOOP:
			return new HadoopFileStorer();
		case FILE_SYSTEM:
			return null;
		case POSTGRESSQL:
			return null;
		default:
			return null;	
		}
	}
}
