package worker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Zip files
 * @author ruifengm
 * @since 2017-Jul-27
 *
 */

public class Zipper {
	
	public void zipFiles(ArrayList<String> filePathList, String outputDir) throws IOException {
		String zipFile = outputDir + "/" + HTMLToXHTMLConverter.inputHTMLParentFolder + ".zip";
		byte[] buffer = new byte[1024];
    	FileOutputStream fos = new FileOutputStream(zipFile);
    	ZipOutputStream zos = new ZipOutputStream(fos);

    	System.out.println("Output to Zip : " + zipFile);

    	for(String filePath: filePathList){
    		System.out.println("File Added : " + filePath);
    		File file = new File(filePath);
    		ZipEntry ze= new ZipEntry(file.getName());
        	zos.putNextEntry(ze);
        	FileInputStream in = new FileInputStream(file);

        	int len;
        	while ((len = in.read(buffer)) > 0) {
        		zos.write(buffer, 0, len);
        	}

        	in.close();
    	}
    	zos.closeEntry();
    	//remember close it
    	zos.close();
    	System.out.println("Zip done.");
	}

}
