package tgc.novelfromquotes;

import java.io.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;

import tgc.framework.Generator;
import tgc.framework.OutputType;
import tgc.framework.OutputTypeNotSupportedException;
import tgc.framework.StreamHelper;

public class NovelFromQuotesGenerator implements Generator
{
	// constants
	final static String OUTPUT_ENCODING = "UTF-8";

	// Autowired fields, configure in Beans.xml
	@Autowired
	private NovelFromQuotesModel novelFromQuotesModel;
	@Autowired
	private NovelFromQuotesTextdata novelFromQuotesTextdata;
	
	// Other fields with default values
	private File outputFile;
	private OutputType outputType = OutputType.STRING;
	private HashMap<String, Locale> supportedLocaleCache = new HashMap<String, Locale>();

	public String generateText()
	{
		return generateText(Locale.getDefault());
	}
	
	@Override
	public String generateText(final Locale locParam)
	{
		Random random = new Random();
		StringBuffer stringBuffer = new StringBuffer();
		
		for(int i = 1; i <= novelFromQuotesModel.getNumberOfChapters(); i++)
		{
			// In this method we disregard the Locale country settings and are only interested in 
			// the language part
			Locale locale = new Locale(locParam.getLanguage());

			// chapter title
			stringBuffer.append(novelFromQuotesModel.getLocalizedChapterWord(locale));
			stringBuffer.append(" " + i);
			stringBuffer.append(System.getProperty("line.separator")); // line break
			stringBuffer.append(System.getProperty("line.separator")); // line break

			for(int j = 0; j < novelFromQuotesModel.getQuotesPerChapter(); j++)
			{
				stringBuffer.append(novelFromQuotesTextdata.getLocalizedRandomSentence(locale).trim());
				stringBuffer.append(" ");
				// add random line breaks inside of a chapter.
				if(random.nextFloat() > 0.8)
				{
					stringBuffer.append(System.getProperty("line.separator"));
				}
			}
			
			stringBuffer.append(System.getProperty("line.separator")); // line break
			stringBuffer.append(System.getProperty("line.separator")); // line break
		}
		
		switch(outputType)
		{
			case TXTFILE:
				outputToTextFile(stringBuffer);
				break;
			default:
				break;
		}
		
		return stringBuffer.toString();
	}

	@Override
	public Locale[] getSupportedLocales()
	{
		Set<Locale> supportedLocalesOfModel = new HashSet<Locale>(Arrays.asList(novelFromQuotesModel.getSupportedLocales()));
		Set<Locale> supportedLocalesOfTextdata = new HashSet<Locale>(Arrays.asList(novelFromQuotesTextdata.getSupportedLocales()));
		supportedLocalesOfModel.retainAll(supportedLocalesOfTextdata);
		
		Locale[] locales = new Locale[supportedLocalesOfModel.size()];
		
		return supportedLocalesOfModel.toArray(locales);
	}

	@Override
	public boolean isLocaleSupported(Locale locale)
	{
		// on first look-up, fill the locale cache, so that further lookups can be 
		// served more efficiently
		if(supportedLocaleCache.isEmpty())
		{
			for(Locale loc : getSupportedLocales())
			{
				supportedLocaleCache.put(loc.toString(), loc);
			}
		}
		return supportedLocaleCache.containsKey(locale.toString()); 
	}

	public void setOutputFile(String filepath)
	{
		outputFile = new File(filepath);
	}
	
	@Override
	public void setOutputType(OutputType outputType) throws OutputTypeNotSupportedException
	{
		this.outputType = outputType;
	}

	@Override
	public boolean isSupportedOutputType(OutputType outputType)
	{
		if(outputType == OutputType.STRING || outputType == OutputType.TXTFILE)
		{
			return true;
		}
		else
		{
			return false;
		}
	}	
	
	private void outputToTextFile(StringBuffer stringBuffer)
	{
		if(outputFile == null)
		{
			System.err.println("Could not save generated text to file, output file is not defined.");
		}
		else
		{
			try
			{
				FileOutputStream out = new FileOutputStream(outputFile);
				Writer writer = new BufferedWriter(new OutputStreamWriter(out, OUTPUT_ENCODING));
				writer.write(stringBuffer.toString());
				StreamHelper.safeClose(writer);
			}
			catch (UnsupportedEncodingException exc)
			{
				System.err.println("Error: Tried to write the generated text to a file in " + OUTPUT_ENCODING + " encoding, but this encoding is not supported on this platform.");
			}
			catch (IOException exc)
			{
				System.err.println("Error when trying to write the generated text into the file " + outputFile.getAbsolutePath());
			}
		}
	}
}