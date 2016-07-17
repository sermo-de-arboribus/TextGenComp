package tgc.boolmatrix;

import tgc.framework.AbstractLocaleSupporter;

public class BMModel extends AbstractLocaleSupporter
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
	
	public FoodType getCurrentFoodType()
	{
		return currentFoodType;
	}
	
	public void setIngredients(FoodType type, boolean[] ingredients)
	{
		currentFoodType = type;
		validateIngredientsArray(ingredients);
		currentFoodIngredients = ingredients;
	}
	
	public void setModelToStandardEggPlant()
	{
		setModelToStandard(2);
	}
	
	public void setModelToStandardGooseLiver()
	{
		setModelToStandard(3);
	}
	
	public void setModelToStandardMilkRice()
	{
		setModelToStandard(1);
	}
	
	public void setModelToStandardSausage()
	{
		setModelToStandard(0);
	}
	
	private void setModelToStandard(int index)
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