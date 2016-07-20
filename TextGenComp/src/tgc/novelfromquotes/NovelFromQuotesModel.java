package tgc.novelfromquotes;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NovelFromQuotesModel implements INovelFromQuotesModel
{
	// fields that are expected to be configured in the Spring configuration file Beans.xml
	private int numberOfChapters;
	private int quotesPerChapter;
	
	private final Map<Locale, String> chapterWords = new HashMap<Locale, String>();
	private HashMap<String, Locale> supportedLocaleCache;
	
	public NovelFromQuotesModel()
	{
		supportedLocaleCache = new HashMap<String, Locale>();
		initializeChapterWords();
	}

	@Override
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
	
	@Override
	public int getNumberOfChapters()
	{
		return numberOfChapters;
	}
	
	@Override
	public int getQuotesPerChapter()
	{
		return quotesPerChapter;
	}

	@Override
	public Locale[] getSupportedLocales()
	{
		return chapterWords.keySet().toArray(new Locale[]{});
	}

	@Override
	public boolean isLocaleSupported(Locale locale)
	{
		// on first look-up, fill the locale cache, so that further lookups can be 
		// served more efficiently
		if(supportedLocaleCache.isEmpty())
		{
			for(Locale loc : getSupportedLocales())
			{
				supportedLocaleCache.put(loc.toString(), loc);
			}
		}
		return supportedLocaleCache.containsKey(locale.toString()); 
	}
	
	@Override
	public void setNumberOfChapters(int numberOfChapters)
	{
		this.numberOfChapters = numberOfChapters;
	}


	@Override
	public void setQuotesPerChapter(int quotesPerChapter)
	{
		this.quotesPerChapter = quotesPerChapter;
	}

	private void initializeChapterWords()
	{
		chapterWords.put(new Locale("cs"), "Kapitola");
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
		chapterWords.put(new Locale("it"), "Capitolo");
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
	}
}