package tgc.novelfromquotes;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import tgc.framework.AbstractLocaleSupporter;

public class NovelFromQuotesModel extends AbstractLocaleSupporter
{
	// fields that are expected to be configured in the Spring configuration file Beans.xml
	private int numberOfChapters;
	private int quotesPerChapter;
	
	private final Map<Locale, String> chapterWords = new HashMap<Locale, String>();
	
	public NovelFromQuotesModel()
	{
		super();
		initializeChapterWords();
	}

	public int getNumberOfChapters()
	{
		return numberOfChapters;
	}
	
	@Override
	public Locale[] getSupportedLocales()
	{
		return chapterWords.keySet().toArray(new Locale[]{});
	}

	public void setNumberOfChapters(int numberOfChapters)
	{
		this.numberOfChapters = numberOfChapters;
	}

	public String getLocalizedChapterWord(Locale locale)
	{
		if(!isLocaleSupported(locale))
		{
			return ""; 
		}
		else
		{
			return chapterWords.get(locale);
		}
	}
	
	public int getQuotesPerChapter()
	{
		return quotesPerChapter;
	}

	public void setQuotesPerChapter(int quotesPerChapter)
	{
		this.quotesPerChapter = quotesPerChapter;
	}

	private void initializeChapterWords()
	{
		chapterWords.put(new Locale("cs", "CZ"), "Kapitola");
		chapterWords.put(new Locale("de"), "Kapitel");
		chapterWords.put(new Locale("de", "AT"), "Kapitel");
		chapterWords.put(new Locale("de", "CH"), "Kapitel");
		chapterWords.put(new Locale("de", "CZ"), "Kapitel");
		chapterWords.put(new Locale("de", "DE"), "Kapitel");
		chapterWords.put(new Locale("de", "LU"), "Kapitel");
		chapterWords.put(new Locale("en"), "Chapter");
		chapterWords.put(new Locale("en", "AT"), "Chapter");
		chapterWords.put(new Locale("en", "AU"), "Chapter");
		chapterWords.put(new Locale("en", "CA"), "Chapter");
		chapterWords.put(new Locale("en", "CN"), "Chapter");
		chapterWords.put(new Locale("en", "DE"), "Chapter");
		chapterWords.put(new Locale("en", "GB"), "Chapter");
		chapterWords.put(new Locale("en", "NZ"), "Chapter");
		chapterWords.put(new Locale("en", "US"), "Chapter");
		chapterWords.put(new Locale("en", "ZA"), "Chapter");
		chapterWords.put(new Locale("es"), "Capítulo");
		chapterWords.put(new Locale("fr"), "Chapitre");
		chapterWords.put(new Locale("fr", "FR"), "Chapitre");
		chapterWords.put(new Locale("grc"), "κεφάλαιο");
		chapterWords.put(new Locale("it"), "Chapitre");
		chapterWords.put(new Locale("it", "IT"), "Capitolo");
		chapterWords.put(new Locale("la"), "Caput");
		chapterWords.put(new Locale("pl"), "Rozdział");
		chapterWords.put(new Locale("pl", "PL"), "Rozdział");
		chapterWords.put(new Locale("ru"), "глава");
		chapterWords.put(new Locale("ru", "RU"), "глава");
		chapterWords.put(new Locale("se"), "Kapitel");
		chapterWords.put(new Locale("se", "SE"), "Kapitel");
		chapterWords.put(new Locale("zh"), "卷");
		chapterWords.put(new Locale("zh", "CN"), "卷");
 
		/* cs-CZ - 3 
		 * de-AT - 15 
		 * de-CH - 5 
		 * de-CZ - 1 
		 * de-DE - 216 
		 * en - 19 
		 * en-AT - 2 
		 * en-CN - 1 
		 * en-DE - 2 
		 * en-GB - 23 
		 * en-US - 12 
		 * es - 1 
		 * fr-FR - 24 
		 * grc - 1 
		 * it-IT - 5 
		 * la - 2 
		 * pl-PL - 3 
		 * ru-RU - 5 
		 * se-SE - 1 
		 * zh-CN - 9
		 */		
	}
}