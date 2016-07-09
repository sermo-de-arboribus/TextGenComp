package tgc.authorblurb;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;

import tgc.framework.Generator;

public class AuthorBlurbGenerator implements Generator
{
	@Autowired
	private AuthorBlurbModel authorBlurbModel;
	@Autowired
	private AuthorBlurbTextdata authorBlurbTextdata;
	private HashMap<String, Locale> supportedLocaleCache = new HashMap<String, Locale>();
	
	public String generateText(Locale locale)
	{
		String template = authorBlurbModel.getModel(locale);
		StringBuffer resultText = new StringBuffer();
		replaceVars(resultText, template);
		
		return resultText.toString();
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
}