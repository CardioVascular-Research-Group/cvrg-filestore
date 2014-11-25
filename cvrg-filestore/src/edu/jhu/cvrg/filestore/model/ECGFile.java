package edu.jhu.cvrg.filestore.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import edu.jhu.cvrg.filestore.enums.EnumFileType;

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
public class ECGFile {

	private byte[] ecgDataFile;
	private String fileName = "";
	private long fileSize = 0;
	private int channels = 1;
	private float sampFrequency = 250;
	private String subjectID = "";
	private int subjectAge = 71;
	private String subjectSex = "Unknown";
	private String recordName = "";
	private String datatype = "";
	private String studyID = "";
	private String fileDate = "";
	private int numberOfPoints = 0;
	private String date = "1/1/2013";
	private EnumFileType fileType;

	public ECGFile(byte[] fileBytes, String fileName, long fileSize, 
			String subjectID, String recordName,
			String datatype, String studyID) {

		this.ecgDataFile = fileBytes;
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.subjectID = subjectID;
		this.recordName = recordName;
		this.datatype = datatype;
		this.studyID = studyID;
	}
	
	public String getFileExtension(){
		return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
	}
	
	public String getFileName() {
		return fileName;
	}

	public long getFileSize() {
		return fileSize;
	}
	
	public void setChannels(int channels){
		this.channels = channels;
	}

	public int getChannels() {
		return channels;
	}
	
	public void setSampFrequency(float frequency){
		this.sampFrequency = frequency;
	}

	public float getSampFrequency() {
		return sampFrequency;
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
		this.numberOfPoints = points;
	}

	public int getNumberOfPoints() {
		return numberOfPoints;
	}

	public String getDate() {
		return date;
	}

	public EnumFileType getFileType() {
		return fileType;
	}
	
	public void setFileType(EnumFileType fileType){
		this.fileType = fileType;
	}

	public InputStream getEcgDataFileAsInputStream(){
		return new ByteArrayInputStream(ecgDataFile);
	}

	public byte[] getEcgDataFileAsBytes() {
		return ecgDataFile;
	}

}
