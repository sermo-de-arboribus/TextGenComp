package tgc.novelfromquotes;

import java.util.Locale;

import tgc.framework.LocaleSupporter;

public interface INovelFromQuotesModel extends LocaleSupporter
{
	public int getNumberOfChapters();
	public void setNumberOfChapters(int numberOfChapters);
	public String getLocalizedChapterWord(Locale locale);
	public int getQuotesPerChapter();
	public void setQuotesPerChapter(int quotesPerChapter);
}