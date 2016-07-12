package tgc.epubgenerator;

public class EpubTextData
{

	public String getCoverHtml()
	{
		return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>"
				+ "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
				+ "<head>"
				+ "<title>Coverseite</title>"
				+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>"
				+ "</head>"
				+ "<body style=\"text-align:center\">"
				+ "<div style=\"text-align:center;width:95%\">"
				+ "<img alt=\"Cover\" src=\"{$coverjpgpath}\" style=\"max-width:90%\" />"
				+ "</div>"
				+ "</body>"
				+ "</html>";
	}
}
