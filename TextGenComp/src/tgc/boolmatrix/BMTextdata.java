package tgc.boolmatrix;

import java.util.Locale;

import tgc.framework.AbstractLocaleSupporter;

// This class contains localized strings for a matrix describing food ingredients
public class BMTextdata extends AbstractLocaleSupporter
{
	/*  Index:
	 *  0      1   2    3    4    5    6    7   8    9     10        11     12       13            14          15             16
	 *  Lang | Vitamins                            | Allergenes                    | Additives     
	 *       | A | B1 | B2 | B3 | B6 | B12 | C | E | Nut | Lactose | Soy  | Gluten | Antioxidant | Colouring | Preservative | Flavour enhancer
	 * 
	 */
	
	private static final String[][] INGREDIENT_WORDS = new String[][]
	{
		{ "de", "Vitamin A", "Vitamin B₁", "Vitamin B₂", "Vitamin B₃", "Vitamin B₆", "Vitamin B₁₂", "Vitamin C", "Vitamin E", "Spuren von Nüssen", "Laktose", "Soja", "Gluten", "Antioxidationsmittel", "Farbstoff", "Konservierungsmittel", "Geschmacksverstärker" },
		{ "en", "vitamin A", "vitamin B₁", "vitamin B₂", "vitamin B₃", "vitamin B₆", "vitamin B₁₂", "vitamin C", "vitamin E", "traces of nuts", "lactose", "soy", "gluten", "antioxidant", "food colouring", "preservative", "flavour enhancer" },
		{ "fr", "vitamine A", "vitamine B₁", "vitamine B₂", "vitamine B₃", "vitamine B₆", "vitamine B₁₂", "vitamine C", "vitamine E", "traces des noix", "lactose", "soja", "gluten", "antioxydant", "colorant", "conservateur", "exhausteur" },
		{ "cs", "vitamin A", "vitamin B₁", "vitamin B₂", "vitamin B₃", "vitamin B₆", "vitamin B₁₂", "vitamin C", "vitamin E", "drobet ořechů", "laktóza", "sója", "gluten", "antioxidant", "barvivo", "konzervant", "látka zvýrazňující chuť a vůni" },
		{ "zh", "维生素A", "维生素B₁", "核黄素", "烟酸", "维生素B₆", "维生素B₁₂", "维生素C", "维生素E", "果仁", "乳糖", "黄豆", "面筋", "抗氧化剂", "食用​色素", "保鲜剂", "味精"}
	};
	
	private static final String[][] FOOD_WORDS = new String[][]
	{
		{ "", "Bratwurst", "fried sausage","saucisse grillé", "pečená klobása", "德国​油​煎​香肠" },
		{ "", "Milchreis", "rice pudding", "riz au lait", "mléčná rýže", "大米​布丁" },
		{ "", "Aubergine", "egg-plant", "aubergine", "baklažán", "茄子"},
		{ "", "Gänseleber", "goose liver", "foie d'oie", "husí játra", "鹅肝" }
	};
	
	private static final String[] CONTAINMENT_WORDS = new String[] { "enthält", "contains", "contient", "obsáhá", "包含" };
	private static final String[] NON_CONTAINMENT_WORDS = new String[] { "enthält keine", "doesn't contain", "ne contient pas", "neobsáhá", "不包含" };

	private static final String[][] GROUP_WORDS = new String[][]
	{
		{ "de", "Vitamine", "Allergene", "Zusatzstoffe" },
		{ "en", "vitamins", "allergens", "additives" },
		{ "fr", "des vitamins", "des allergènes", "des additives"},
		{ "cs", "vitaminy", "alergeny", "přídání" },
		{ "zh", "维生素", "过敏原", "食品​添加剂" }
	};
	
	private static final Locale[] SUPPORTED_LOCALES = new Locale[]
	{
		Locale.US, Locale.UK, Locale.CANADA, Locale.CHINA, Locale.GERMANY, Locale.GERMAN, new Locale("de", "AT"), new Locale("de", "CH"), new Locale("cs"), new Locale("cs", "CZ"), Locale.FRANCE, Locale.FRENCH
	};

	public String getLocalizedFoodWord(Locale loc, FoodType foodType)
	{
		return FOOD_WORDS[foodTypeToIndex(foodType)][localeToIndex(loc)];
	}
	
	@Override
	public Locale[] getSupportedLocales()
	{
		return SUPPORTED_LOCALES;
	}

	private int foodTypeToIndex(FoodType foodType)
	{
		return foodType.ordinal();
	}
	
	private int localeToIndex(Locale loc)
	{
		switch(loc.getLanguage())
		{
			case "de": return 0;
			case "en": return 1;
			case "fr": return 2;
			case "cs": return 3;
			case "zh": return 4;
			default: return -1;
		}
	}
}