package edu.jhu.cvrg.filestore.main;
/*
Copyright 2014 Johns Hopkins University Institute for Computational Medicine

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
/**
* @author Chris Jurado, Mike Shipway, Brandon Benitez
* 
*/
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.faces.portal.context.LiferayFacesContext;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.model.ResourceAction;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.ResourcePermission;
import com.liferay.portal.model.Role;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.ResourceActionLocalServiceUtil;
import com.liferay.portal.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.documentlibrary.DuplicateFileException;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil;

import edu.jhu.cvrg.filestore.exception.FSException;
import edu.jhu.cvrg.filestore.model.FSFile;
import edu.jhu.cvrg.filestore.model.FSFolder;
import edu.jhu.cvrg.filestore.util.FileStoreConstants;
import edu.jhu.cvrg.filestore.util.Semaphore;

public class Liferay61FileStorer extends FileStorer{
	
	private long groupId;
	private long userId;
	private long companyId;
	private static Logger log = Logger.getLogger(Liferay61FileStorer.class);
	
	private static final List<String[]> patterns = new ArrayList<String[]>();
	static{
		patterns.add(new String[]{"[\\[&@}:,=>/<{%|+#?\"\'\\;*~\\]]", "_"});
		patterns.add(new String[]{"[\n\r]", ""});
	}
	
	
	public Liferay61FileStorer(String[] args){
		this.groupId = Long.valueOf(args[0]);
		this.userId = Long.valueOf(args[1]);
		this.companyId = Long.valueOf(args[2]);
	}

	@Override
	public FSFolder getFolder(long folderId) throws FSException {
		
		try {
			
			Folder folder = DLAppLocalServiceUtil.getFolder(folderId);
			
			return new FSFolder(folder.getFolderId(), folder.getName(), folder.getParentFolderId());
		} catch (Exception e) {
			log.warn("Folder ID [" + folderId + "] does not exists.");
		}
		return null;
	}

	@Override
	public FSFile getFile(long fileId, boolean referenceOnly) throws FSException {
		try {
			
			FileEntry file = DLAppLocalServiceUtil.getFileEntry(fileId);
			
			byte[] data = null;
			if(!referenceOnly){
				data = new byte[Long.valueOf(file.getSize()).intValue()];
				file.getContentStream().read((data));
			}
			
			return new FSFile(file.getFileEntryId(), file.getTitle(), file.getExtension(), file.getFolderId(), data, file.getSize());
		} catch (Exception e) {
			log.warn("File ID [" + fileId + "] does not exists.");
		}
		return null;
	}
	
	@Override
	public FSFile getFileByNameAndFolder(long folderId, String fileName, boolean referenceOnly) throws FSException {
		try {
			
			List<FileEntry> filesInFolder = DLAppLocalServiceUtil.getFileEntries(groupId, folderId);
			
			FileEntry file = null;
			
			for (FileEntry f : filesInFolder) {
				if(f.getTitle().equals(fileName)){
					file = f;
					break;
				}
			}
			
			if(file != null){
				byte[] data = null;
				if(!referenceOnly){
					data = new byte[Long.valueOf(file.getSize()).intValue()];
					file.getContentStream().read((data));
				}
				
				return new FSFile(file.getFileEntryId(), file.getTitle(), file.getExtension(), file.getFolderId(), data, file.getSize());
			}
		} catch (Exception e) {
			log.warn("File name [" + fileName + "] in folder ID [" + folderId + "] does not exists.");
		}
		return null;
	}

	@Override
	public List<FSFolder> getFolders(long folderId) throws FSException {
		try {
			
			List<Folder> subFolders = DLAppLocalServiceUtil.getFolders(groupId, folderId);
			
			if(subFolders != null){
				List<FSFolder> fsSubFolders = new ArrayList<FSFolder>();
				for (Folder sub : subFolders) {
					fsSubFolders.add(new FSFolder(sub.getFolderId(), sub.getName(), sub.getParentFolderId()));
				}
				return fsSubFolders;
			}
			
		} catch (Exception e) {
			log.warn("Folder ID [" + folderId + "] does not exists.");
		}
		return null;
	}

	@Override
	public List<FSFile> getFiles(long folderId, boolean referenceOnly) throws FSException {
		try {
			
			List<FileEntry> files = DLAppLocalServiceUtil.getFileEntries(groupId, folderId);
			
			if(files != null){
				List<FSFile> fsSubFolders = new ArrayList<FSFile>();
				for (FileEntry f : files) {
					byte[] data = null;
					if(!referenceOnly){
						data = new byte[Long.valueOf(f.getSize()).intValue()];
						f.getContentStream().read((data));
					}
					fsSubFolders.add(new FSFile(f.getFileEntryId(), f.getTitle(), f.getExtension(), f.getFolderId(), data, f.getSize()));
				
				}
				return fsSubFolders;
			}
			
		} catch (Exception e) {
			log.warn("Folder ID [" + folderId + "] does not exists.");
		}
		return null;
	}

	@Override
	public synchronized FSFile addFile(long parentFolderId, String fileName, byte[] fileData, boolean shared) throws FSException {
		try {
			
			FSFolder parentFolder  = this.getFolder(parentFolderId);
			if(parentFolder == null){
				throw new FSException("Folder ID [" + parentFolderId + "] does not exists.");
			}
			
			//TODO [VILARDO] DEFINE THE FILE TYPE
			ServiceContext service = LiferayFacesContext.getInstance().getServiceContext();
			FileEntry newFile = DLAppLocalServiceUtil.addFileEntry(userId, groupId, parentFolderId, fileName, "", fileName, "", "1.0", fileData, service);
			
			if(newFile != null) {
				Role userRole = RoleLocalServiceUtil.getRole(companyId, FileStoreConstants.AXIS_USER_ROLE_NAME);
				ResourcePermission resourcePermission = null;

				resourcePermission = ResourcePermissionLocalServiceUtil.createResourcePermission(CounterLocalServiceUtil.increment());
				resourcePermission.setCompanyId(companyId);
				resourcePermission.setName(DLFileEntry.class.getName());
				resourcePermission.setScope(ResourceConstants.SCOPE_INDIVIDUAL);
				resourcePermission.setPrimKey(String.valueOf(newFile.getPrimaryKey()));
				resourcePermission.setRoleId(userRole.getRoleId());
				
				ResourceAction resourceActionDelete = ResourceActionLocalServiceUtil.getResourceAction(DLFolder.class.getName(), ActionKeys.DELETE);
				ResourceAction resourceActionView = ResourceActionLocalServiceUtil.getResourceAction(DLFolder.class.getName(), ActionKeys.VIEW);
				resourcePermission.setActionIds(resourceActionDelete.getBitwiseValue()+resourceActionView.getBitwiseValue());
				ResourcePermissionLocalServiceUtil.addResourcePermission(resourcePermission);
				
				if(shared){
					userRole = RoleLocalServiceUtil.getRole(companyId, FileStoreConstants.SITE_MEMBER_ROLE_NAME);
					
					resourcePermission = ResourcePermissionLocalServiceUtil.createResourcePermission(CounterLocalServiceUtil.increment());
					resourcePermission.setCompanyId(companyId);
					resourcePermission.setName(DLFileEntry.class.getName());
					resourcePermission.setScope(ResourceConstants.SCOPE_INDIVIDUAL);
					resourcePermission.setPrimKey(String.valueOf(newFile.getPrimaryKey()));
					resourcePermission.setRoleId(userRole.getRoleId());
					
					resourcePermission.setActionIds(resourceActionView.getBitwiseValue());
					ResourcePermissionLocalServiceUtil.addResourcePermission(resourcePermission);	
				}
				
				return new FSFile(newFile.getFileEntryId(), newFile.getTitle(), newFile.getExtension(), newFile.getFolderId(), fileData, newFile.getSize());
			}else {
				throw new FSException("Please select a file");
			}
			
			
		} catch(DuplicateFileException e){
			log.error(e.getStackTrace());
			e.printStackTrace();
			throw new FSException("This file already exists.", e);
		
		} catch (Exception e) {
			log.error(e.getStackTrace());
			e.printStackTrace();
			throw new FSException("Error on file creation", e);
		}
	}

	@Override
	public synchronized FSFolder addFolder(long parentFolderId, String folderName, boolean shared) throws FSException{
		Folder newFolder = null;
		Semaphore s = Semaphore.getCreateFolderSemaphore();	
		folderName = Liferay61FileStorer.convertToLiferayFolderName(folderName);
		
		try {
			s.take();
			
			FSFolder parentFolder  = this.getFolder(parentFolderId);
			if(parentFolder == null){
				throw new FSException("Folder ID [" + parentFolderId + "] does not exists.");
			}
			
			List<FSFolder> subFolders = this.getFolders(parentFolderId);
			if (subFolders != null) {
				for (FSFolder subFolder : subFolders) {
					if(folderName.equals(subFolder.getName())){
						return subFolder;
					}
				}
			}
			
			ServiceContext service = LiferayFacesContext.getInstance().getServiceContext();
			try{
				newFolder = DLAppLocalServiceUtil.addFolder(userId, groupId, parentFolderId, folderName, "", service);
			} catch (Exception e){
				Thread.sleep(2000);
				int tries = 5;
					
				for (int i = 0; i < tries && newFolder == null; i++) {
					try{
						newFolder = DLAppLocalServiceUtil.getFolder(groupId, parentFolderId, folderName);
					}catch (Exception e2){
						Thread.sleep(2000);
						log.warn("Sleep and Try Again. #" + (i));
					}
				}		
			}

			if(newFolder != null) {
				Role userRole = RoleLocalServiceUtil.getRole(companyId, FileStoreConstants.AXIS_USER_ROLE_NAME);
				ResourcePermission resourcePermission = null;

				resourcePermission = ResourcePermissionLocalServiceUtil.createResourcePermission(CounterLocalServiceUtil.increment());
				resourcePermission.setCompanyId(companyId);
				resourcePermission.setName(DLFolder.class.getName());
				resourcePermission.setScope(ResourceConstants.SCOPE_INDIVIDUAL);
				resourcePermission.setPrimKey(String.valueOf(newFolder.getPrimaryKey()));
				resourcePermission.setRoleId(userRole.getRoleId());
				
				ResourceAction resourceActionDelete = ResourceActionLocalServiceUtil.getResourceAction(DLFolder.class.getName(), ActionKeys.DELETE);
				ResourceAction resourceActionView = ResourceActionLocalServiceUtil.getResourceAction(DLFolder.class.getName(), ActionKeys.VIEW);
				ResourceAction resourceActionAccess = ResourceActionLocalServiceUtil.getResourceAction(DLFolder.class.getName(), ActionKeys.ACCESS);
				ResourceAction resourceActionAddDoc = ResourceActionLocalServiceUtil.getResourceAction(DLFolder.class.getName(), ActionKeys.ADD_DOCUMENT);
				resourcePermission.setActionIds(resourceActionDelete.getBitwiseValue()+resourceActionView.getBitwiseValue()+resourceActionAccess.getBitwiseValue()+resourceActionAddDoc.getBitwiseValue());
				ResourcePermissionLocalServiceUtil.addResourcePermission(resourcePermission);
				
				if(shared){
					userRole = RoleLocalServiceUtil.getRole(companyId, FileStoreConstants.SITE_MEMBER_ROLE_NAME);
					
					resourcePermission = ResourcePermissionLocalServiceUtil.createResourcePermission(CounterLocalServiceUtil.increment());
					resourcePermission.setCompanyId(companyId);
					resourcePermission.setName(DLFolder.class.getName());
					resourcePermission.setScope(ResourceConstants.SCOPE_INDIVIDUAL);
					resourcePermission.setPrimKey(String.valueOf(newFolder.getPrimaryKey()));
					resourcePermission.setRoleId(userRole.getRoleId());
					
					resourcePermission.setActionIds(resourceActionView.getBitwiseValue()+resourceActionAccess.getBitwiseValue()+resourceActionAddDoc.getBitwiseValue());
					ResourcePermissionLocalServiceUtil.addResourcePermission(resourcePermission);
				}
				
				return new FSFolder(newFolder.getFolderId(), newFolder.getName(), newFolder.getParentFolderId());
			}else {
				throw new FSException("Please select a folder");
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new FSException("Error on record name folder's creation.", e);
		}finally{
			try {
				s.release();
			} catch (InterruptedException e) {
				log.error(e.getMessage());
			}
		}
	}
	
	private static String convertToLiferayFolderName(final String name) {
		String str = name;
		
		for (String[] strings : patterns) {
			str = str.replaceAll(strings[0], strings[1]);
		}
		
		return str;
	}
	
	private static void extractFolderHierachic(Folder folder, StringBuilder treePath) throws Exception {
		try {
			if(folder != null && !FileStoreConstants.WAVEFORM_ROOT_FOLDER_NAME.equals(folder.getName())){
				if(folder.getParentFolder() != null){
					extractFolderHierachic(folder.getParentFolder(), treePath);
				}
				treePath.append('/').append(folder.getName());
			}
		} catch (Exception e) {
			log.error("Problems with the liferay folder structure");
			throw e;
		}
	}

	@Override
	public void deleteFile(long fileId) throws FSException {
		try {
			DLAppLocalServiceUtil.deleteFileEntry(fileId);
		} catch (Exception e) {
			throw new FSException("Error on file delete.", e);
		}
		
	}

	@Override
	public void deleteFolder(long folderId) throws FSException {
		try {
			DLAppLocalServiceUtil.deleteFolder(folderId);
		} catch (Exception e) {
			throw new FSException("Error on folder delete.", e);
		}
		
	}
	
}