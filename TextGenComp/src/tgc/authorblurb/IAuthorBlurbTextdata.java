package tgc.authorblurb;

import tgc.framework.LocaleSupporter;

public interface IAuthorBlurbTextdata extends LocaleSupporter
{
	public String[] getPossibleWordsForKey(AuthorBlurbPropertyName key);
}