package tgc.epubgenerator;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;

import nu.xom.*;
import tgc.framework.Generator;
import tgc.novelfromquotes.NovelFromQuotesModel;

public class EpubModel
{
	@Autowired
	private EpubTextData epubTextData;
	
	// prepare a byte array with the ASCII codes for the String "application/epub+zip" without EOL/EOF marker
	private static final byte[] mimetypeASCII = new byte[]{0x61, 0x70, 0x70, 0x6C, 0x69, 0x63, 0x61, 0x74, 0x69, 0x6F, 0x6E, 0x2F, 0x65, 0x70, 0x75, 0x62, 0x2B, 0x7A, 0x69, 0x70};
	
	private final Map<Locale, String> chapterWords = new HashMap<Locale, String>();
	
	private String title;
	private String author;
	private String textBody;
	
	EpubModel(final String title, final String author, final String textBody)
	{
		this.title = title;
		this.author = author;
		this.textBody = textBody;
		initializeChapterWords();
	}

	public File generateEpub(final Locale locale)
	{
		File destFolder = new File(System.getProperty("java.io.tempdir"));
		File tempDir = new File(destFolder.getPath() + "/tgc_epubgen");

		// generate cover file
		JpegFile cover = new JpegFile(title, author);
		File imageFileDir = new File(tempDir.getPath() + "/OEBPS/images/");
		File imageFilePath = cover.generate(imageFileDir);
		
		String tocCoverPath = imageFilePath.toURI().toString();
		int index = tocCoverPath.indexOf("images/");
		tocCoverPath = tocCoverPath.substring(index);
		
		// generate cover HTML
		String coverTemplate = epubTextData.getCoverHtml(tocCoverPath);
		File HTMLCoverPagePath = new File(tempDir.getPath() + "/OEBPS/text/Cover.xhtml");
		saveHtmlFile(HTMLCoverPagePath, coverTemplate);
		
		// generate title page file
		String template = epubTextData.getTitlePageHtml(locale, title, author);
		File HTMLTitlePagePath = new File(tempDir.getPath() + "OEBPS/text/Title.xhtml");
		saveHtmlFile(HTMLTitlePagePath, template);
		
		// generate HTML chapter files
		List<File> chapterFiles = new ArrayList<File>();
		String[] chapterTexts = textBody.split(getChapterSeparatorRegex(locale));
		
		for(int i = 0; i < chapterTexts.length; i++)
		{
			String chapterTemplate = epubTextData.getChapterHtml(i + 1, chapterWords.get(locale), chapterTexts[i]);
			File HTMLChapterFile = new java.io.File(tempDir.getPath(), "/OEBPS/text/Chapter" + i + ".xhtml");
			chapterFiles.add(HTMLChapterFile);
			saveHtmlFile(HTMLChapterFile, chapterTemplate);
		}
		
		// generate NCX file
		DocType ncxDtd = new DocType("ncx", "-//NISO//DTD ncx 2005-1//EN", "http://www.daisy.org/z3986/2005/ncx-2005-1.dtd");
		Element ncxRoot = new Element("ncx", "http://www.daisy.org/z3986/2005/ncx/");
		Document ncxFile = new Document(ncxRoot);
		ncxFile.setDocType(ncxDtd);
		
		ncxRoot.addAttribute(new Attribute("version", "2005-1"));
		
		Element ncxHead = new Element("head", "http://www.daisy.org/z3986/2005/ncx/");
		Element meta1 = new Element("meta", "http://www.daisy.org/z3986/2005/ncx/");
		Element meta2 = new Element("meta", "http://www.daisy.org/z3986/2005/ncx/");
		Element meta3 = new Element("meta", "http://www.daisy.org/z3986/2005/ncx/");
		Element meta4 = new Element("meta", "http://www.daisy.org/z3986/2005/ncx/");
		
		meta1.addAttribute(new Attribute("name", "dtb:uid"));
		UUID guid = UUID.randomUUID();
		meta1.addAttribute(new Attribute("content", "urn:guid:" + guid.toString()));
		meta2.addAttribute(new Attribute("name", "dtb:depth"));
		meta2.addAttribute(new Attribute("content", "0"));
		meta3.addAttribute(new Attribute("name", "dtb:totalPageCount"));
		meta3.addAttribute(new Attribute("content", "0"));
		meta4.addAttribute(new Attribute("name", "dtb:maxPageNumber"));
		meta4.addAttribute(new Attribute("content", "0"));
		
		ncxHead.appendChild(meta1);
		ncxHead.appendChild(meta2);
		ncxHead.appendChild(meta3);
		ncxHead.appendChild(meta4);
		
		ncxRoot.appendChild(ncxHead);
		
		Element ncxTitle = new Element("docTitle", "http://www.daisy.org/z3986/2005/ncx/");
		Element titleText = new Element("text", "http://www.daisy.org/z3986/2005/ncx/");
		titleText.appendChild(new Text(title));
		ncxTitle.appendChild(titleText);
		ncxRoot.appendChild(ncxTitle);
		
		Element navMap = new Element("navMap", "http://www.daisy.org/z3986/2005/ncx/");
		Element navPoint = new Element("navPoint", "http://www.daisy.org/z3986/2005/ncx/");
		navPoint.addAttribute(new Attribute("id", "navPoint-1"));
		navPoint.addAttribute(new Attribute("playOrder", "1"));
		navMap.appendChild(navPoint);
		Element navLabel = new Element("navLabel", "http://www.daisy.org/z3986/2005/ncx/");
		Element labelText = new Element("text", "http://www.daisy.org/z3986/2005/ncx/");
		labelText.appendChild(new Text("Startseite"));
		navLabel.appendChild(labelText);
		navPoint.appendChild(navLabel);
		Element content = new Element("content", "http://www.daisy.org/z3986/2005/ncx/");
		content.addAttribute(new Attribute("src", "text/Title.xhtml"));
		navPoint.appendChild(content);
		
		// add book chapters
		Iterator<File> iterator = chapterFiles.iterator();
		int counter = 2;
		while(iterator.hasNext())
		{
			File nextFile = iterator.next();
			Element chapterNavPoint = new Element("navPoint", "http://www.daisy.org/z3986/2005/ncx/");
			chapterNavPoint.addAttribute(new Attribute("id", "navPoint-" + counter));
			chapterNavPoint.addAttribute(new Attribute("playOrder", "" + counter));
			
			Element chapterLabel = new Element("navLabel", "http://www.daisy.org/z3986/2005/ncx/");
			Element chapterText = new Element("text", "http://www.daisy.org/z3986/2005/ncx/");
			chapterText.appendChild(new Text("Kapitel " + (counter - 1)));
			chapterLabel.appendChild(chapterText);
			chapterNavPoint.appendChild(chapterLabel);
			Element chapterContent = new Element("content", "http://www.daisy.org/z3986/2005/ncx/");
			chapterContent.addAttribute(new Attribute("src", "text/" + nextFile.getPath()));
			chapterNavPoint.appendChild(chapterContent);
			navMap.appendChild(chapterNavPoint);
			
			counter++;
		}
		
		ncxRoot.appendChild(navMap);
		
		File ncxFilePath = new File(tempDir.getPath() + "/OEBPS/toc.ncx");
		try
		{
			FileOutputStream fos = new FileOutputStream(ncxFilePath);
			Serializer ser = new Serializer(fos);
			ser.setIndent(4);
			ser.write(ncxFile);
			ser.flush();
			fos.close();
		}
		catch (IOException exc)
		{
			System.err.println("Could not save NCX file when generating an Epub for " + title + ". " + exc.getMessage());
		}
		
		// generate OPF file
		Element opfRoot = new Element("package", "http://www.idpf.org/2007/opf");
		opfRoot.addAttribute(new Attribute("unique-identifier", "BookId"));
		opfRoot.addAttribute(new Attribute("version", "2.0"));
		
		Element metadata = new Element("metadata", "http://www.idpf.org/2007/opf");
		metadata.addNamespaceDeclaration("dc", "http://purl.org/dc/elements/1.1/");
		metadata.addNamespaceDeclaration("opf", "http://www.idpf.org/2007/opf");
		
		Element identifier = new Element("dc:identifier", "http://purl.org/dc/elements/1.1/");
		identifier.addAttribute(new Attribute("id", "BookId"));
		identifier.addAttribute(new Attribute("opf:scheme", "http://www.idpf.org/2007/opf", "UUID"));
		identifier.appendChild(new Text("urn:guid:" + guid.toString()));
		metadata.appendChild(identifier);
		
		Element dcTitle = new Element("dc:title", "http://purl.org/dc/elements/1.1/");
		dcTitle.appendChild(new Text(title));
		metadata.appendChild(dcTitle);
		
		Element dcCreator = new Element("dc:creator", "http://purl.org/dc/elements/1.1/");
		dcCreator.addAttribute(new Attribute("opf:role", "http://www.idpf.org/2007/opf", "aut"));
		dcCreator.appendChild(new Text(author));
		metadata.appendChild(dcCreator);
		
		Element dcLanguage = new Element("dc:language", "http://purl.org/dc/elements/1.1/");
		dcLanguage.appendChild(new Text(locale.getLanguage()));
		metadata.appendChild(dcLanguage);
		
		Element dcDate = new Element("dc:date", "http://purl.org/dc/elements/1.1/");
		dcDate.addAttribute(new Attribute("opf:event", "http://www.idpf.org/2007/opf", "modification"));
		dcDate.appendChild(new Text(new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
		metadata.appendChild(dcDate);
		
		Element isbn = new Element("dc:identifier", "http://purl.org/dc/elements/1.1/");
		isbn.addAttribute(new Attribute("opf:scheme", "http://www.idpf.org/2007/opf", "GUID"));
		isbn.appendChild(new Text(guid.toString()));
		metadata.appendChild(isbn);
		
		Element dcPublisher = new Element("dc:publisher", "http://purl.org/dc/elements/1.1/");
		dcPublisher.appendChild(new Text("Komponentenverlag"));
		metadata.appendChild(dcPublisher);
		
		Element metaCover = new Element("meta", "http://www.idpf.org/2007/opf");
		metaCover.addAttribute(new Attribute("name", "cover"));
		metaCover.addAttribute(new Attribute("content", "coverjpg"));
		metadata.appendChild(metaCover);
		
		opfRoot.appendChild(metadata);
		
		Element manifest = new Element("manifest", "http://www.idpf.org/2007/opf");
		Element item1 = new Element("item", "http://www.idpf.org/2007/opf");
		Element item2 = new Element("item", "http://www.idpf.org/2007/opf");
		Element item3 = new Element("item", "http://www.idpf.org/2007/opf");
		Element item4 = new Element("item", "http://www.idpf.org/2007/opf");
		
		item1.addAttribute(new Attribute("href", "toc.ncx"));
		item1.addAttribute(new Attribute("id", "ncx"));
		item1.addAttribute(new Attribute("media-type", "application/x-dtbncx+xml"));
		manifest.appendChild(item1);
		
		item2.addAttribute(new Attribute("href", "text/Title.xhtml"));
		item2.addAttribute(new Attribute("id", "Title"));
		item2.addAttribute(new Attribute("media-type", "application/xhtml+xml"));
		manifest.appendChild(item2);
		
		// list cover HTML
		item4.addAttribute(new Attribute("href", "text/Cover.xhtml"));
		item4.addAttribute(new Attribute("id", "Cover"));
		item4.addAttribute(new Attribute("media-type", "application/xhtml+xml"));
		manifest.appendChild(item4);
		
		// list cover JPG
		item3.addAttribute(new Attribute("href", tocCoverPath));
		item3.addAttribute(new Attribute("id", "coverjpg"));
		item3.addAttribute(new Attribute("media-type", "image/jpeg"));
		manifest.appendChild(item3);
		
		// add chapters
		Iterator<java.io.File> opfChapterItems = chapterFiles.iterator();
		while(opfChapterItems.hasNext())
		{
			File nextFile = opfChapterItems.next();
			Element nextItem = new Element("item", "http://www.idpf.org/2007/opf");
			nextItem.addAttribute(new Attribute("href", "text/" + getFileName(nextFile.getPath())));
			nextItem.addAttribute(new Attribute("id", getBaseName(nextFile.getPath())));
			nextItem.addAttribute(new Attribute("media-type", "application/xhtml+xml"));
			manifest.appendChild(nextItem);
		}
		
		opfRoot.appendChild(manifest);
		
		Element spine = new Element("spine", "http://www.idpf.org/2007/opf");
		spine.addAttribute(new Attribute("toc", "ncx"));
		
		Element coverref = new Element("itemref", "http://www.idpf.org/2007/opf");
		coverref.addAttribute(new Attribute("idref", "Cover"));
		spine.appendChild(coverref);
		
		Element itemref = new Element("itemref", "http://www.idpf.org/2007/opf");
		itemref.addAttribute(new Attribute("idref", "Title"));
		spine.appendChild(itemref);
		
		// add chapters
		Iterator<java.io.File> opfChapterSpineRefs = chapterFiles.iterator();
		while(opfChapterSpineRefs.hasNext())
		{
			java.io.File nextFile = opfChapterSpineRefs.next();
			Element nextItem = new Element("itemref", "http://www.idpf.org/2007/opf");
			nextItem.addAttribute(new Attribute("idref", getBaseName(nextFile.getPath())));
			spine.appendChild(nextItem);
		}
		
		opfRoot.appendChild(spine);
		
		Element guide = new Element("guide", "http://www.idpf.org/2007/opf");
		Element guideRef = new Element("reference", "http://www.idpf.org/2007/opf");
		guideRef.addAttribute(new Attribute("type", "cover"));
		guideRef.addAttribute(new Attribute("title", "Cover"));
		guideRef.addAttribute(new Attribute("href", "text/Cover.xhtml"));
		guide.appendChild(guideRef);
		
		opfRoot.appendChild(guide);
		
		Document opfDoc = new Document(opfRoot);
		
		File opfFilePath = new File(tempDir.getPath(), "/OEBPS/content.opf");
		try
		{
			FileOutputStream fos = new FileOutputStream(opfFilePath);
			Serializer ser = new Serializer(fos);
			ser.setIndent(4);
			ser.write(opfDoc);
			ser.flush();
			fos.close();
		}
		catch (IOException exc)
		{
			System.err.println("Could not save OPF file when generating an Epub for " + title + ". " + exc.getMessage());
		}
		
		// create container.xml file
		Element container = new Element("container", "urn:oasis:names:tc:opendocument:xmlns:container");
		container.addAttribute(new Attribute("version", "1.0"));
		
		Element rootfiles = new Element("rootfiles", "urn:oasis:names:tc:opendocument:xmlns:container");
		Element rootfile = new Element("rootfile", "urn:oasis:names:tc:opendocument:xmlns:container");
		rootfile.addAttribute(new Attribute("full-path", "OEBPS/content.opf"));
		rootfile.addAttribute(new Attribute("media-type", "application/oebps-package+xml"));
		rootfiles.appendChild(rootfile);
		container.appendChild(rootfiles);
		
		Document containerDoc = new Document(container);
		File containerFilePath = new File(tempDir.getPath() + "/META-INF/container.xml");
		containerFilePath.getParentFile().mkdirs();
		try
		{
			FileOutputStream fos = new FileOutputStream(containerFilePath);
			Serializer ser = new Serializer(fos);
			ser.setIndent(4);
			ser.write(containerDoc);
			ser.flush();
			fos.close();
		}
		catch (IOException exc)
		{
			System.err.println("Could not save container file when generating an Epub for " + title + ". " + exc.getMessage());
		}
		
		// zip epub in a way that is conformant with the EPUB spec
		String destPath = destFolder.getPath() + "/out.epub";
		
		zipEpub(destPath, tempDir.getPath());
		
		// delete temp folder
		try
		{
			tempDir.delete();			
		}
		catch (Exception exc)
		{
			System.err.println("Could not delete temporary directory " + tempDir.getPath() + " for " + title);
		}
		
		return new java.io.File(destPath);
	}
	
	private String getChapterSeparatorRegex(Locale locale)
	{ 
		// use lookbehind regex (?=SPLITREGEX) so we do not lose the delimiters in the output text
		// return "(?=" + chapterWords.get(locale) + "\\s*[0-9]+)";
		return chapterWords.get(locale) + "\\s*[0-9]+";
	}
	
	// Helper method to put all temporary files into a zip container as required by the EPUB specification
	private void zipEpub(String zipFileName, String dir)
	{
		try
		{
		    java.io.File dirObj = new java.io.File(dir);
		    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
		    
		    // write uncompressed mimetype file
			ZipEntry zipEntry = new ZipEntry("mimetype");
			zipEntry.setMethod(ZipOutputStream.STORED);
			zipEntry.setSize(20);
			zipEntry.setCompressedSize(20);
			zipEntry.setCrc(0x2CAB616F);
			out.putNextEntry(zipEntry);
			out.write(mimetypeASCII, 0, mimetypeASCII.length);
			out.closeEntry();
			
			// add all the remaining files
			addDir("", dirObj, out);
		    out.close();
		}
		catch(IOException exc)
		{
			System.err.println("Error zipping Epub file " + zipFileName + ". " + exc.getMessage());
		}
	}

	// Helper method to add all files in a given directory to a zip container. The method works recursively
	// through all child directories.
	private void addDir(String basePath, java.io.File dirObj, ZipOutputStream out) throws IOException
	{
		java.io.File[] files = dirObj.listFiles();
	    byte[] tmpBuf = new byte[1024];

	    for (int i = 0; i < files.length; i++)
	    {
	      if (files[i].isDirectory())
	      {
	    	String path = basePath + files[i].getName() + "/";
	    	out.putNextEntry(new ZipEntry(path));
	        addDir(path, files[i], out);
	        out.closeEntry();
	        continue;
	      }
	      FileInputStream in = new FileInputStream(files[i].getAbsolutePath());
	      out.putNextEntry(new ZipEntry(basePath + files[i].getName()));
	      int len;
	      while ((len = in.read(tmpBuf)) > 0)
	      {
	        out.write(tmpBuf, 0, len);
	      }
	      out.closeEntry();
	      in.close();
	    }
	}

	// Helper method to write out HTML pages (passed in as a String) to a file
	private void saveHtmlFile(java.io.File HTMLPagePath, String HTMLPage)
	{
		HTMLPagePath.getParentFile().mkdirs();
		try
		{
			PrintWriter out = new PrintWriter(HTMLPagePath, "UTF-8");
			out.print(HTMLPage);
			out.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}
	
	private void initializeChapterWords()
	{
		chapterWords.put(new Locale("cs", "CZ"), "Kapitola");
		chapterWords.put(new Locale("de"), "Kapitel");
		chapterWords.put(new Locale("de", "AT"), "Kapitel");
		chapterWords.put(new Locale("de", "CH"), "Kapitel");
		chapterWords.put(new Locale("de", "CZ"), "Kapitel");
		chapterWords.put(new Locale("de", "DE"), "Kapitel");
		chapterWords.put(new Locale("de", "LU"), "Kapitel");
		chapterWords.put(new Locale("en"), "Chapter");
		chapterWords.put(new Locale("en", "AT"), "Chapter");
		chapterWords.put(new Locale("en", "AU"), "Chapter");
		chapterWords.put(new Locale("en", "CA"), "Chapter");
		chapterWords.put(new Locale("en", "CN"), "Chapter");
		chapterWords.put(new Locale("en", "DE"), "Chapter");
		chapterWords.put(new Locale("en", "GB"), "Chapter");
		chapterWords.put(new Locale("en", "NZ"), "Chapter");
		chapterWords.put(new Locale("en", "US"), "Chapter");
		chapterWords.put(new Locale("en", "ZA"), "Chapter");
		chapterWords.put(new Locale("es"), "Capítulo");
		chapterWords.put(new Locale("fr"), "Chapitre");
		chapterWords.put(new Locale("fr", "FR"), "Chapitre");
		chapterWords.put(new Locale("grc"), "κεφάλαιο");
		chapterWords.put(new Locale("it"), "Chapitre");
		chapterWords.put(new Locale("it", "IT"), "Capitolo");
		chapterWords.put(new Locale("la"), "Caput");
		chapterWords.put(new Locale("pl"), "Rozdział");
		chapterWords.put(new Locale("pl", "PL"), "Rozdział");
		chapterWords.put(new Locale("ru"), "глава");
		chapterWords.put(new Locale("ru", "RU"), "глава");
		chapterWords.put(new Locale("se"), "Kapitel");
		chapterWords.put(new Locale("se", "SE"), "Kapitel");
		chapterWords.put(new Locale("zh"), "卷");
		chapterWords.put(new Locale("zh", "CN"), "卷");
 	}
	
	// inspired by Apache Commons IO
	private String getFileName(final String fullPath)
	{
		if(fullPath == null)
		{
			return null;
		}
		final int index = indexOfLastSeparator(fullPath);
		return fullPath.substring(index + 1);
	}
	
	// inspired by Apache Commons IO
	private String getBaseName(final String fullPath)
	{
		return removeExtension(getFileName(fullPath));
	}

	// inspired by Apache Commons IO
	private String removeExtension(final String filename)
	{
		if(filename == null)
		{
			return null;
		}
		
		final int index = filename.lastIndexOf('.');
		if(index == -1)
		{
			return filename;
		}
		else
		{
			return filename.substring(0, index);
		}
	}
	
	//inspired by Apache Commons IO
	private int indexOfLastSeparator(final String filename)
	{
		if(filename == null)
		{
			return -1;
		}
		final int lastUnixPos = filename.lastIndexOf('/');
		final int lastWindowsPos = filename.lastIndexOf('\\');
		return Math.max(lastUnixPos, lastWindowsPos);
	}
}