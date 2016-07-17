package tgc.boolmatrix;

public enum FoodType
{
	SAUSAGE, MILKRICE, EGGPLANT, GOOSELIVER;
	
	public static FoodType fromInteger(int x)
	{
        switch(x)
        {
        case 0:
            return SAUSAGE;
        case 1:
            return MILKRICE;
        case 2:
        	return EGGPLANT;
        case 3:
        	return GOOSELIVER;
        default:
        }
        return null;
    }
}