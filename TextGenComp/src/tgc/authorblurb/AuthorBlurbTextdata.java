package tgc.authorblurb;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import tgc.framework.LocaleSupporter;

public class AuthorBlurbTextdata implements LocaleSupporter
{
	private AuthorBlurbProperties configurationProperties;
	private HashMap<String, Locale> supportedLocaleCache;
	
	public AuthorBlurbTextdata()
	{
		supportedLocaleCache = new HashMap<String, Locale>();
		try
		{
			configurationProperties = AuthorBlurbProperties.getInstance();
		}
		catch(IOException exc)
		{
			System.out.println("The AuthorBlurb properties configuration file could not be loaded: " + AuthorBlurbProperties.getPropertyFilePath());
		}
	}
	
	public String[] getPossibleWordsForKey(AuthorBlurbPropertyName key)
	{
		return configurationProperties.getProperty(key);
	}

	public Locale[] getSupportedLocales()
	{
		return new Locale[]{ new Locale("de-DE"), new Locale("de-AT"), new Locale("de-CH"), new Locale("de") };
	}
	
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