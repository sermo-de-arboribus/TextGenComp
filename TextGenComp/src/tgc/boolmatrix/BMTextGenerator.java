package tgc.boolmatrix;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;

import tgc.framework.Generator;
import tgc.framework.OutputType;
import tgc.framework.OutputTypeNotSupportedException;

public class BMTextGenerator implements Generator
{
	// fields that are autowired by Spring framework
	@Autowired
	private BMModel bmModel;
	@Autowired
	private BMTextdata bmTextData;

	// other private fields
	private Date lastCachingTime = new Date();
	private OutputType outputType;
	private HashMap<String, Locale> supportedLocaleCache = new HashMap<String, Locale>();
	
	@Override
	public Locale[] getSupportedLocales()
	{
		Set<Locale> supportedLocalesOfModel = new HashSet<Locale>(Arrays.asList(bmModel.getSupportedLocales()));
		Set<Locale> supportedLocalesOfTextdata = new HashSet<Locale>(Arrays.asList(bmTextData.getSupportedLocales()));
		supportedLocalesOfModel.retainAll(supportedLocalesOfTextdata);
		
		Locale[] locales = new Locale[supportedLocalesOfModel.size()];
		
		return supportedLocalesOfModel.toArray(locales);
	}

	@Override
	public boolean isLocaleSupported(Locale locale)
	{
		// refresh cache every 10 minutes
		final Date now = new Date();
		if(supportedLocaleCache.isEmpty() || (now.getTime() - lastCachingTime.getTime() > 600000))
		{
			for(final Locale loc : getSupportedLocales())
			{
				supportedLocaleCache.put(loc.toString(), loc);
			}
			lastCachingTime = now;
		}
		return supportedLocaleCache.containsKey(locale.toString());
	}

	@Override
	public String generateText(Locale locale)
	{
		// TODO: differentiate between OutputTypes
		
		StringBuffer sb = new StringBuffer();
		
		if(isLocaleSupported(locale))
		{		
			sb.append(bmTextData.getLocalizedFoodWord(locale, bmModel.getCurrentFoodType()));
			sb.append(" ");
		}
		else
		{
			throw new IllegalArgumentException("The Locale " + locale.toString() + " is not supported by BMTextGenerator");
		}
		
		return sb.toString();
	}

	@Override
	public void setOutputType(OutputType outputType) throws OutputTypeNotSupportedException
	{
		if(outputType == OutputType.EPUBFILE)
		{
			throw new OutputTypeNotSupportedException("This generator does not support EPUB as an output format.");
		}
		else
		{
			this.outputType = outputType;
		}

	}

	@Override
	public boolean isSupportedOutputType(OutputType outputType)
	{
		if(outputType == OutputType.EPUBFILE)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}