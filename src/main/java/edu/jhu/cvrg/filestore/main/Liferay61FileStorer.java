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
import java.util.List;

import org.apache.log4j.Logger;

import com.liferay.faces.portal.context.LiferayFacesContext;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.documentlibrary.DuplicateFileException;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFolderLocalServiceUtil;

import edu.jhu.cvrg.filestore.exception.UploadFailureException;
import edu.jhu.cvrg.filestore.model.ECGFile;
import edu.jhu.cvrg.waveform.utility.ResourceUtility;
import edu.jhu.cvrg.waveform.utility.Semaphore;

public class Liferay61FileStorer extends FileStorer{
	
	private long groupId;
	private long userId;
	private static Logger log = Logger.getLogger(Liferay61FileStorer.class);
	
	public Liferay61FileStorer(String[] args){
		this.groupId = Long.valueOf(args[0]);
		this.userId = Long.valueOf(args[1]);
	}

	@Override
	public boolean storeFile(ECGFile file, String folderPath, String userId){
		System.out.println("File Storer storing files to be stored in the stored file place.");
		try {
			
			String folderName = getFolderName(folderPath);
			
			//TODO [VILARDO] DEFINE THE FILE TYPE
			ServiceContext service = LiferayFacesContext.getInstance().getServiceContext();
			DLAppLocalServiceUtil.addFileEntry(this.userId, groupId, 
					findFolderIDByName(folderName), file.getFileName(), "", file.getFileName(), "", "1.0", 
					file.getEcgDataFileAsBytes(), service);
			
		} catch(DuplicateFileException e){
			log.error(e.getStackTrace());
			e.printStackTrace();
//			throw new UploadFailureException("This file already exists.", e);
		
		} catch (Exception e) {
			log.error(e.getStackTrace());
			e.printStackTrace();
//			throw new UploadFailureException("Error on file creation", e);
		}
		return true;
	}
	
	private String getFolderName(String path){
		String[] folders = path.split("|");
		int index = folders.length - 1;
		return folders[index];
	}

	public static boolean fileExists(String filename, String folderName, String userIdentifier, String groupIdentifier){

		long folderId = findFolderIDByName(folderName);
		long groupId = Long.valueOf(groupIdentifier);
		
		try {
			FileEntry liferayFile = DLAppLocalServiceUtil.getFileEntry(groupId, folderId, filename);
			if(liferayFile != null){
				return true;
			}
		} catch (PortalException e) {
			log.debug("File not found");
		} catch (SystemException e) {
			log.debug("File not found");
		}
		
		return false;
	} 
	
	public static String createFolder(String folderName, String userId) throws UploadFailureException{

		Folder recordNameFolder = null;
		Semaphore s = Semaphore.getCreateFolderSemaphore();	
		folderName = ResourceUtility.convertToLiferayDocName(folderName);
		
		try {
			s.take();
			long folderId = findFolderIDByName(folderName);
			DLFolder folder = DLFolderLocalServiceUtil.fetchDLFolder(folderId);			
			ServiceContext service = LiferayFacesContext.getInstance().getServiceContext();
			try{
				recordNameFolder = DLAppLocalServiceUtil.addFolder(Long.valueOf(userId), ResourceUtility.getCurrentGroupId(), 
						folderId, folderName, "", service);
			} catch (Exception e){
				Thread.sleep(2000);
				int tries = 5;
					
				for (int i = 0; i < tries && recordNameFolder == null; i++) {
					try{
						recordNameFolder = DLAppLocalServiceUtil.getFolder(folder.getRepositoryId(), folder.getFolderId(), folderName);
					}catch (Exception e2){
						Thread.sleep(2000);
						log.warn("Sleep and Try Again. #" + (i));
					}
				}		
			}

			if(recordNameFolder != null) {
				StringBuilder treePath = new StringBuilder();
				extractFolderHierachic(recordNameFolder, treePath);
				return treePath.toString();
			}else {
				throw new UploadFailureException("Please select a folder");
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new UploadFailureException("Error on record name folder's creation.", e);
		}finally{
			try {
				s.release();
			} catch (InterruptedException e) {
				log.error(e.getMessage());
			}
		}
	}
	
	private static void extractFolderHierachic(Folder folder, StringBuilder treePath) throws Exception {
		try {
			if(folder != null && !"waveform".equals(folder.getName())){
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
	
	private static long findFolderIDByName(String folderName){
		
		int folderCount;
		try {
			folderCount = DLFolderLocalServiceUtil.getDLFoldersCount();

			List<DLFolder> folders = DLFolderLocalServiceUtil.getDLFolders(0, folderCount -1);
			for(DLFolder folder : folders){
				if(folder.getName().equals(folderName)){
					return folder.getFolderId();
				}
			}
		
		} catch (SystemException e) {
			log.error("Unable to retrieve folder " + folderName);
			e.printStackTrace();
		}
		
		return 0L;		
	}

	@Override
	public boolean storeFiles() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fileExists(String filename, String userIdentifier, String groupIdentifier) {
		// TODO Auto-generated method stub
		return false;
	}
}