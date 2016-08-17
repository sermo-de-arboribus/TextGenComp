package tgc.speakingcounter;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import tgc.framework.Generator;
import tgc.framework.OutputType;
import tgc.framework.OutputTypeNotSupportedException;

public class SpeakingCounterGenerator implements Generator
{
	@Autowired
	SpeakingCounterModel speakingCounterModel;
	@Autowired
	SpeakingCounterTextdata speakingCounterTextdata;
	
	private Date lastCachingTime = new Date();
	@SuppressWarnings("unused")
	private OutputType outputType;
	private HashMap<String, Locale> supportedLocaleCache = new HashMap<String, Locale>();
	
	@Override
	public Locale[] getSupportedLocales()
	{
		Set<Locale> supportedLocalesOfModel = new HashSet<Locale>(Arrays.asList(speakingCounterModel.getSupportedLocales()));
		Set<Locale> supportedLocalesOfTextdata = new HashSet<Locale>(Arrays.asList(speakingCounterTextdata.getSupportedLocales()));
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
		return speakingCounterTextdata.getLocalizedNumberWord(speakingCounterModel.getCounterValue(), locale);
	}
	
	@Override
	public void setOutputType(OutputType outputType) throws OutputTypeNotSupportedException
	{
		if (isSupportedOutputType(outputType))
		{
			this.outputType = outputType; 
		}
		else
		{
			throw new OutputTypeNotSupportedException("Speaking Counter does not support outputType " + outputType);
		}		
	}
	
	@Override
	public boolean isSupportedOutputType(OutputType outputType)
	{
		if(outputType == OutputType.STRING) return true;
		
		return false;
	}
}