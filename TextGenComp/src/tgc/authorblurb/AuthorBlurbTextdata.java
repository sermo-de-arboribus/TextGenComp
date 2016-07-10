package tgc.authorblurb;

import java.io.IOException;
import java.util.Locale;
import tgc.framework.AbstractLocaleSupporter;

public class AuthorBlurbTextdata extends AbstractLocaleSupporter
{
	private AuthorBlurbProperties configurationProperties;
	
	public AuthorBlurbTextdata()
	{
		super();
		
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

	@Override
	public Locale[] getSupportedLocales()
	{
		return new Locale[]{ new Locale("de", "DE"), new Locale("de", "AT"), new Locale("de", "CH"), new Locale("de") };
	}
}