package edu.jhu.cvrg.filestore.filetree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
	
	public FileNode(FileNode parentNode, String name, long uuid){
		initialize(parentNode, name, uuid, true);
	}
	
	public FileNode(FileNode parentNode, String name, long uuid, boolean isFolder){
		initialize(parentNode, name, uuid, isFolder);
	}
	
	private void initialize(FileNode parentNode, String name, long uuid, boolean isFolder){
		this.name = name;
		this.parentNode = parentNode;
		this.uuid = uuid;
		this.isFolder = isFolder;
		
		if(parentNode!=null){
			parentNode.addChild(this);
		}
		
		System.out.println("New node created with UUID " + this.uuid);
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
}