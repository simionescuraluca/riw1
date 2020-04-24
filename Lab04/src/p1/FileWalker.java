package p1;

import java.io.File;
import java.util.List;

public class FileWalker {
	public void walk( String path,List<File> files ) 
	{
		File directory = new File(path);

	    // Get all files from a directory.
	    File[] fList = directory.listFiles();
	    if(fList != null)
	        for (File file : fList) 
	        {      
	            if (file.isFile()) 
	            {
	                files.add(file);
	            } 
	            else if (file.isDirectory()) 
	            {
	                walk(file.getAbsolutePath(), files);
	            }
	        }
	}
}
