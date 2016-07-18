package tgc.boolmatrix;

public enum IngredientGroup
{
	VITAMINS, ALLERGENS, ADDITIVES;
	
	public static IngredientGroup fromInteger(int x)
	{
        switch(x)
        {
        case 0:
            return VITAMINS;
        case 1:
            return ALLERGENS;
        case 2:
        	return ADDITIVES;
        default:
        }
        return null;
    }
}
