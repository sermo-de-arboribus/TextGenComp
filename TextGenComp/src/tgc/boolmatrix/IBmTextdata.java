package tgc.boolmatrix;

import java.util.Locale;

import tgc.framework.LocaleSupporter;

public interface IBmTextdata extends LocaleSupporter
{
	public String getLocalizedContainmentWord(Locale loc);
	public String getLocalizedFoodWord(Locale loc, FoodType foodType);
	public String getLocalizedGroupWord(Locale loc, IngredientGroup group);
	public String getLocalizedIngredientWord(final Locale loc, final int index);
	public String getLocalizedNonContainmentWord(Locale loc);
}
