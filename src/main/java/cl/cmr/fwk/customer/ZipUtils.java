package cl.cmr.fwk.customer;

import java.io.File; 
import java.io.FileInputStream; 
import java.io.FileOutputStream; 
import java.io.IOException; 
import java.util.ArrayList; 
import java.util.List; 
import java.util.zip.ZipEntry; 
import java.util.zip.ZipOutputStream;

public class ZipUtils { 

    private List <String> fileList; 

    public ZipUtils() { 
    	fileList = new ArrayList <String>(); 
    } 

    public void zipIt(String zipFile, String path) { 
    	
	    byte[] buffer = new byte[1024]; 
	    String source = new File(path.toString()).getName(); 
	    FileOutputStream fos = null; 
	    ZipOutputStream zos = null; 
	    
	    try { 
	    	
		    fos = new FileOutputStream(zipFile); 
		    zos = new ZipOutputStream(fos); 
		    FileInputStream in = null; 
		
		    for (String file: this.fileList) { 
		    	
			    ZipEntry ze = new ZipEntry(source + File.separator + file); 
			    zos.putNextEntry(ze); 
			    
			    try { 
			    	
				    in = new FileInputStream(path.toString() + File.separator + file); 
				    int len; 
				    while ((len = in .read(buffer)) > 0) { 
				    	zos.write(buffer, 0, len); 
				    } 
				    
			    } finally { 
			    	in.close(); 
			    } 
		    } 
		    
		    zos.closeEntry(); 
		    
	    } catch (IOException ex) { 
	    	
	    	ex.printStackTrace(); 
	    	
	    } finally { 
		    try { 
		    	zos.close(); 
		    } catch (IOException e) { 
		    	e.printStackTrace(); 
		    } 
	    } 
	    
    } 

    public void generateFileList(File node, String path) {
    	
	    if (node.isFile()) { 
	    	fileList.add(generateZipEntry(node.toString(), path)); 
	    } 
	    
	    if (node.isDirectory()) { 
		    String[] subNote = node.list(); 
		    for (String filename: subNote) { 
		    	generateFileList(new File(node, filename), path); 
		    } 
	    } 
	    
    } 

    private String generateZipEntry(String file, String path) { 
    	return file.substring(path.length() + 1, file.length()); 
    } 
    
} 
