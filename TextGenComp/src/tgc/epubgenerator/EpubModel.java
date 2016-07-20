package tgc.epubgenerator;

import java.util.*;

public class EpubModel implements IEpubModel
{	
	private final Map<Locale, String> chapterWords = new HashMap<Locale, String>();
	private String textBody = "";
	protected HashMap<String, Locale> supportedLocaleCache;
	
	public EpubModel()
	{
		supportedLocaleCache = new HashMap<String, Locale>();
		initializeChapterWords();
	}

	/**
	 * This method takes a complete text and splits it into chapters, based on a localized keyword signifying the beginning of a new chapter
	 * (e.g. "Chapter", "Kapitel", "Chapitre", etc.)
	 * @param completeText The complete text of an article, book, etc.
	 * @return a ListIterator<String> to run through the split chapters
	 */
	@Override
	public ListIterator<String> getChapterIterator(final Locale locale)
	{
		LinkedList<String> chapterTexts = new LinkedList<String>(Arrays.asList(textBody.split(getChapterSeparatorRegex(locale))));
		// remove trailing empty strings
		if(chapterTexts.get(0) == null || chapterTexts.get(0).equals(""))
		{
			chapterTexts.remove(0);
		}
		return chapterTexts.listIterator();
	}
	
	@Override
	public String getLocalizedChapterWord(final Locale locale)
	{
		return chapterWords.get(locale);
	}
	
	@Override
	public Locale[] getSupportedLocales()
	{
		return chapterWords.keySet().toArray(new Locale[]{});
	}

	@Override
	public String getTextBody()
	{
		return textBody;
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
	public void setTextBody(final String text)
	{
		this.textBody = text;
	}
	
	private String getChapterSeparatorRegex(Locale locale)
	{ 
		// use lookbehind regex (?=SPLITREGEX) so we do not lose the delimiters in the output text
		// return "(?=" + chapterWords.get(locale) + "\\s*[0-9]+)";
		return chapterWords.get(locale) + "\\s*[0-9]+";
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
 	}
}