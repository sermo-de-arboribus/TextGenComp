package tgc.boolmatrix;

import java.util.List;

import tgc.framework.LocaleSupporter;

public interface IBmModel extends LocaleSupporter
{
	public boolean containsNoAdditives();
	public boolean containsNoAllergens();
	public boolean containsNoVitamins();
	public List<Integer> getContainedIngredientIndices();
	public FoodType getCurrentFoodType();
	public void setIngredients(FoodType type, boolean[] ingredients);
	public void setModelToStandardEggPlant();
	public void setModelToStandardGooseLiver();
	public void setModelToStandardMilkRice();
	public void setModelToStandardSausage();
}