package tgc.authorblurb;

import java.util.HashMap;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

public class AuthorBlurbTextdata implements IAuthorBlurbTextdata
{
	@Autowired
	private AuthorBlurbProperties configurationProperties;
	protected HashMap<String, Locale> supportedLocaleCache;
	
	public AuthorBlurbTextdata()
	{
		supportedLocaleCache = new HashMap<String, Locale>();
	}

	@Override
	public String[] getPossibleWordsForKey(AuthorBlurbPropertyName key)
	{
		return configurationProperties.getProperty(key);
	}

	@Override
	public Locale[] getSupportedLocales()
	{
		return new Locale[]{ new Locale("de", "DE"), new Locale("de", "AT"), new Locale("de", "CH"), new Locale("de") };
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