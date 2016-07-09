package tgc.framework;

import java.util.Locale;

public interface Generator extends LocaleSupporter
{
	public String generateText(Locale locale);
}