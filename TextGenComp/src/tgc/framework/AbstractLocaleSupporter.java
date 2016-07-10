package tgc.framework;

import java.util.HashMap;
import java.util.Locale;

public abstract class AbstractLocaleSupporter implements LocaleSupporter
{
	protected HashMap<String, Locale> supportedLocaleCache;

	public AbstractLocaleSupporter()
	{
		supportedLocaleCache = new HashMap<String, Locale>();
	}
	
	@Override
	public Locale[] getSupportedLocales()
	{
		return Locale.getAvailableLocales();
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
