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
public enum EnumFileExtension {
	
	RDT, 
	XYZ, 
	DAT, 
	HEA, 
	ZIP, 
	TXT, 
	CSV, 
	NAT, 
	GTM, 
	XML, 
	HL7;
	
	public static EnumFileExtension getTypeById(Integer id){
		
		if(id != null){
			for (EnumFileExtension e : EnumFileExtension.values()) {
				if(e.ordinal() == id){
					return e;
				}
			}
		}
		return null;
	}
}

