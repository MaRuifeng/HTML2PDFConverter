# HTML2PDFConverter

Description
-----------

This is a Java based SWT program that provides a nice and simple UI to convert HTML files to PDFs. 

Firstly it will try to tidy up the HTML files into XHTML files using [JTidy](http://jtidy.sourceforge.net/), and then use the [Flying Saucer](http://flyingsaucerproject.github.io/flyingsaucer/r8/guide/users-guide-R8.html#xil_37) to convert the HTML files into PDF.
[JSoup](https://jsoup.org/) is used to parse the data of large HTML tables to CSV files, but that usage is temporarily commented out. 
The Flying Saucer uses a free version of [iText](http://itextpdf.com/) to do the conversion. The details can be found in their documentaion. 

Note that there is CSS injection introduced for a special purpose. You may need to modify that part accordingly. 

Owner
----
Ruifeng Ma (mrfflyer@gmail.com)

Requirements
------------
* Theoricially Java 1.6 and above. The original program was developed using [Oracle JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).
* WindowBuilder Pro for SWT can be installed from `http://download.eclipse.org/windowbuilder/WB/release/R201506241200-1/4.4/` in the eclipse IDE. Just make sure the version is correct. 


Build
-----
* Export as a runnable jar
* If needed, use [launch4j](http://launch4j.sourceforge.net/index.html) to make it into a nice Windows executable

Contributing
------------
Contact the owner before contributing. 

Copyright
---------
Copyright (C) 2017 Ruifeng Ma. All rights reserved. 
