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
public class FileSystemFileTree extends FileTree {

	private static final long serialVersionUID = 1026672575639472250L;
	
	public FileSystemFileTree(String[] args) {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void buildTree() {
		// TODO Auto-generated method stub

	}

	@Override
	public FileNode getRoot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addFolder(long parentNodeUuid, String newFolderName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteNode(long uuid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FileNode getFileNodeByName(String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

}
