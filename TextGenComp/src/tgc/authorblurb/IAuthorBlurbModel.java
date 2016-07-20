package tgc.authorblurb;

import java.util.Locale;

import tgc.framework.LocaleSupporter;

public interface IAuthorBlurbModel extends LocaleSupporter
{
	public String getModel(Locale locale);
}