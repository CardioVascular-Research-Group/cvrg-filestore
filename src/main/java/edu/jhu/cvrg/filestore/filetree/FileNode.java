package edu.jhu.cvrg.filestore.filetree;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileNode {

	private Object content = null;
	private long documentRecordId = 0L;
	private long analysisJobId = 0L;
	private FileNode parentNode = null;
	private List<FileNode> childNodes = null;
	private boolean isFolder = true;
	private UUID uuid = java.util.UUID.randomUUID();
	private String name = "";
	
	public FileNode(FileNode parentNode, String name, long documentRecordId, long analysisJobId){
		initialize(parentNode, name, documentRecordId, analysisJobId, true);
	}
	
	public FileNode(FileNode parentNode, String name, long documentRecordId, long analysisJobId, boolean isFolder){
		initialize(parentNode, name, documentRecordId, analysisJobId, isFolder);
	}
	
	private void initialize(FileNode parentNode, String name, long documentRecordId, long analysisJobId, boolean isFolder){
//		this.content = content;
		this.name = name;
		this.parentNode = parentNode;
		this.documentRecordId = documentRecordId;
		this.analysisJobId = analysisJobId;
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
	
	public long getDocumentRecordId(){
		return this.documentRecordId;
	}
	
	public long getAnalysisJobId(){
		return this.analysisJobId;
	}

	public boolean isFolder() {
		return isFolder;
	}
	
	public FileNode getParent(){
		return parentNode;
	}

	public UUID getUuid() {
		return uuid;
	}
}