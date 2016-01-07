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
public class FSFolder extends FSBase{

	private static final long serialVersionUID = 8712782518984177089L;

	public FSFolder(long folderId, String folderName, long parentId) {
		this.id = folderId;
		this.name = folderName;
		this.parentId = parentId;
	}
}