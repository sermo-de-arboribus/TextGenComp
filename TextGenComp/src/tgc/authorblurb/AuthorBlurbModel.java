package tgc.authorblurb;

import java.util.HashMap;
import java.util.Locale;

public class AuthorBlurbModel implements IAuthorBlurbModel
{	
	protected HashMap<String, Locale> supportedLocaleCache;
	
	public AuthorBlurbModel()
	{
		supportedLocaleCache = new HashMap<String, Locale>();
	}
	
	@Override
	public Locale[] getSupportedLocales()
	{
		return new Locale[]{ new Locale("de"), new Locale("de", "DE"), new Locale("de", "AT"), new Locale("de", "CH") };
	}
		
	@Override
	public String getModel(final Locale locale)
	{
		return "{$authorname} wurde {$birthyear} in {$birthplace} geboren. Mit {$bookgenre} \u00FCber {$topic} eroberte sie oder er eine {$fantype} {$community} und wurde {$when} mit \u201E{$booktitle}\u201C f\u00FCr {$prize} nominiert. Die Autorin oder der Autor lebt {$residence}{$endmarker}";
	}

	@Override
	public boolean isLocaleSupported(final Locale locale)
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