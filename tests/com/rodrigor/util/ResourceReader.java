package com.rodrigor.util;

import java.io.File;
import java.net.URL;

public class ResourceReader {
	
	private File baseDir;
	private File workingDir;


	public File readFile(String filename){
		return new File(workingDir.getPath() + "/" + filename);
	}
		
	
	public ResourceReader(){
		URL url = ResourceReader.class.getResource("/");
		File classesDir = new File(url.getPath());
		this.baseDir = new File(classesDir.getParentFile().getPath());
	}

	public ResourceReader(String workingDir){
		this();
		this.setWorkingDir(workingDir);
	}
	
	public void setWorkingDir(String wd){
		if(!wd.startsWith("/"))
			wd = "/"+wd;
		this.workingDir = new File(baseDir.getPath()+wd);
	}
	
	public File getWorkingDir(){
		return this.workingDir;
	}
	
	public File getBaseDir(){
		return this.baseDir;
	}

}
