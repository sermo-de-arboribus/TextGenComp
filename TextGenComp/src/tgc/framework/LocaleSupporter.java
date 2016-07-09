package tgc.framework;

import java.util.Locale;

public interface LocaleSupporter
{
	Locale[] getSupportedLocales();
	boolean isLocaleSupported(Locale locale);
}