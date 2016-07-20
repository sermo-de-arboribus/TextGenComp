package tgc.boolmatrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class BMModel implements IBmModel
{
	/*  Index:
	 *  0      1   2    3    4    5    6    7   8    9     10        11     12       13            14          15             16
	 *       | Vitamins                            | Allergenes                    | Additives     
	 *       | A | B1 | B2 | B3 | B6 | B12 | C | E | Nut | Lactose | Soy  | Gluten | Antioxidant | Colouring | Preservative | Flavour enhancer
	 *  Sausage
	 *  Milk rice
	 *  Egg-plant
	 *  Goose liver
	 */
	private static final boolean[][] STANDARD_INGREDIENTS_MATRIX = new boolean[][]
	{
		// Ingredients list for fried sausages
		{ false, /* vitamins */ false, true, true, false, true, true, false, true, /* allergens */ false, false, false, false, /* additives */ false, true, true, true },
		// Ingredients list for milk rice
		{ false, /* vitamins */ true, true, true, true, true, true, false, false, /* allergens */ true, true, false, false, /* additives */ true, false, true, false },
		// Ingredients list for egg-plant
		{ false, /* vitamins */ false, true, true, false, true, false, false, true, /* allergens */ false, false, false, false, /* additives */ false, false, false, false },
		// Ingredients list for goose liver
		{ false, /* vitamins */ true, true, true, false, true, false, true, true, /* allergens */ false, false, false, false, /* additives */ true, false, true, false }
	};
	
	private boolean[] currentFoodIngredients;
	private FoodType currentFoodType;
	protected HashMap<String, Locale> supportedLocaleCache;
	
	public BMModel()
	{
		supportedLocaleCache = new HashMap<String, Locale>();
	}
	
	@Override
	public boolean containsNoAdditives()
	{
		boolean hasNoAdditives = true;
		for(int i = 13; i < 17; i++)
		{
			// for "containsNoAdditives" to be true, all additive values must be "false"
			// if at least one additive value is true (and its negation therefore false), 
			// the whole expression becomes false
			hasNoAdditives &= !currentFoodIngredients[i];
		}
		return hasNoAdditives;
	}
	
	@Override
	public boolean containsNoAllergens()
	{
		boolean hasNoAllergens = true;
		for(int i = 9; i < 13; i++)
		{
			// for "containsNoAllergens" to be true, all allergen values must be "false"
			// if at least one allergen value is true (and its negation therefore false), 
			// the whole expression becomes false
			hasNoAllergens &= !currentFoodIngredients[i];
		}
		return hasNoAllergens;
	}

	@Override
	public boolean containsNoVitamins()
	{
		boolean hasNoVitamins = true;
		for(int i = 1; i < 9; i++)
		{
			// for "containsNoVitamins" to be true, all vitamin values must be "false"
			// if at least one vitamin value is true (and its negation therefore false), 
			// the whole expression becomes false
			hasNoVitamins &= !currentFoodIngredients[i];
		}
		return hasNoVitamins;
	}

	@Override
	public FoodType getCurrentFoodType()
	{
		return currentFoodType;
	}

	@Override
	public List<Integer> getContainedIngredientIndices()
	{
		List<Integer> trueIndices = new ArrayList<>();
		// start with index 1, as index 0 does not represent an ingredient
		for(int i = 1; i < currentFoodIngredients.length; i++)
		{
			if(currentFoodIngredients[i])
			{
				trueIndices.add(i);
			}
		}
		
		return trueIndices;
	}

	@Override
	public Locale[] getSupportedLocales()
	{
		return Locale.getAvailableLocales();
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
	
	@Override
	public void setIngredients(FoodType type, boolean[] ingredients)
	{
		currentFoodType = type;
		validateIngredientsArray(ingredients);
		currentFoodIngredients = ingredients;
	}
	
	@Override
	public void setModelToStandardEggPlant()
	{
		setModelToStandard(2);
	}
	
	@Override
	public void setModelToStandardGooseLiver()
	{
		setModelToStandard(3);
	}
	
	@Override
	public void setModelToStandardMilkRice()
	{
		setModelToStandard(1);
	}
	
	@Override
	public void setModelToStandardSausage()
	{
		setModelToStandard(0);
	}
	
	private void setModelToStandard(final int index)
	{
		currentFoodType = FoodType.fromInteger(index);
		currentFoodIngredients = STANDARD_INGREDIENTS_MATRIX[index];
	}
	
	private void validateIngredientsArray(boolean[] ingredients)
	{
		if(ingredients.length != 17)
		{
			throw new IllegalArgumentException("A food ingredients list must be a boolean array with exactly 17 elements ('columns').");
		}
	}
}