package tgc.authorblurb;

public enum AuthorBlurbPropertyName
{
	AUTHORNAME("authorname"), BIRTHYEAR("birthyear"), BIRTHPLACE("birthplace"), BOOKGENRE("bookgenre"), 
	TOPIC("topic"), WHEN("when"), BOOKTITLE("booktitle"), PRIZE("prize"), RESIDENCE("residence"), 
	FANTYPE("fantype"), ENDMARKER("endmarker"), COMMUNITY("community");

	public final String propertyKey;
	
	AuthorBlurbPropertyName(final String propertyKey)
	{
		this.propertyKey = propertyKey;
	}
}
