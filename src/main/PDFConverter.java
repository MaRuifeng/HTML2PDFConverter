package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.lowagie.text.DocumentException;

import worker.HTMLToXHTMLConverter;
import worker.XHTMLToPDFConverter;
import worker.Zipper;


/**
 * This is the main converter where the service will be called.
 * @author ruifengm
 * @since 2017-Jul-26
 */

public class PDFConverter {
	
	public static String outputFolder = "";
	
	private static void printUsage(){
		System.out.println("Designed usage of HTMLToPDFConverter: \n" +
				"java -jar HTMLToPDFConverter.jar arg1 arg2\n" +
				"where arg1 is the source HTML file directory\n" +
				"and arg2 is the ouput PDF file directory.\n\n" +
				"Example: java -jar HTMLToPDFConverter.jar C:/html_reports C:/pdf_reports");
	}
	
	public void convert (String inputDir, String outputDir) throws IOException, DocumentException, FileNotFoundException {
		// Convert HTML to XHTML
	    HTMLToXHTMLConverter.getinputHTMLFileList(inputDir);
		HTMLToXHTMLConverter xhtmlConverter = new HTMLToXHTMLConverter(); 
		for (String htmlFilePath: HTMLToXHTMLConverter.inputHTMLFileList) {
			HTMLToXHTMLConverter.outputHTMLFileList.add(xhtmlConverter.convertToXHTML(htmlFilePath));
		}
		
		// Convert XHTML to PDF or CSV
		XHTMLToPDFConverter pdfConverter = new XHTMLToPDFConverter();
		outputFolder = outputDir + "/" + HTMLToXHTMLConverter.inputHTMLParentFolder + "_PDF";
		new File(outputFolder).mkdir();
		for (String xhtmlFilePath: HTMLToXHTMLConverter.outputHTMLFileList) {
			if (xhtmlFilePath.contains("trades.xhtml")) {
				// too many data entries, convert to CSV
				// XHTMLToPDFConverter.outputFileList.add(pdfConverter.convertHtmlTableToCSV(xhtmlFilePath, outputDir));
			}
			else {
				// convert to PDF
				XHTMLToPDFConverter.outputFileList.add(pdfConverter.convertToPDF(xhtmlFilePath, outputFolder));
			}
		}
		
		// Zip files
		Zipper zipper = new Zipper();
		zipper.zipFiles(XHTMLToPDFConverter.outputFileList, outputFolder);
	}
	
	public static void main(String[] args) {
		// process arguments
		if(args == null || args.length != 2){
			printUsage();
			return;
		}
		
		String inputDir = args[0];
		String outputDir = args[1];
		
		PDFConverter pdfConverter = new PDFConverter(); 
		try {
			pdfConverter.convert(inputDir, outputDir);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Completed.");
	}

}
