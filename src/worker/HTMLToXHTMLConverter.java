package worker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;



/**
 * Tidy up HTML files to convert them to strict XHTML counterparts
 * @author ruifengm
 * @since 2017-Jul-26
 */

public class HTMLToXHTMLConverter {
	
	public static ArrayList<String> inputHTMLFileList = new ArrayList<String>();
	public static ArrayList<String> outputHTMLFileList = new ArrayList<String>();
	public static String inputHTMLParentFolder = "";
	
	public static void getinputHTMLFileList(String dir) throws FileNotFoundException {

		System.out.println("Loading HTML report files... ");
		File folder = new File(dir);
		inputHTMLParentFolder = folder.getName();
		for (File item: folder.listFiles()) {
			if (item.getName().matches("(?i)(.+)\\.html")) inputHTMLFileList.add(item.getAbsolutePath());
		}
		if (inputHTMLFileList.isEmpty()){
			System.out.println("No HTML files found in the given folder.");
			throw new FileNotFoundException("No HTML files found in the given folder.");
		}
		System.out.println("HTML files successfully loaded.");
	}
	
	public String convertToXHTML(String filePath) throws IOException {
		File inputFile = new File(filePath);
		InputStream inputStream = new FileInputStream(inputFile);
		String outputFileDir = inputFile.getParentFile().getAbsolutePath();
		String outputFileName = inputFile.getName().replace(".html", ".xhtml");
		
		System.out.println("Converting " + inputFile.getName() + " to XHTML ...");
        Tidy tidy = new Tidy(); 
        tidy.setShowWarnings(false);
        tidy.setXmlTags(false);
        tidy.setInputEncoding("UTF-8");
        tidy.setOutputEncoding("UTF-8");
        tidy.setSmartIndent(true);
        tidy.setDocType("strict");
        tidy.setXHTML(true);
        tidy.setMakeClean(true);
//        tidy.setUpperCaseAttrs(true); // not applicable to XHTML
//        tidy.setUpperCaseTags(true); // not applicalble to XHTML
        Document xmlDoc = tidy.parseDOM(inputStream, null); 
        
        String outputFilePath = outputFileDir + "/" + outputFileName;
        tidy.pprint(xmlDoc, new FileOutputStream(outputFilePath));
        
        // Inject CSS into the newly created XHTML file
        String fileContent = IOUtils.toString(new FileInputStream(outputFilePath), "UTF-8");
        String cssString = IOUtils.toString(HTMLToXHTMLConverter.class.getResourceAsStream("../config/css.txt"), "UTF-8");
        fileContent = fileContent.replaceAll("(?s)<style type=\"text/css\">(.+)</style>", cssString); // match new lines
        //fileContent = fileContent.replaceAll("style='display: none'", ""); 
        IOUtils.write(fileContent, new FileOutputStream(outputFilePath), "UTF-8");
        
        return outputFilePath;
	}

}
