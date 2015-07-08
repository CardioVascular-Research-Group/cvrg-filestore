package edu.jhu.cvrg.filestore.model;

import java.util.HashMap;
import java.util.Map;

import edu.jhu.cvrg.data.enums.FileType;
import edu.jhu.cvrg.filestore.enums.EnumFileExtension;
import edu.jhu.icm.ecgFormatConverter.ECGFileData;

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
* @author Mike Shipway, Chris Jurado
* 
*/
public class ECGFileMeta {

	private String subjectID = "";
	private int subjectAge = 71;
	private String subjectSex = "Unknown";
	private String recordName = "";
	private String datatype = "";
	private String studyID = "";
	private String fileDate = "";
	private String date = "1/1/2013";
//	private EnumFileType fileType;
	private FileType fileType;
	private FSFile file;
	private Map<EnumFileExtension, FSFile> auxiliarFiles;
	private String treePath;
	private ECGFileData ecgFileData;
	
	// Used for Eureka integration
	private boolean virtual;
	
	public ECGFileMeta(String subjectID, String recordName, String datatype, String studyID) {

		ecgFileData = new ECGFileData(); 
		this.subjectID = subjectID;
		this.recordName = recordName;
		this.datatype = datatype;
		this.studyID = studyID;
	}
	
	public void setChannels(int channels){
//		this.channels = channels;
		this.ecgFileData.channels = channels;
	}

	public int getChannels() {
		return ecgFileData.channels;
	}
	
	public void setSampFrequency(float frequency){
		this.ecgFileData.samplingRate = frequency;
	}

	public float getSampFrequency() {
		return ecgFileData.samplingRate;
	}

	public String getSubjectID() {
		return subjectID;
	}

	public int getSubjectAge() {
		return subjectAge;
	}

	public String getSubjectSex() {
		return subjectSex;
	}

	public String getRecordName() {
		return recordName;
	}

	public String getDatatype() {
		return datatype;
	}

	public String getStudyID() {
		return studyID;
	}

	public String getFileDate() {
		return fileDate;
	}
	
	public void setNumberOfPoints(int points){
		this.ecgFileData.samplesPerChannel = points;
	}

	public int getNumberOfPoints() {
		return ecgFileData.samplesPerChannel;
	}

	public String getDate() {
		return date;
	}

	public FileType getFileType() {
		return fileType;
	}
	
	public void setFileType(FileType fileType){
		this.fileType = fileType;
	}

	public Map<EnumFileExtension, FSFile> getAuxiliarFiles() {
		return auxiliarFiles;
	}

	public void addAuxFile(EnumFileExtension extension, FSFile file) {
		if(auxiliarFiles == null){
			auxiliarFiles = new HashMap<EnumFileExtension, FSFile>();
		}
		auxiliarFiles.put(extension, file);
	}

	public String getTreePath() {
		return treePath;
	}

	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	public FSFile getFile() {
		return file;
	}

	public void setFile(FSFile file) {
		this.file = file;
	}

	public boolean isVirtual() {
		return virtual;
	}

	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}

}
