package tgc.authorblurb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;

import tgc.framework.Generator;
import tgc.framework.OutputType;
import tgc.framework.OutputTypeNotSupportedException;
import tgc.framework.StreamHelper;

public class AuthorBlurbGenerator implements Generator
{
	// constants
	final static String OUTPUT_ENCODING = "UTF-8";
	
	// Autowired fields, configure in Beans.xml
	@Autowired
	private AuthorBlurbModel authorBlurbModel;
	@Autowired
	private AuthorBlurbTextdata authorBlurbTextdata;
	
	// Other fields with default values
	private File outputFile;
	private OutputType outputType = OutputType.STRING;
	private HashMap<String, Locale> supportedLocaleCache = new HashMap<String, Locale>();
	
	@Override
	public String generateText(final Locale locale)
	{
		String template = authorBlurbModel.getModel(locale);
		StringBuffer resultText = new StringBuffer();
		replaceVars(resultText, template);
		
		switch(outputType)
		{
			case TXTFILE:
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
						writer.write(resultText.toString());
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
				break;
			default:
				break;
		}
		
		return resultText.toString();
	}
	
	@Override
	public Locale[] getSupportedLocales()
	{
		Set<Locale> supportedLocalesOfModel = new HashSet<Locale>(Arrays.asList(authorBlurbModel.getSupportedLocales()));
		Set<Locale> supportedLocalesOfTextdata = new HashSet<Locale>(Arrays.asList(authorBlurbTextdata.getSupportedLocales()));
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
		if(isSupportedOutputType(outputType))
		{
			this.outputType = outputType;
		}
		else
		{
			throw new OutputTypeNotSupportedException("Output type " + outputType.toString() + " not supported in AutoBlurbGenerator");
		}
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
	
	private void replaceVars(StringBuffer resultBuffer, String unprocessedText)
	{
		Random random = new Random();
		if(unprocessedText.contains("{$"))
		{
			// in the template, variables are marked with {$ ... }
			int varStart = unprocessedText.indexOf("{$");
			int varEnd = unprocessedText.indexOf("}");
			
			// append literal text from the template to the result text
			resultBuffer.append(unprocessedText.substring(0, varStart));
			String variable = unprocessedText.substring(varStart + 2, varEnd);
			
			// take the variable value from the configuration file
			String[] possibleVariableReplacements = authorBlurbTextdata.getPossibleWordsForKey(AuthorBlurbPropertyName.valueOf(variable.toUpperCase()));
			if(possibleVariableReplacements == null)
			{
				System.out.println("No variable values found for template variable {$" + variable + "}");
			}
			String replacement = possibleVariableReplacements[random.nextInt(possibleVariableReplacements.length)];
			resultBuffer.append(replacement);	
			replaceVars(resultBuffer, unprocessedText.substring(varEnd + 1));
		}
		else
		{
			resultBuffer.append(unprocessedText);
		}
	}
}