package tgc.authorblurb;

import java.util.Locale;
import tgc.framework.AbstractLocaleSupporter;

public class AuthorBlurbModel extends AbstractLocaleSupporter
{	
	public AuthorBlurbModel()
	{
		super();
	}
	
	@Override
	public Locale[] getSupportedLocales()
	{
		return new Locale[]{ new Locale("de"), new Locale("de", "DE"), new Locale("de", "AT"), new Locale("de", "CH") };
	}
		
	public String getModel(Locale locale)
	{
		return "{$authorname} wurde {$birthyear} in {$birthplace} geboren. Mit {$bookgenre} \u00FCber {$topic} eroberte sie oder er eine {$fantype} {$community} und wurde {$when} mit \u201E{$booktitle}\u201C f\u00FCr {$prize} nominiert. Die Autorin oder der Autor lebt {$residence}{$endmarker}";
	}
}