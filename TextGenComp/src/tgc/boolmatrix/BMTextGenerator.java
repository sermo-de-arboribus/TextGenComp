package tgc.boolmatrix;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;

import tgc.framework.Generator;
import tgc.framework.OutputType;
import tgc.framework.OutputTypeNotSupportedException;

public class BMTextGenerator implements Generator
{
	// fields that are autowired by Spring framework
	@Autowired
	private BMModel bmModel;
	@Autowired
	private BMTextdata bmTextData;

	// other private fields
	private Date lastCachingTime = new Date();
	@SuppressWarnings("unused")
	private OutputType outputType;
	private HashMap<String, Locale> supportedLocaleCache = new HashMap<String, Locale>();
	
	@Override
	public Locale[] getSupportedLocales()
	{
		Set<Locale> supportedLocalesOfModel = new HashSet<Locale>(Arrays.asList(bmModel.getSupportedLocales()));
		Set<Locale> supportedLocalesOfTextdata = new HashSet<Locale>(Arrays.asList(bmTextData.getSupportedLocales()));
		supportedLocalesOfModel.retainAll(supportedLocalesOfTextdata);
		
		Locale[] locales = new Locale[supportedLocalesOfModel.size()];
		
		return supportedLocalesOfModel.toArray(locales);
	}

	@Override
	public boolean isLocaleSupported(Locale locale)
	{
		// refresh cache every 10 minutes
		final Date now = new Date();
		if(supportedLocaleCache.isEmpty() || (now.getTime() - lastCachingTime.getTime() > 600000))
		{
			for(final Locale loc : getSupportedLocales())
			{
				supportedLocaleCache.put(loc.toString(), loc);
			}
			lastCachingTime = now;
		}
		return supportedLocaleCache.containsKey(locale.toString());
	}

	@Override
	public String generateText(Locale locale)
	{	
		bmModel.setModelToStandardEggPlant();
		
		StringBuffer sb = new StringBuffer();
		
		if(isLocaleSupported(locale))
		{	
			sb.append(bmTextData.getLocalizedFoodWord(locale, bmModel.getCurrentFoodType()));
			sb.append(" ");
			if(bmModel.containsNoVitamins())
			{
				sb.append(bmTextData.getLocalizedNonContainmentWord(locale));
				sb.append(" ");
				sb.append(bmTextData.getLocalizedGroupWord(locale, IngredientGroup.VITAMINS));
				sb.append(", ");
			}
			if(bmModel.containsNoAllergens())
			{
				sb.append(bmTextData.getLocalizedNonContainmentWord(locale));
				sb.append(" ");
				sb.append(bmTextData.getLocalizedGroupWord(locale, IngredientGroup.ALLERGENS));
				sb.append(", ");
			}
			if(bmModel.containsNoAdditives())
			{
				sb.append(bmTextData.getLocalizedNonContainmentWord(locale));
				sb.append(" ");
				sb.append(bmTextData.getLocalizedGroupWord(locale, IngredientGroup.ADDITIVES));
				sb.append(", ");
			}
			List<Integer> containedIngredientsIndices = bmModel.getContainedIngredientIndices();
			if(!containedIngredientsIndices.isEmpty())
			{
				sb.append(bmTextData.getLocalizedContainmentWord(locale));
				sb.append(" ");
			}
			for(int index : containedIngredientsIndices)
			{
				sb.append(bmTextData.getLocalizedIngredientWord(locale, index));
				sb.append(", ");
			}
			sb.replace(sb.length() - 2, sb.length() - 1, ".");
		}
		else
		{
			throw new IllegalArgumentException("The Locale " + locale.toString() + " is not supported by BMTextGenerator");
		}
		
		return sb.toString();
	}

	@Override
	public void setOutputType(OutputType outputType) throws OutputTypeNotSupportedException
	{
		if(outputType != OutputType.STRING)
		{
			throw new OutputTypeNotSupportedException("This generator only supports STRING as an output format.");
		}
		else
		{
			this.outputType = outputType;
		}

	}

	@Override
	public boolean isSupportedOutputType(OutputType outputType)
	{
		if(outputType != OutputType.STRING)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}