package tgc.epubgenerator;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

public class EpubTextdata implements IEpubTextdata
{
	private HashMap<String, Locale> supportedLocaleCache;

	public EpubTextdata()
	{
		supportedLocaleCache = new HashMap<String, Locale>();
	}
	
	@Override
	public String getCoverHtml(final String coverImagePath)
	{
		return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>"
				+ "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
				+ "<head>"
				+ "<title>Cover Image</title>"
				+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>"
				+ "</head>"
				+ "<body style=\"text-align:center\">"
				+ "<div style=\"text-align:center;width:95%\">"
				+ "<img alt=\"Cover\" src=\"../" + coverImagePath + "\" style=\"max-width:90%\" />"
				+ "</div>"
				+ "</body>"
				+ "</html>";
	}

	@Override
	public Locale[] getSupportedLocales()
	{
		return Locale.getAvailableLocales();
	}

	@Override
	public String getTitlePageHtml(final Locale locale, final String title, final String author)
	{
		return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>"
				+ "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
				+ "<head>"
				+ "<title>Title Page " + title + "</title>"
				+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>"
				+ "</head>"
				+ "<body style=\"text-align:center\">"
				+ "<h1>" + title + "</h1>"
				+ "<div><br/></div>"
				+ "<h2>" + author + "</h2>"
				+ "<div><br/></div>"
				+ "<div><br/></div>"
				+ "<div><br/></div>"
				+ "<div>Komponentenverlag</div>"
				+ "<div>Trier</div>"
				+ "<div>" + new GregorianCalendar().getWeekYear() + "</div>"
				+ "<div><br/></div>"
				+ "</body>"
				+ "</html>";
	}

	@Override
	public String getChapterHtml(final int chapterNumber, final String chapterTitle, final String chapterText)
	{
		return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>"
				+ "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
				+ "<head>"
				+ "<title>Chapter " + chapterNumber + "</title>"
				+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>"
				+ "</head>"
				+ "<body>"
				+ "<h1>" + chapterTitle + " " + chapterNumber + "</h1>"
				+ "<div><br/></div>"
				+ "<div>" + chapterText.replace("\n", "<br/>") + "</div>"
				+ "</body>"
				+ "</html>";
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
}