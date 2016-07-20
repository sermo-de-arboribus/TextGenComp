package tgc.epubgenerator;

import java.util.ListIterator;
import java.util.Locale;

import tgc.framework.LocaleSupporter;

public interface IEpubModel extends LocaleSupporter
{
	public ListIterator<String> getChapterIterator(Locale locale);
	public String getLocalizedChapterWord(Locale locale);
	public String getTextBody();
	public void setTextBody(String text);
}