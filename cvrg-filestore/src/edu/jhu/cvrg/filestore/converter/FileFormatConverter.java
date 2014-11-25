package edu.jhu.cvrg.filestore.converter;

import java.io.File;
import java.sql.Connection;

import org.apache.log4j.Logger;

import edu.jhu.cvrg.data.dto.UploadStatusDTO;
import edu.jhu.cvrg.filestore.enums.EnumFileExtension;
import edu.jhu.cvrg.filestore.enums.EnumFileType;
import edu.jhu.cvrg.filestore.exception.UploadFailureException;
import edu.jhu.cvrg.filestore.model.ECGFile;
import edu.jhu.cvrg.filestore.model.MetaContainer;

import edu.jhu.cvrg.waveform.utility.ResourceUtility;

public class FileFormatConverter {
	private Logger log = Logger.getLogger(FileFormatConverter.class);
	
	private long conversionStartTime;
	private MetaContainer metaData = new MetaContainer();
	private Connection db = null;

	private UploadStatusDTO convertUploadedFile(ECGFile ecgFile, String outputDirectory, String uId, boolean isPublic) throws UploadFailureException {
//
//		String method = "na";
//		boolean correctFormat = true;
//		EnumFileExtension fileExtension = EnumFileExtension.valueOf(ecgFile.getFileExtension().toUpperCase());
//		
//		db = ConnectionFactory.createConnection();
//
//		switch (ecgFile.getFileType()) {
//			case WFDB:			method = "wfdbToRDT"; 			break;
//			case HL7:			method = "hL7";					break;
//			case PHILIPS_103:	method = "philips103ToWFDB";	break;
//			case PHILIPS_104:	method = "philips104ToWFDB";	break;
//			case MUSE_XML:		method = "museXML";				break;
//			default:	
//				switch (fileExtension) {
//					case RDT:	method = "rdtToWFDB16";					break;
//					case XYZ:	method = "wfdbToRDT"; 		fileType = EnumFileType.WFDB;		break;
//					case ZIP:	method = "processUnZipDir";	/* leave the fileFormat tag alone*/ break;
//					case TXT:	method = evaluateTextFile(liferayFile.getTitle());	/* will eventually process GE MUSE Text files*/	break;
//					case CSV:	method = "xyFile";						break;
//					case NAT:	method = "na";							break;
//					case GTM:	method = "na";							break;
//					default:	method = "geMuse";						break;
//				}
//			break;
//		}
//		
//		if(fileType != null){
//			metaData.setFileFormat(fileType.ordinal());
//		
//			if(EnumFileExtension.HEA.equals(fileExtension) || EnumFileExtension.DAT.equals(fileExtension)) {
//				
//				FileEntry headerFile = liferayFile;
//				if(EnumFileExtension.DAT.equals(fileExtension)){
//					headerFile = wfdbPairFile;
//				}
//				// Parse the locally held header file
//				correctFormat = checkWFDBHeader(headerFile);
//			}
//			
//			if(!correctFormat) {
//				throw new UploadFailureException("The header file has not been parsed properly");
//			}
//			
//			log.info("method = " + method);
//			
//			if(ResourceUtility.getNodeConversionService().equals("0")){
//				log.error("Missing Web Service Configuration.  Cannot run File Conversion Web Service.");
//				throw new UploadFailureException("Cannot run File Conversion Web Service. Missing Web Service Configuration.");
//			}
//	
//			if(!method.equals("na")){
//			
//				LinkedHashMap<String, String> parameterMap = new LinkedHashMap<String, String>();
//			
//				parameterMap.put("userid", String.valueOf(user.getUserId()));
//				parameterMap.put("subjectid", 	metaData.getSubjectID());
//				parameterMap.put("filename", 	liferayFile.getTitle());
//				parameterMap.put("studyID", 	metaData.getStudyID());
//				parameterMap.put("datatype", 	metaData.getDatatype());
//				parameterMap.put("treePath", 	metaData.getTreePath());
//				parameterMap.put("recordName", 	metaData.getRecordName());
//				parameterMap.put("fileSize", 	String.valueOf(metaData.getFileSize()));
//				parameterMap.put("fileFormat", 	String.valueOf(metaData.getFileFormat()));
//				
//				parameterMap.put("verbose", 	String.valueOf(false));
//				parameterMap.put("service", 	"DataConversion");
//				
//				parameterMap.put("companyId", 	String.valueOf(companyId));
//				parameterMap.put("groupId", 	String.valueOf(liferayFile.getGroupId()));
//				parameterMap.put("folderId", 	String.valueOf(liferayFile.getFolderId()));
//				
//				LinkedHashMap<String, FileEntry> filesMap = new LinkedHashMap<String, FileEntry>();
//				
//				switch (fileExtension) {
//				case HEA:
//					filesMap.put("contentFile", wfdbPairFile);
//					filesMap.put("headerFile", liferayFile);
//					break;
//				case DAT:
//					filesMap.put("contentFile", liferayFile);
//					filesMap.put("headerFile", wfdbPairFile);
//					break;
//				default:
//					filesMap.put("contentFile", liferayFile);
//					break;
//				}
//				
//	
//				log.info("Calling Web Service.");
//				
//				long conversionTime = java.lang.System.currentTimeMillis();
//				
//				OMElement result = WebServiceUtility.callWebService(parameterMap, false, method, ResourceUtility.getNodeConversionService(), null, filesMap);
//				
//				conversionTime = java.lang.System.currentTimeMillis() - conversionTime;
//				
//				if(result == null){
//					throw new UploadFailureException("Webservice return is null.");
//				}
//				
//				Map<String, OMElement> params = WebServiceUtility.extractParams(result);
//				
//				if(params == null){
//					throw new UploadFailureException("Webservice return params are null.");
//				}else{
//					if(params.get("documentId").getText() != null){
//						long docId = Long.parseLong(params.get("documentId").getText());
//						
//						log.info("["+docId+"]The runtime file validation is = " + validationTime + " milliseconds");
//						log.info("["+docId+"]The runtime for WS tranfer, read and store the document on database is = " + conversionTime + " milliseconds");
//						
//						dto.setDocumentRecordId(docId);
//						dto.setTransferReadTime(conversionTime);
//						
//						db.storeUploadStatus(dto);
//						
//						return dto;
//						
//					}else if(params.get("errorMessage").getText() != null && !params.get("errorMessage").getText().isEmpty()){
//						throw new UploadFailureException(params.get("errorMessage").getText());
//					}	
//					
//				}
//			}
//		}else{
//			throw new UploadFailureException("Unidentified file format/type.");
//		}

		return null;
	}

	// TODO: make this into a function which determines which kind of text file
	// this is, and returns the correct method to use.
	private String evaluateTextFile(String fName) {
		String method = "geMuse";

		return method;

	}
}
