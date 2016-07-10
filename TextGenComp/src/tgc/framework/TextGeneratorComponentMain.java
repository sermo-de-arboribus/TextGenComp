package tgc.framework;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.*;
import org.springframework.context.support.*;

public class TextGeneratorComponentMain
{
	public static void main(String[] args) throws Exception
	{
		Locale locale = validateArgs(args);
		
		ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
		
		Map<String, Generator> allGenerators = context.getBeansOfType(Generator.class);
		
		Iterator<Generator> generatorsIterator = allGenerators.values().iterator();
		while(generatorsIterator.hasNext())
		{
			Generator generator = generatorsIterator.next();
			
			// check if this generator can handle locale
			if(generator.isLocaleSupported(locale))
			{
				System.out.println(generator.generateText(locale));
			}
			else
			{
				System.out.println("Locale " + locale.toString() + " not supported for generator " + generator.getClass().getName());
			}
		}
		
		((ClassPathXmlApplicationContext)context).close();
	}
	
	private static Locale validateArgs(String[] args)
	{
		if(args.length != 1)
		{
			System.out.println("TextGeneratorComponentMain must be called with exactly one argument.");
			System.out.println("The argument is the locale string (e.g. 'de', 'en-US') that is used to generate the output text");
			throw new IllegalArgumentException("Expected one command line argument.");
		}
		String[] localeArgs = args[0].split("-");
		if(localeArgs.length > 1)
		{
			return new Locale(localeArgs[0], localeArgs[1]);
		}
		else
		{
			return new Locale(localeArgs[0]);
		}		
	}
}