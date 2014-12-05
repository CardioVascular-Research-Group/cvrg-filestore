package edu.jhu.cvrg.filestore.enums;

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
/**
* @author Andre Vilardo
* 
*/
public enum EnumFileType {
	
	/**  Indicates an ECG file used by GE Magellan 	 **/
	GE_MAGELLAN("GE Magellan", EnumFileExtension.TXT),
	/**  Indicates an WFDB file format.  Requires both a header and a .dat file **/
	WFDB("WFDB", EnumFileExtension.HEA, EnumFileExtension.DAT),
	/**  Indicates an RDT file **/
	RDT("RDT", EnumFileExtension.RDT), 
	/**  Indicates a Holter2 formatted file 	 **/
	HOLTER12("Holter 12", EnumFileExtension.CSV),
	/**  Indicates a Holter13 formatted file 	 **/
	HOLTER3("Holter 3", EnumFileExtension.CSV), 
	/** Indicates a GE Muse file	 **/
	GE_MUSE("GE Muse", EnumFileExtension.TXT),
	/** Indicates an HL7 format	 **/
	HL7("HL7", EnumFileExtension.HL7),
	/** Indicates an xyFile (which is has a .csv extension)	 **/
	XY_FILE("XY formatted CSV file", EnumFileExtension.CSV),
	PHILIPS_103("Philips 1.03", EnumFileExtension.XML),
	PHILIPS_104("Philips 1.04", EnumFileExtension.XML),
	MUSE_XML("Muse", EnumFileExtension.XML),
	/** Indicates a Schiller ECG file (which is has a .xml extension)	 **/
	SCHILLER("Schiller", EnumFileExtension.XML);
	
	private String label;
	private EnumFileExtension[] extension;
	
	private EnumFileType(String label, EnumFileExtension ... ext) {
		this.label = label;
		this.extension = ext;
	}
	
	public static EnumFileType getTypeById(Integer id){
		
		if(id != null){
			for (EnumFileType e : EnumFileType.values()) {
				if(e.ordinal() == id){
					return e;
				}
			}
		}
		return null;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public EnumFileExtension[] getExtension() {
		return extension;
	}

	public void setExtension(EnumFileExtension[] extension) {
		this.extension = extension;
	}
}

