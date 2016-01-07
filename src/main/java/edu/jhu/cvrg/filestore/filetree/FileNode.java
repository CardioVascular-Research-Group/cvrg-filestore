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
import java.util.ArrayList;
import java.util.List;

import edu.jhu.cvrg.filestore.enums.EnumFileStoreType;

public class FileNode implements Serializable{

	private static final long serialVersionUID = -121741741906128195L;
	
	private Object content = null;
	private Long documentRecordId = null;
	private Long analysisJobId = null;
	private FileNode parentNode = null;
	private List<FileNode> childNodes = null;
	private boolean isFolder = true;
	private long uuid ;
	private String name = "";
	private String originalFileName = null;
	
	private EnumFileStoreType storeStrategy;
	
	public FileNode(FileNode parentNode, String name, long uuid, boolean isFolder, EnumFileStoreType storeStrategy, String originalFileName){
		this.name = name;
		this.parentNode = parentNode;
		this.uuid = uuid;
		this.isFolder = isFolder;
		
		if(parentNode!=null){
			parentNode.addChild(this);
		}
		this.storeStrategy = storeStrategy;
		this.originalFileName = originalFileName;
		this.originalFileName = originalFileName;
	}
	
	public void addChild(FileNode newChildNode){
		
		if(childNodes == null){
			childNodes = new ArrayList<FileNode>();
		}
		childNodes.add(newChildNode);
	}
	
	public String getName(){
		return name;
	}
	
	public List<FileNode> getChildren(){
		return childNodes;
	}
	
	public Object getContent(){
		return this.content;
	}
	
	public boolean isRoot(){
		return this.parentNode == null;
	}
	
	public boolean isLeaf(){
		return childNodes == null;
	}
	
	public Long getDocumentRecordId(){
		return this.documentRecordId;
	}
	
	public Long getAnalysisJobId(){
		return this.analysisJobId;
	}

	public boolean isFolder() {
		return isFolder;
	}
	
	public FileNode getParent(){
		return parentNode;
	}

	public long getUuid() {
		return uuid;
	}

	public void setDocumentRecordId(Long documentRecordId) {
		this.documentRecordId = documentRecordId;
	}

	public void setAnalysisJobId(Long analysisJobId) {
		this.analysisJobId = analysisJobId;
	}

	public EnumFileStoreType getStoreStrategy() {
		return storeStrategy;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}
}