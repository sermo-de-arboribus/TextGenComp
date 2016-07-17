package tgc.novelfromquotes;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import tgc.framework.AbstractLocaleSupporter;

public class NovelFromQuotesTextdata extends AbstractLocaleSupporter
{
	// members that are expected to be configured in the Beans.xml
	private File quotesConfiguration;
	
	// other members that are used internally
	private final Set<Sentence> allSentences; // all sentences
	private final Map<Locale, List<Sentence>> localizedSentences; // hash map for easy access to localized sentences
	private final List<Sentence> randomAccessSentences;
	private final Random random;
	
	public NovelFromQuotesTextdata(String configFilePath)
	{
		super();
		random = new Random();
		
		setQuotesConfiguration(configFilePath);
		
		allSentences = new HashSet<Sentence>();
		localizedSentences = new HashMap<Locale, List<Sentence>>();
		randomAccessSentences = new ArrayList<Sentence>();

		// fills the Set<Sentence> (using a HashSet here for simple duplicate exclusion)
		readConfiguredSentences();
		// fills the Map<Locale, List<Sentence>> (for easy access to localized sentences)
		buildLocalizedSentencesMap();
		// fills the List<Sentence> (for random access in O(1) time)
		buildRandomAccessList();
	}

	public String getLocalizedRandomSentence(Locale locale)
	{
		List<Sentence> localizedSentenceList = localizedSentences.get(locale);
		int index = random.nextInt(localizedSentenceList.size());
		return localizedSentenceList.get(index).getText();
	}
	
	public String getRandomSentence()
	{
		int index = random.nextInt(randomAccessSentences.size());
		return randomAccessSentences.get(index).getText();
	}
	
	@Override
	public Locale[] getSupportedLocales()
	{
		return supportedLocaleCache.values().toArray(new Locale[]{});
	}
	
	public void setQuotesConfiguration(String filePath)
	{
		quotesConfiguration = new File(filePath);
	}
	
	private void buildLocalizedSentencesMap()
	{
		Iterator<Sentence> sentenceIterator = allSentences.iterator();
		while(sentenceIterator.hasNext())
		{
			Sentence sentence = sentenceIterator.next();
			if(!localizedSentences.containsKey(sentence.getLocale()))
			{
				List<Sentence> newSet = new ArrayList<Sentence>();
				newSet.add(sentence);
				localizedSentences.put(sentence.locale, newSet);
			}
			else
			{
				localizedSentences.get(sentence.locale).add(sentence);
			}
		}
	}
	
	private void buildRandomAccessList()
	{
		Iterator<Sentence> iterator = allSentences.iterator();
		
		while(iterator.hasNext())
		{
			randomAccessSentences.add(iterator.next());
		}
	}
	
	private Sentence getSentence(Element versionElement)
	{
		// normalize Locales: just use the language part, omit the country
		Locale locale = new Locale(versionElement.getAttribute("xml:lang").substring(0, 2));
		// .item(0) might theoretically through a NullReferenceException, but we assume that 
		// the loaded XML file is checked against quotes-schema.xsd, so we can be sure to always
		// get a <text> or <author> element
		String text = versionElement.getElementsByTagName("text").item(0).getTextContent();
		Element parent = (Element)versionElement.getParentNode();
		String author = parent.getElementsByTagName("author").item(0).getTextContent();

		// also fill the supportedLocaleCache of the parent class
		supportedLocaleCache.put(locale.toString(), locale);
		
		return new Sentence(text, author, locale);
	}

	private void readConfiguredSentences()
	{
		// read the XML file that holds the configured sentences
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document doc = null;
		
		try
		{
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(quotesConfiguration);
		}
		catch(ParserConfigurationException exc)
		{
			System.err.println("Error: Could not instantiate XML parser for reading configuration.");
			System.err.println(exc.getLocalizedMessage());
		}
		catch(SAXException exc)
		{
			System.err.println("SAX Error: Could not read XML file " + quotesConfiguration.getAbsolutePath());
			System.err.println(exc.getLocalizedMessage());
		}
		catch(IOException exc)
		{
			System.err.println("I/O Error: Could not read XML file " + quotesConfiguration.getAbsolutePath());
			System.err.println(exc.getLocalizedMessage());
		}
		
		Element documentElement = doc.getDocumentElement();
		
		NodeList quotes = documentElement.getElementsByTagName("quote");
		if(quotes != null && quotes.getLength() > 0)
		{
			for(int i = 0 ; i < quotes.getLength();i++)
			{
				// get the quote element
				Element quote = (Element)quotes.item(i);

				NodeList versions = quote.getElementsByTagName("version");
				
				for(int j = 0; j < versions.getLength(); j++)
				{
					// get the version element
					Element version = (Element)versions.item(j);
					
					// instantiate a Sentence object for each version
					Sentence sentence = getSentence(version);
					
					//add it to the set
					allSentences.add(sentence);
				}
			}
		}
	}
	
	/**
	 * This helper class represents a sentence (or rather a quotation, which could contain several sentences).
	 * The Sentence class implements the Comparable<Sentence> interface, because it is meant to be used by
	 * NovelFromQuotesTextdata in a hash-based Set.
	 * 
	 * @author Kai Weber
	 */
	private static class Sentence implements Comparable<Sentence>
	{
		private final String text;
		private final String author;
		private final Locale locale;
		
		/**
		 * Helper class that represents one or more sentences that make up a quote.
		 * @param text The text of the quote.
		 * @param author The author of the quote.
		 * @param locale The locale of the quote, mainly used to represent the language.
		 */
		Sentence(final String text, final String author, final Locale locale)
		{
			this.text = text;
			this.author = author;
			this.locale = locale;
		}

		@Override
		public int compareTo(final Sentence otherSentence)
		{
			return (text.compareTo(otherSentence.text));
		}
		
		@Override
		public boolean equals(final Object otherObject)
		{
			if(otherObject == null)
			{
				return false;
			}
			
			if(this == otherObject)
			{
				return true;
			}
			
			if(this.getClass() != otherObject.getClass())
			{
				return false;
			}
			
			final Sentence otherSentence = (Sentence)otherObject;
			return compareTo(otherSentence) == 0;
		}
		
		@SuppressWarnings("unused")
		public String getAuthor()
		{
			return author;
		}

		public Locale getLocale()
		{
			return locale;
		}
		
		public String getText()
		{
			return text;
		}

		@Override
		public int hashCode()
		{
			return text.hashCode();
		}
	}
}