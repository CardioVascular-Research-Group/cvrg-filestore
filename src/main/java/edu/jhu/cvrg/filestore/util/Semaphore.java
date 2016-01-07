package edu.jhu.cvrg.filestore.util;
/*
Copyright 2015 Johns Hopkins University Institute for Computational Medicine

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
public class Semaphore {
	  private int signals = 0;
	  private int bound   = 0;
	  private static Semaphore createFolderSemaphore = null;
	  private static Semaphore createFileSemaphore = null;
	  
	  public static Semaphore getCreateFolderSemaphore(){
		  if(createFolderSemaphore == null){
			  createFolderSemaphore = new Semaphore(1);
		  }
		  return createFolderSemaphore;
	  }
	  
	  public static Semaphore getCreateFileSemaphore(){
		  if(createFileSemaphore == null){
			  createFileSemaphore = new Semaphore(1);
		  }
		  return createFileSemaphore;
	  }	  
	  
	  private Semaphore(int upperBound){
	    this.bound = upperBound;
	  }

	  public synchronized void take() throws InterruptedException{
	    while(this.signals == bound) wait();
	    this.signals++;
	    this.notify();
	  }

	  public synchronized void release() throws InterruptedException{
	    while(this.signals == 0) wait();
	    this.signals--;
	    this.notify();
	  }
}