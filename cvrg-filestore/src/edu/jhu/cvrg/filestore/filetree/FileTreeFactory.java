package edu.jhu.cvrg.filestore.filetree;

import edu.jhu.cvrg.filestore.enums.EnumFileStoreType;

public class FileTreeFactory {

	public static FileTree getFileTree(EnumFileStoreType type, String[] args){

		switch(type){
			case LIFERAY_61:	return new Liferay61FileTree(args);
			case HADOOP:		return new HadoopFileTree(args);
			case FILE_SYSTEM:	return new FileSystemFileTree(args);
			default:			return new Liferay61FileTree(args);
		}
	}	
}