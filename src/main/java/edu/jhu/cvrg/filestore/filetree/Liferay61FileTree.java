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
import java.util.List;

import org.apache.log4j.Logger;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFolderLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.permission.DLFolderPermission;

import edu.jhu.cvrg.filestore.enums.EnumFileStoreType;
import edu.jhu.cvrg.filestore.exception.FSException;
import edu.jhu.cvrg.filestore.main.FileStoreFactory;
import edu.jhu.cvrg.filestore.main.FileStorer;
import edu.jhu.cvrg.filestore.model.FSFile;
import edu.jhu.cvrg.filestore.model.FSFolder;

public class Liferay61FileTree extends FileTree {

	private static final long serialVersionUID = -3353749155173931410L;
	
	private long waveformRootFolderId = 0L;
	private long userRootFolderId = 0L;
	private long userId = 0L;
	private long groupId = 0L;
	private FileNode treeRoot;
	private FileStorer filestore;
	private String extentionFilter = "hea";
	private boolean groupFolder = false;

	public Liferay61FileTree(String[] args) {
		System.out.println("Number of arguments is " + args.length);
		for(String argument: args){
			System.out.println(argument);
		}

		this.groupId = Long.valueOf(args[0]);
		this.userId = Long.valueOf(args[1]);
		//args[2] = COMPANY ID
		if (args.length > 3) {
			waveformRootFolderId = findFolderIDByName(args[3]);
		}
		
		filestore = FileStoreFactory.returnFileStore(EnumFileStoreType.LIFERAY_61, args);
		userRootFolderId = findFolderIDByName(args[1]);
		if (userRootFolderId == 0L) {
			userRootFolderId = createUserFolder().getId();
		}
//		userRootFolderId = waveformRootFolderId;

		if(args.length > 4){
			userRootFolderId = waveformRootFolderId;
			groupFolder = true;
		}

		buildTree();
	}

	@Override
	protected void buildTree() {

		FSFolder userRootFolder = getUserRootFolder();
		FSFolder waveformRootFolder = getWaveformRootFolder();

		if (userRootFolder != null && waveformRootFolder != null) {
			treeRoot = new FileNode(null, waveformRootFolder.getName(), waveformRootFolder.getId(), true, EnumFileStoreType.LIFERAY_61, null);
			
			addChildren(waveformRootFolder, treeRoot);

		} else {
			getLog().error(waveformRootFolderId + " folder does not exist");
		}
	}
	
	@Override
	public void addFolder(long parentNodeUuid, String newFolderName) {
		
		FileNode parentNode = findNodeByUuid(parentNodeUuid);
		
		long parentFolderId = parentNode.getUuid();
		try {
			FSFolder newFolder = filestore.addFolder(parentFolderId, newFolderName, false);
			new FileNode(parentNode, newFolder.getName(), newFolder.getId(), true, EnumFileStoreType.LIFERAY_61, null);
			
		} catch (FSException e) {
			getLog().error("Error on addFolder. ["+e.getMessage()+"]");
			e.printStackTrace();
		}
	}
	
	@Override
	public void deleteNode(long uuid){

		FileNode node = findNodeByUuid(uuid);
		if(node == null){
			return;
		}
		try {
			long folderIdToDelete = -1; 
			if(node.isFolder()){
				folderIdToDelete = node.getUuid();
			}
			else{
				folderIdToDelete = node.getParent().getUuid();
			}
			
			filestore.deleteFolder(folderIdToDelete);
		} catch (FSException e) {
			getLog().error("Error on deleteNode. ["+e.getMessage()+"]");
			e.printStackTrace();
		}
	}
	
	private FileNode findNodeByUuid(long nodeId){
		
		if(treeRoot.getUuid() == nodeId){
			return treeRoot;
		}
		
		return checkChildNodes(treeRoot, nodeId);
	}
	
	private FileNode checkChildNodes(FileNode parentNode, long nodeId){
		
		FileNode searchNode = null;
		
		if(parentNode.getChildren() == null){
			return null;
		}
		
		for(FileNode node : parentNode.getChildren()){
			if(node.getUuid() == nodeId){
				return node;
			}
			else{
				searchNode = checkChildNodes(node, nodeId);
				if(searchNode != null){
					return searchNode;
				}
			}
		}
		return searchNode;
	}

	private FSFolder getUserRootFolder(){
		try {
			return filestore.getFolder(userRootFolderId);
		} catch (FSException e) {
			e.printStackTrace();
			getLog().warn("User root folder does not exist.  Creating.");
			return createUserFolder();
		}
	}
	
	private FSFolder getWaveformRootFolder(){
		try {
			return filestore.getFolder(waveformRootFolderId);
		} catch (FSException e) {
			e.printStackTrace();
			return null;
		}
	}

	private FSFolder createUserFolder() {

		try {
			return filestore.addFolder(waveformRootFolderId, String.valueOf(userId), false);
		} catch (FSException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private long searchFolderTreeByName(long parentFolderId, String folderName){

		try {	
			List<FSFolder> folders = filestore.getFolders(parentFolderId);

			if(folders == null){
				return 0L;
			}
			
			for (FSFolder folder : folders) {

				if (folder.getName().equals(folderName)) {
					return folder.getId();
				}
				else{
					long folderId = searchFolderTreeByName(folder.getId(), folderName);
					if(folderId != 0L){
						return folderId;
					}
				}
			}

		} catch (FSException e) {
			e.printStackTrace();
		}
		
		return 0L;
	}

	private long findFolderIDByName(String folderName) {
		
		System.out.println("Folder name is " + folderName);

//		return searchFolderTreeByName(0L, folderName);
		
		int folderCount;
		try {
			folderCount = DLFolderLocalServiceUtil.getDLFoldersCount();

			List<DLFolder> folders = DLFolderLocalServiceUtil.getDLFolders(0, folderCount);
			for (DLFolder folder : folders) {
				System.out.println("Checking against folder " + folder.getName());
				if (folder.getName().equals(folderName)) {
					System.out.println("Found folder.");
					return folder.getFolderId();
				}
			}

		} catch (SystemException e) {
			getLog().error("Unable to retrieve folder " + folderName);
			e.printStackTrace();
		}
		System.out.println("Didn't find folder.");
		return 0L;
	}

	private void addChildren(FSFolder parentFolder, FileNode parentNode) {

		try {
			for (FSFile file : filestore.getFiles(parentFolder.getId(), true)) {
					if(parentFolder.getName().equals(file.getNameWithoutExtension())){
						new FileNode(parentNode, file.getNameWithoutExtension(), file.getId(), false, EnumFileStoreType.LIFERAY_61, file.getName());
						break;
					}
			}
		} catch (FSException e) {
			e.printStackTrace();
		}

		try {
			for (FSFolder childFolder : filestore.getFolders(parentFolder.getId())) {
				if(hasPermission(childFolder)){
					if(!(groupFolder && childFolder.getName().equals(String.valueOf(this.userId)))){
						addChildren(childFolder, new FileNode(parentNode, childFolder.getName(), childFolder.getId(), true, EnumFileStoreType.LIFERAY_61, null));
					}
				}
			}

		} catch (FSException e) {
			e.printStackTrace();
		}
	}
	
	private boolean hasPermission(FSFolder folder){
		try {
			DLFolder dlFolder = DLFolderLocalServiceUtil.getDLFolder(folder.getId());
			PermissionChecker permissionChecker = PermissionThreadLocal.getPermissionChecker();	
			DLFolderPermission.check(permissionChecker, groupId, dlFolder.getFolderId(), ActionKeys.VIEW);
		} catch (PortalException e) {
			System.out.println("Something break?  Portal Exception");
			e.printStackTrace();
			return false;
		} catch (SystemException e) {
			System.out.println("Something break?  System Exception");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private Logger getLog() {
		return Logger.getLogger(this.getClass());
	}

	@Override
	public FileNode getRoot() {
		if (this.treeRoot == null) {
			buildTree();
		}
		return this.treeRoot;
	}

	@Override
	public FileNode getFileNodeByName(String fileName) {
		FileNode targetNode = findNodeByName(fileName, this.getRoot());
		
		return targetNode;
	}
	
	
	private FileNode findNodeByName(String name, FileNode startNode){

		FileNode foundNode = null;
		
		if(name.equals(startNode.getOriginalFileName()) && !startNode.isFolder()){
			return startNode;
		}
		List<FileNode> children = startNode.getChildren();
		if(children != null){
			for(FileNode childNode : children){
				if(name.equals(childNode.getOriginalFileName()) && !childNode.isFolder()){
					return childNode;
				}
	
				if(childNode.getChildren() != null){
					foundNode = findNodeByName(name, childNode);
					if(foundNode != null){
						break;
					}
				}
			}
		}
		return foundNode;
	}
}