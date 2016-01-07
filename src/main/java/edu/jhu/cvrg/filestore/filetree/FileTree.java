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
import java.io.Serializable;

public abstract class FileTree implements Serializable{
	
	private static final long serialVersionUID = -5767559856956618378L;

	public abstract void addFolder(long parentNodeUuid, String newFolderName);
	
	protected abstract void buildTree();
	
	public abstract void deleteNode(long uuid);
	
	public abstract FileNode getRoot();
	
	public abstract FileNode getFileNodeByName(String fileName);
}