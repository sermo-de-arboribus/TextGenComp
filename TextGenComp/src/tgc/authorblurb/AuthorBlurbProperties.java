package tgc.authorblurb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import tgc.framework.StreamHelper;

/**
 * This class encapsulates the java.util.Properties class. It prevents a problem associated
 * with this class:
 * In java.util.Properties arbitrary Objects could be stored as properties, because it extends Hashtable<K,V>
 * By wrapping properties here, we can ensure that only properties that make sense for AuthorBlurb can be 
 * used in getters and setters (controlled by enum type AuthorBlurbPropertyNames).
 * @author Kai Weber, inspired by Michael Inden ("Der Weg zum Java-Profi")
 */
public class AuthorBlurbProperties
{
	private static final String FILE_PATH = "config/AuthorBlurb.properties";
	private final Properties properties = new Properties();
	
	public AuthorBlurbProperties() throws IOException
	{
		readAppProperties();
	}
		
	public synchronized String[] getProperty(final AuthorBlurbPropertyName key)
	{
		return properties.getProperty(key.propertyKey).split(",");
	}
	
	public static synchronized String getPropertyFilePath()
	{
		return new File(FILE_PATH).getAbsolutePath();
	}
	
	public synchronized void setProperty(final AuthorBlurbPropertyName key, final String value)
	{
		properties.setProperty(key.propertyKey, value);
	}
	
	private synchronized void readAppProperties() throws IOException
	{
		InputStream inputStream = null;
		try
		{
			inputStream = new FileInputStream(FILE_PATH);
			properties.load(inputStream);
		}
		finally
		{
			StreamHelper.safeClose(inputStream);
		}
	}
}