package worker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;


/**
 * Convert XHTML files to PDF files
 * @author ruifengm
 * @since 2017-Jul-26
 */

public class XHTMLToPDFConverter {
	
	public static ArrayList<String> outputFileList = new ArrayList<String>();
	
	public String convertToPDF(String inputFilePath, String outputFileDir) throws IOException, DocumentException {
		File inputFile = new File(inputFilePath);
		String outputFileName = inputFile.getName().replace(".xhtml", ".pdf");
		String outputFilePath = outputFileDir + "/" + outputFileName;
		// Convert to PDF
		System.out.println("Converting " + inputFile.getName() + " to PDF ...");
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocument(new File(inputFilePath));
		renderer.layout();
		FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath);
		renderer.createPDF(fileOutputStream);
		fileOutputStream.close();
		return outputFilePath;
	}
	
	public String convertHtmlTableToCSV(String inputFilePath, String outputFileDir) throws FileNotFoundException, IOException {
		File inputFile = new File(inputFilePath);
		String outputFileName = inputFile.getName().replace(".xhtml", ".csv");
		String outputFilePath = outputFileDir + "/" + outputFileName;
		
		// Convert to CSV
		System.out.println("Converting " + inputFile.getName() + " to CSV ...");
		Document doc = Jsoup.parse(new FileInputStream(inputFilePath) , "UTF-8", "");
		
		String csvString = "";
		String headerString = "";
		
		Element table = doc.getElementsByTag("table").get(1);
		Elements rows = table.select("tr"); 
		Elements ths = rows.select("th");

		for (Element th: ths) {
			headerString  += th.text() + ","; 
		}
		csvString = csvString + headerString.substring(0, headerString.lastIndexOf(",")) + "\n"; 
		
		for (Element row: rows) {
			String rowString = ""; 
			for (Element td: row.select("td")) {
				rowString += td.text() + ","; 
			}
			if (rowString != "") csvString = csvString + rowString.substring(0, rowString.lastIndexOf(",")) + "\n"; // Omit empty row
		}
		
		IOUtils.write(csvString, new FileOutputStream(outputFilePath), "UTF-8");
		return outputFilePath;
	}
}
