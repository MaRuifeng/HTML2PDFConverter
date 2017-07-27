package ui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import main.PDFConverter;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.custom.StyledText;

import com.lowagie.text.DocumentException;

public class ConverterUI {
	
    protected Shell converterShell;
    private Text sourceFolderText;
    private Text targetFolderText;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ConverterUI window = new ConverterUI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		converterShell.open();
		converterShell.layout();
		while (!converterShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		converterShell = new Shell();
		converterShell.setSize(673, 460);
		converterShell.setText("HTML-to-PDF Converter");
		converterShell.setImage(SWTResourceManager.getImage(ConverterUI.class, "/ui/image/converter.ico"));
		
		CTabFolder tabFolder = new CTabFolder(converterShell, SWT.BORDER);
		tabFolder.setBounds(0, 0, 655, 415);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		CTabItem tbtmConvert = new CTabItem(tabFolder, SWT.NONE);
		tbtmConvert.setText("Convert");
		
		Composite convertComposite = new Composite(tabFolder, SWT.NONE);
		convertComposite.setForeground(SWTResourceManager.getColor(70, 130, 180));
		convertComposite.setFont(SWTResourceManager.getFont("Segoe UI", 20, SWT.NORMAL));
		convertComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		tbtmConvert.setControl(convertComposite);
		
		Label lblServiceTitle = new Label(convertComposite, SWT.NONE);
		lblServiceTitle.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		lblServiceTitle.setForeground(SWTResourceManager.getColor(65, 105, 225));
		lblServiceTitle.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblServiceTitle.setBounds(47, 55, 433, 31);
		lblServiceTitle.setText("Convert HTML Files to PDF Reports ");
		
		sourceFolderText = new Text(convertComposite, SWT.BORDER);
		sourceFolderText.setBounds(47, 145, 410, 26);
		
		Label lblsourceFileFolder = new Label(convertComposite, SWT.NONE);
		lblsourceFileFolder.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblsourceFileFolder.setBounds(47, 119, 286, 20);
		lblsourceFileFolder.setText("HTML files in this folder will be converted:");
		
		Label lblTargetFileFolder = new Label(convertComposite, SWT.NONE);
		lblTargetFileFolder.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblTargetFileFolder.setText("PDF reports will be saved to this folder:");
		lblTargetFileFolder.setBounds(47, 207, 286, 20);
		
		targetFolderText = new Text(convertComposite, SWT.BORDER);
		targetFolderText.setBounds(47, 233, 410, 26);
		
		Button btnSourceDirBrowse = new Button(convertComposite, SWT.NONE);
		btnSourceDirBrowse.setBounds(489, 141, 90, 30);
		btnSourceDirBrowse.setText("Browse");
		btnSourceDirBrowse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(converterShell, SWT.NULL); 
				String path = dialog.open();
				if (path != null) sourceFolderText.setText(path);
				System.out.println(sourceFolderText.getText());
			}
		});
		
		Button btnTargetDirBrowse = new Button(convertComposite, SWT.NONE);
		btnTargetDirBrowse.setText("Browse");
		btnTargetDirBrowse.setBounds(489, 229, 90, 30);
		btnTargetDirBrowse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(converterShell, SWT.NULL); 
				String path = dialog.open();
				if (path != null) targetFolderText.setText(path);
			}
		});
		
		final Button btnConvert = new Button(convertComposite, SWT.NONE);
		btnConvert.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		btnConvert.setBounds(47, 306, 120, 40);
		btnConvert.setText("Convert");
		
		final Label labelProgress = new Label(convertComposite, SWT.NONE);
		labelProgress.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		labelProgress.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		labelProgress.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.ITALIC));
		labelProgress.setBounds(187, 306, 249, 20);
		
		btnConvert.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (sourceFolderText.getText() == "" || targetFolderText.getText() == "") {
					showWarning("Please make sure source and destination folders are selected.");
				}
				else {
					btnConvert.setEnabled(false);
					labelProgress.setText("Conversion in progress...");
					PDFConverter converter = new PDFConverter(); 
					try {
						converter.convert(sourceFolderText.getText(), targetFolderText.getText());
					} catch (IOException excep) {
						showWarning("Please ensure that the source folder contains valid HTML file(s). Contact the author if the problem persists." 
								+ "\n\nException Message: \n" 
								+ excep.getMessage());
					} catch (DocumentException excep) {
						showError("The program encountered an error when generating PDF files. Please contact the author." 
								+ "\n\nException Message: \n" 
								+ excep.getMessage());
					} catch (Exception excep) {
						showError("The program encountered an error. Please contact the author." 
								+ "\n\nException Message: \n" 
								+ excep.getMessage());
					} finally {
						btnConvert.setEnabled(true);
						labelProgress.setText("Completed.");
					}
					
					try {
						Desktop.getDesktop().open(new File(PDFConverter.outputFolder));
					} catch (IOException e1) {
						showWarning("The conversion is completed, but the program is unable to open the destination folder.");
					}
				}
			}
			
			public void showWarning(String message) {
				MessageBox msgBox = new MessageBox(converterShell, SWT.ICON_WARNING | SWT.OK);
				msgBox.setText("Warning"); 
				msgBox.setMessage(message);
				int btnId = msgBox.open();
				switch(btnId) {
				case SWT.OK:
					// do something
				}
			}
			public void showError(String message) {
				MessageBox msgBox = new MessageBox(converterShell, SWT.ICON_ERROR | SWT.OK);
				msgBox.setText("Error"); 
				msgBox.setMessage(message);
				int btnId = msgBox.open();
				switch(btnId) {
				case SWT.OK:
					// do something
				}
			}
		});
		
		CTabItem tbtmHelp = new CTabItem(tabFolder, SWT.NONE);
		tbtmHelp.setText("Help");
		
		Composite helpComposite = new Composite(tabFolder, SWT.NONE);
		tbtmHelp.setControl(helpComposite);
		
		StyledText styledText = new StyledText(helpComposite, SWT.BORDER | SWT.WRAP);
		styledText.setText("This program is designed to convert HTML files from AmiBroker backtests into PDF reports.\n" 
				+ "Version: 1.0\nAuthor: Ruifeng Ma\n\n" 
				+ "Oracle Java SE Development Kit (version 8 preferred) is expected to run this program. If it's not installed, the user will be prompted for installation.\n" 
				+ "Happy coding!");
		styledText.setBounds(10, 10, 629, 365);

	}
}
