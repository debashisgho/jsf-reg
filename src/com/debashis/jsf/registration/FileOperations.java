package com.debashis.jsf.registration;

import java.io.File;
import java.io.FileOutputStream;


public abstract class FileOperations {

	private static final String successCode ="S";
	private static final String failureCode ="F";
	
			
 	private static FileOutputStream openFile(String filePath){
		FileOutputStream fos=null;
		try{
			File file = new File(filePath);
			boolean fileExistsFlag =true;
			try{
				fileExistsFlag = file.exists();
			}
			
			catch(Exception e){
				fileExistsFlag =false;
				System.out.println(e.toString());
			}
			
			if(fileExistsFlag){				
				System.out.println("file exists");
				fos = new FileOutputStream(file,true);				
			}
			
			else{				
				System.out.println("file does not exist");
				fos = new FileOutputStream(file);				
			}			
											
		}
				
		catch(Exception e){
			System.out.println(e.toString());			
		}
		
		return fos;
	}
	   
	public static String deleteFileData(String filePath){
		File file = new File(filePath);
		if(file.exists()){
			
		 try{							
			file.delete();
			return successCode;
		}
		 catch(Exception e){
			 return failureCode;
		 }
		}
		
		else{
			return successCode;
		}
		
		
	}
	
	public static String writeDataToFile(String filePath, String fileContent){
		FileOutputStream fos = openFile(filePath);
		try{
			fos.write(fileContent.getBytes());
			System.out.println("written to file: "+fileContent);
			fos.close();
			return successCode;
		}
		catch(Exception e){
			System.out.println(e.toString());
			return failureCode;
		}
		
	}
	
}
