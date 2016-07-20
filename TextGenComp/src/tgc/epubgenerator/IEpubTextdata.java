package tgc.epubgenerator;

import java.util.Locale;

import tgc.framework.LocaleSupporter;

public interface IEpubTextdata extends LocaleSupporter
{
	public String getCoverHtml(String coverImagePath);
	public String getTitlePageHtml(Locale locale, String title, String author);
	public String getChapterHtml(int chapterNumber, String chapterTitle, String chapterText);
}