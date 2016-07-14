package tgc.epubgenerator;

import java.util.GregorianCalendar;
import java.util.Locale;

public class EpubTextData
{

	public String getCoverHtml(String coverImagePath)
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
				+ "<img alt=\"Cover\" src=\"" + coverImagePath + "\" style=\"max-width:90%\" />"
				+ "</div>"
				+ "</body>"
				+ "</html>";
	}
	
	public String getTitlePageHtml(Locale locale, String title, String author)
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

	public String getChapterHtml(int chapterNumber, String chapterTitle, String chapterText)
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
				+ "<div>" + chapterText.replace("[\r\n]+", "<br/>") + "</div>"
				+ "</body>"
				+ "</html>";
	}
}