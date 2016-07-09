package tgc.authorblurb;

import java.util.HashMap;
import java.util.Locale;

import tgc.framework.LocaleSupporter;

public class AuthorBlurbModel implements LocaleSupporter
{
	private HashMap<String, Locale> supportedLocaleCache;
	
	public AuthorBlurbModel()
	{
		supportedLocaleCache = new HashMap<String, Locale>();
	}
	
	public Locale[] getSupportedLocales()
	{
		// this model theoretically supports all Locales that the JVM provides
		return new Locale[]{ new Locale("de-DE") };
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
	
	public String getModel(Locale locale)
	{
		return "{$authorname} wurde {$birthyear} in {$birthplace} geboren. Mit {$bookgenre} \u00FCber {$topic} eroberte sie oder er eine {$fantype} {$community} und wurde {$when} mit \u201E{$booktitle}\u201C f\u00FCr {$prize} nominiert. Die Autorin oder der Autor lebt {$residence}{$endmarker}";
	}
}