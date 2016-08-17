package tgc.speakingcounter;

import java.util.Locale;

import tgc.framework.LocaleSupporter;

public class SpeakingCounterModel implements LocaleSupporter
{
	private final int TOP_LIMIT = 10;
	private final int BOTTOM_LIMIT = 0;
	
	private int counterValue;
	
	public SpeakingCounterModel()
	{
		counterValue = 0;
	}
	
	public void increment()
	{
		if(counterValue < TOP_LIMIT)
		{
			counterValue++;	
		}
	}
	
	public void decrement()
	{
		if(counterValue > BOTTOM_LIMIT)
		{
			counterValue--;	
		}
	}
	
	public int getCounterValue()
	{
		return counterValue;
	}
	
	public void setCounterValue(int i)
	{
		if(BOTTOM_LIMIT <= i && i <= TOP_LIMIT)
		{
			counterValue = i;
		}
	}
	
	@Override
	public Locale[] getSupportedLocales()
	{
		return Locale.getAvailableLocales();
	}

	@Override
	public boolean isLocaleSupported(Locale locale)
	{
		return true;
	}	
}
