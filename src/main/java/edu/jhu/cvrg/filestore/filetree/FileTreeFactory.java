package edu.jhu.cvrg.filestore.filetree;
/*
Copyright 2013 Johns Hopkins University Institute for Computational Medicine

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
import edu.jhu.cvrg.filestore.enums.EnumFileStoreType;

public class FileTreeFactory {

	public static FileTree getFileTree(EnumFileStoreType type, String[] args){

		switch(type){
			case LIFERAY_61:	return new Liferay61FileTree(args);
			case HADOOP:		return new HadoopFileTree(args);
			case FILE_SYSTEM:	return new FileSystemFileTree(args);
			case VIRTUAL_DB:   	return new VirtualFileTree(args);
			default:			return new Liferay61FileTree(args);
		}
	}	
}