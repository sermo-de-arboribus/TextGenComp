package tgc.speakingcounter;

import java.util.Locale;

import tgc.framework.LocaleSupporter;

public class SpeakingCounterTextdata implements LocaleSupporter
{
	private static final String[] GERMAN_NUMBERS = new String[] { "Null", "Eins", "Zwei", "Drei", "Vier", "Fünf", "Sechs", "Sieben", "Acht", "Neun", "Zehn" };
	private static final String[] ENGLISH_NUMBERS = new String[] { "Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten" };
	
	public String getLocalizedNumberWord(int i, Locale locale)
	{
		switch (locale.getLanguage())
		{
			case "de":
				return GERMAN_NUMBERS[i];
			case "en":
				return ENGLISH_NUMBERS[i];
			default:
				return "";
		}
	}
	
	@Override
	public Locale[] getSupportedLocales()
	{
		return new Locale[] { Locale.GERMAN, Locale.GERMANY, Locale.ENGLISH, Locale.UK, Locale.US };
	}

	@Override
	public boolean isLocaleSupported(Locale locale)
	{
		Locale[] supportedLocales = getSupportedLocales();
		for(Locale loc : supportedLocales)
		{
			if (loc.equals(locale)) return true;
		}
		return false;
	}
}