package tgc.novelfromquotes;

import java.util.Locale;

import tgc.framework.LocaleSupporter;

public interface INovelFromQuotesTextdata extends LocaleSupporter
{
	public String getLocalizedRandomSentence(Locale locale);
	public String getRandomSentence();
	public void setQuotesConfiguration(String filePath);
}