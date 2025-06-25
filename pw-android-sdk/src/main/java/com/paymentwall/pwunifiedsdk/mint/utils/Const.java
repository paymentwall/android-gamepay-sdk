package com.paymentwall.pwunifiedsdk.mint.utils;

import java.util.Currency;

public class Const {
	public static final float RATIO = 0.95f;
	public static final String SDK_MESSAGE = "sdk_message";

	public static final String[] WIDGET = {};

	public static final class PW_LOCAL_REQUEST_CODE {
		public static final int NULL = -1;
		public static final int DEFAULT = 0;
		public static final int FLEXIBLE = 1;
	}

	public static final class PW_URL {
		public static final String PS = "https://api.paymentwall.com/api/ps/";
		public static final String SUBSCRIPTION = "https://api.paymentwall.com/api/subscription/";
		public static final String CART = "https://api.paymentwall.com/api/cart/";
	}
	
	public static final class API_TYPE {
		public static final String PS = "ps";
		public static final String SUBSCRIPTION = "subscription";
		public static final String CART = "cart";
	}

	public static final class RESPONSE_TYPE {

	}

	public static final class POST_OPTION {
		public static final int PINLESS = 1;
		public static final int PRICEINFO =2;
	}

	public static final class PW_LOCAL_WIDGET_TYPE {
		public static final int DEFAULT = 0;
		public static final int FLEXIBLE = 1;
	}

	public static final class GENDER {
		public static final int MALE = 1;
		public static final int FEMALE = 0;
	}

	public static final class REGEX {
		public static final String HEXADECIMAL32 = "^[0-9a-fA-F]{32}$";
		public static final String MINT_REGEX = "^[0-9]{16}$";

	}
	public static final class PW_LOCAL_DEFAULT {
		public static final String WIDGET = "m2";
	}
	public static final class P {

		public static final String BIRTHDAY = "birthday";
		public static final String COUNTRY_CODE = "country_code";
		public static final String DEFAULT_GOODSID = "default_goodsid";
		public static final String DISPLAY_GOODSID = "display_goodsid";
		public static final String EMAIL = "email";
		public static final String EVALUATION = "evaluation";
		public static final String FIRSTNAME = "firstname";
		public static final String HIDE_GOODSID = "hide_goodsid";
		public static final String KEY = "key";
		public static final String LANG = "lang";
		public static final String LASTNAME = "lastname";
		public static final String LOCATION_ADDRESS = "location[address]";
		public static final String LOCATION_CITY = "location[city]";
		public static final String LOCATION_COUNTRY = "location[country]";
		public static final String LOCATION_STATE = "location[state]";
		public static final String LOCATION_ZIP = "location[zip]";
		public static final String PINGBACK_URL = "pingback_url";
		public static final String PS = "ps";
		public static final String SEX = "sex";
		public static final String SIGN = "sign";
		public static final String SIGN_VERSION = "sign_version";
		public static final String SUCCESS_URL = "success_url";
		public static final String TS = "ts";
		public static final String UID = "uid";
		public static final String WIDGET = "widget";
		public static final String AG_EXTERNAL_ID = "ag_external_id";
		public static final String AG_NAME = "ag_name";
		public static final String AG_PERIOD_LENGTH = "ag_period_length";
		public static final String AG_PERIOD_TYPE = "ag_period_type";
		public static final String AG_POST_TRIAL_EXTERNAL_ID = "ag_post_trial_external_id";
		public static final String AG_POST_TRIAL_NAME = "ag_post_trial_name";
		public static final String AG_POST_TRIAL_PERIOD_LENGTH = "ag_post_trial_period_length";
		public static final String AG_POST_TRIAL_PERIOD_TYPE = "ag_post_trial_period_type";
		public static final String AG_PROMO = "ag_promo";
		public static final String AG_RECURRING = "ag_recurring";
		public static final String AG_TRIAL = "ag_trial";
		public static final String AG_TYPE = "ag_type";
		public static final String AMOUNT = "amount";
		public static final String CURRENCYCODE = "currencyCode";
		public static final String HIDE_POST_TRIAL_GOOD = "hide_post_trial_good";
		public static final String POST_TRIAL_AMOUNT = "post_trial_amount";
		public static final String POST_TRIAL_CURRENCYCODE = "post_trial_currencyCode";
		public static final String SHOW_POST_TRIAL_NON_RECURRING = "show_post_trial_non_recurring";
		public static final String SHOW_POST_TRIAL_RECURRING = "show_post_trial_recurring";
		public static final String SHOW_TRIAL_NON_RECURRING = "show_trial_non_recurring";
		public static final String SHOW_TRIAL_RECURRING = "show_trial_recurring";
		public static final String PRODUCT_NAME = "product_name";
		public static final String PRODUCT_ID = "product_id";
		public static final String PRICE_ID = "price_id";
		public static final String CURRENCY = "currency";
		public static final String COUNTRY = "country";
		public static final String MNC = "mnc";
		public static final String MCC = "mcc";

		public static String i(Integer indicator) {
			if (indicator != null) {
				return "[" + indicator + "]";
			} else {
				return null;
			}
		}
	}

	/*
	 * public static final class EVALUATION { public static final int YES = 1;
	 * public static final int NO = 0; }
	 */

	// ISO 639-1 languages
	public static final class LANGUAGE {
		public static final String ABKHAZ = "ab";
		public static final String AFAR = "aa";
		public static final String AFRIKAANS = "af";
		public static final String AKAN = "ak";
		public static final String ALBANIAN = "sq";
		public static final String AMHARIC = "am";
		public static final String ARABIC = "ar";
		public static final String ARAGONESE = "an";
		public static final String ARMENIAN = "hy";
		public static final String ASSAMESE = "as";
		public static final String AVARIC = "av";
		public static final String AVESTAN = "ae";
		public static final String AYMARA = "ay";
		public static final String AZERBAIJANI = "az";
		public static final String BAMBARA = "bm";
		public static final String BASHKIR = "ba";
		public static final String BASQUE = "eu";
		public static final String BELARUSIAN = "be";
		public static final String BENGALI = "bn";
		public static final String BIHARI = "bh";
		public static final String BISLAMA = "bi";
		public static final String BOSNIAN = "bs";
		public static final String BRETON = "br";
		public static final String BULGARIAN = "bg";
		public static final String BURMESE = "my";
		public static final String CATALAN = "ca";
		public static final String CHAMORRO = "ch";
		public static final String CHECHEN = "ce";
		public static final String CHICHEWA = "ny";
		public static final String CHINESE = "zh";
		public static final String CHUVASH = "cv";
		public static final String CORNISH = "kw";
		public static final String CORSICAN = "co";
		public static final String CREE = "cr";
		public static final String CROATIAN = "hr";
		public static final String CZECH = "cs";
		public static final String DANISH = "da";
		public static final String DIVEHI = "dv";
		public static final String DUTCH = "nl";
		public static final String DZONGKHA = "dz";
		public static final String ENGLISH = "en";
		public static final String ESPERANTO = "eo";
		public static final String ESTONIAN = "et";
		public static final String EWE = "ee";
		public static final String FAROESE = "fo";
		public static final String FIJIAN = "fj";
		public static final String FINNISH = "fi";
		public static final String FRENCH = "fr";
		public static final String FULA = "ff";
		public static final String GALICIAN = "gl";
		public static final String GEORGIAN = "ka";
		public static final String GERMAN = "de";
		public static final String GREEK = "el";
		public static final String GUARANI = "gn";
		public static final String GUJARATI = "gu";
		public static final String HAITIAN = "ht";
		public static final String HAUSA = "ha";
		public static final String HEBREW = "he";
		public static final String HERERO = "hz";
		public static final String HINDI = "hi";
		public static final String HIRI_MOTU = "ho";
		public static final String HUNGARIAN = "hu";
		public static final String INTERLINGUA = "ia";
		public static final String INDONESIAN = "id";
		public static final String INTERLINGUE = "ie";
		public static final String IRISH = "ga";
		public static final String IGBO = "ig";
		public static final String INUPIAQ = "ik";
		public static final String IDO = "io";
		public static final String ICELANDIC = "is";
		public static final String ITALIAN = "it";
		public static final String INUKTITUT = "iu";
		public static final String JAPANESE = "ja";
		public static final String JAVANESE = "jv";
		public static final String KALAALLISUT = "kl";
		public static final String KANNADA = "kn";
		public static final String KANURI = "kr";
		public static final String KASHMIRI = "ks";
		public static final String KAZAKH = "kk";
		public static final String KHMER = "km";
		public static final String KIKUYU = "ki";
		public static final String KINYARWANDA = "rw";
		public static final String KYRGYZ = "ky";
		public static final String KOMI = "kv";
		public static final String KONGO = "kg";
		public static final String KOREAN = "ko";
		public static final String KURDISH = "ku";
		public static final String KWANYAMA = "kj";
		public static final String LATIN = "la";
		public static final String LUXEMBOURGISH = "lb";
		public static final String GANDA = "lg";
		public static final String LIMBURGISH = "li";
		public static final String LINGALA = "ln";
		public static final String LAO = "lo";
		public static final String LITHUANIAN = "lt";
		public static final String LUBA_KATANGA = "lu";
		public static final String LATVIAN = "lv";
		public static final String MANX = "gv";
		public static final String MACEDONIAN = "mk";
		public static final String MALAGASY = "mg";
		public static final String MALAY = "ms";
		public static final String MALAYALAM = "ml";
		public static final String MALTESE = "mt";
		public static final String MAORI = "mi";
		public static final String MARATHI = "mr";
		public static final String MARSHALLESE = "mh";
		public static final String MONGOLIAN = "mn";
		public static final String NAURU = "na";
		public static final String NAVAJO = "nv";
		public static final String NORTHERN_NDEBELE = "nd";
		public static final String NEPALI = "ne";
		public static final String NDONGA = "ng";
		public static final String NORWEGIAN_BOKMAL = "nb";
		public static final String NORWEGIAN_NYNORSK = "nn";
		public static final String NORWEGIAN = "no";
		public static final String NUOSU = "ii";
		public static final String SOUTHERN_NDEBELE = "nr";
		public static final String OCCITAN = "oc";
		public static final String OJIBWE = "oj";
		public static final String OLD_CHURCH_SLAVONIC = "cu";
		public static final String OROMO = "om";
		public static final String ORIYA = "or";
		public static final String OSSETIAN = "os";
		public static final String PANJABI = "pa";
		public static final String PALI = "pi";
		public static final String PERSIAN = "fa";
		public static final String POLISH = "pl";
		public static final String PASHTO = "ps";
		public static final String PORTUGUESE = "pt";
		public static final String QUECHUA = "qu";
		public static final String ROMANSH = "rm";
		public static final String KIRUNDI = "rn";
		public static final String ROMANIAN = "ro";
		public static final String RUSSIAN = "ru";
		public static final String SANSKRIT = "sa";
		public static final String SARDINIAN = "sc";
		public static final String SINDHI = "sd";
		public static final String NORTHERN_SAMI = "se";
		public static final String SAMOAN = "sm";
		public static final String SANGO = "sg";
		public static final String SERBIAN = "sr";
		public static final String SCOTTISH_GAELIC = "gd";
		public static final String SHONA = "sn";
		public static final String SINHALA = "si";
		public static final String SLOVAK = "sk";
		public static final String SLOVENE = "sl";
		public static final String SOMALI = "so";
		public static final String SOUTHERN_SOTHO = "st";
		public static final String SPANISH = "es";
		public static final String SUNDANESE = "su";
		public static final String SWAHILI = "sw";
		public static final String SWATI = "ss";
		public static final String SWEDISH = "sv";
		public static final String TAMIL = "ta";
		public static final String TELUGU = "te";
		public static final String TAJIK = "tg";
		public static final String THAI = "th";
		public static final String TIGRINYA = "ti";
		public static final String TIBETAN_STANDARD = "bo";
		public static final String TURKMEN = "tk";
		public static final String TAGALOG = "tl";
		public static final String TSWANA = "tn";
		public static final String TONGA = "to";
		public static final String TURKISH = "tr";
		public static final String TSONGA = "ts";
		public static final String TATAR = "tt";
		public static final String TWI = "tw";
		public static final String TAHITIAN = "ty";
		public static final String UYGHUR = "ug";
		public static final String UKRAINIAN = "uk";
		public static final String URDU = "ur";
		public static final String UZBEK = "uz";
		public static final String VENDA = "ve";
		public static final String VIETNAMESE = "vi";
		public static final String VOLAPUK = "vo";
		public static final String WALLOON = "wa";
		public static final String WELSH = "cy";
		public static final String WOLOF = "wo";
		public static final String WESTERN_FRISIAN = "fy";
		public static final String XHOSA = "xh";
		public static final String YIDDISH = "yi";
		public static final String YORUBA = "yo";
		public static final String ZHUANG = "za";
		public static final String ZULU = "zu";
	}

	public static String[] CURRENCY_ARRAY = { "ADP", "AED", "AFA", "AFN",
			"ALL", "AMD", "ANG", "AOA", "ARS", "ATS", "AUD", "AWG", "AYM",
			"AZM", "AZN", "BAM", "BBD", "BDT", "BEF", "BGL", "BGN", "BHD",
			"BIF", "BMD", "BND", "BOB", "BOV", "BRL", "BSD", "BTN", "BWP",
			"BYB", "BYR", "BZD", "CAD", "CDF", "CHF", "CLF", "CLP", "CNY",
			"COP", "CRC", "CSD", "CUC", "CUP", "CVE", "CYP", "CZK", "DEM",
			"DJF", "DKK", "DOP", "DZD", "EEK", "EGP", "ERN", "ESP", "ETB",
			"EUR", "FIM", "FJD", "FKP", "FRF", "GBP", "GEL", "GHC", "GHS",
			"GIP", "GMD", "GNF", "GRD", "GTQ", "GWP", "GYD", "HKD", "HNL",
			"HRK", "HTG", "HUF", "IDR", "IEP", "ILS", "INR", "IQD", "IRR",
			"ISK", "ITL", "JMD", "JOD", "JPY", "KES", "KGS", "KHR", "KMF",
			"KPW", "KRW", "KWD", "KYD", "KZT", "LAK", "LBP", "LKR", "LRD",
			"LSL", "LTL", "LUF", "LVL", "LYD", "MAD", "MDL", "MGA", "MGF",
			"MKD", "MMK", "MNT", "MOP", "MRO", "MTL", "MUR", "MVR", "MWK",
			"MXN", "MXV", "MYR", "MZM", "MZN", "NAD", "NGN", "NIO", "NLG",
			"NOK", "NPR", "NZD", "OMR", "PAB", "PEN", "PGK", "PHP", "PKR",
			"PLN", "PTE", "PYG", "QAR", "ROL", "RON", "RSD", "RUB", "RUR",
			"RWF", "SAR", "SBD", "SCR", "SDD", "SDG", "SEK", "SGD", "SHP",
			"SIT", "SKK", "SLL", "SOS", "SRD", "SRG", "SSP", "STD", "SVC",
			"SYP", "SZL", "THB", "TJS", "TMM", "TMT", "TND", "TOP", "TPE",
			"TRL", "TRY", "TTD", "TWD", "TZS", "UAH", "UGX", "USD", "USN",
			"USS", "UYU", "UZS", "VEB", "VEF", "VND", "VUV", "WST", "XAF",
			"XAG", "XAU", "XBA", "XBB", "XBC", "XBD", "XCD", "XDR", "XFO",
			"XFU", "XOF", "XPD", "XPF", "XPT", "XSU", "XTS", "XUA", "XXX",
			"YER", "YUM", "ZAR", "ZMK", "ZMW", "ZWD", "ZWL", "ZWN", "ZWR", };

	public static class PW_CURRENCY {
		public static final String ANDORRAN_PESETA = "ADP";
		public static final String UNITED_ARAB_EMIRATES_DIRHAM = "AED";
		public static final String AFGHAN_AFGHANI_1927_2002 = "AFA";
		public static final String AFGHAN_AFGHANI = "AFN";
		public static final String ALBANIAN_LEK = "ALL";
		public static final String ARMENIAN_DRAM = "AMD";
		public static final String NETHERLANDS_ANTILLEAN_GUILDER = "ANG";
		public static final String ANGOLAN_KWANZA = "AOA";
		public static final String ARGENTINE_PESO = "ARS";
		public static final String AUSTRIAN_SCHILLING = "ATS";
		public static final String AUSTRALIAN_DOLLAR = "AUD";
		public static final String ARUBAN_FLORIN = "AWG";
		public static final String AYM = "AYM";
		public static final String AZERBAIJANI_MANAT_1993_2006 = "AZM";
		public static final String AZERBAIJANI_MANAT = "AZN";
		public static final String BOSNIA_HERZEGOVINA_CONVERTIBLE_MARK = "BAM";
		public static final String BARBADIAN_DOLLAR = "BBD";
		public static final String BANGLADESHI_TAKA = "BDT";
		public static final String BELGIAN_FRANC = "BEF";
		public static final String BULGARIAN_HARD_LEV = "BGL";
		public static final String BULGARIAN_LEV = "BGN";
		public static final String BAHRAINI_DINAR = "BHD";
		public static final String BURUNDIAN_FRANC = "BIF";
		public static final String BERMUDAN_DOLLAR = "BMD";
		public static final String BRUNEI_DOLLAR = "BND";
		public static final String BOLIVIAN_BOLIVIANO = "BOB";
		public static final String BOLIVIAN_MVDOL = "BOV";
		public static final String BRAZILIAN_REAL = "BRL";
		public static final String BAHAMIAN_DOLLAR = "BSD";
		public static final String BHUTANESE_NGULTRUM = "BTN";
		public static final String BOTSWANAN_PULA = "BWP";
		public static final String BELARUSIAN_NEW_RUBLE_1994_1999 = "BYB";
		public static final String BELARUSIAN_RUBLE = "BYR";
		public static final String BELIZE_DOLLAR = "BZD";
		public static final String CANADIAN_DOLLAR = "CAD";
		public static final String CONGOLESE_FRANC = "CDF";
		public static final String SWISS_FRANC = "CHF";
		public static final String CHILEAN_UNIT_OF_ACCOUNT_UF = "CLF";
		public static final String CHILEAN_PESO = "CLP";
		public static final String CHINESE_YUAN = "CNY";
		public static final String COLOMBIAN_PESO = "COP";
		public static final String COSTA_RICAN_COLON = "CRC";
		public static final String SERBIAN_DINAR_2002_2006 = "CSD";
		public static final String CUBAN_CONVERTIBLE_PESO = "CUC";
		public static final String CUBAN_PESO = "CUP";
		public static final String CAPE_VERDEAN_ESCUDO = "CVE";
		public static final String CYPRIOT_POUND = "CYP";
		public static final String CZECH_REPUBLIC_KORUNA = "CZK";
		public static final String GERMAN_MARK = "DEM";
		public static final String DJIBOUTIAN_FRANC = "DJF";
		public static final String DANISH_KRONE = "DKK";
		public static final String DOMINICAN_PESO = "DOP";
		public static final String ALGERIAN_DINAR = "DZD";
		public static final String ESTONIAN_KROON = "EEK";
		public static final String EGYPTIAN_POUND = "EGP";
		public static final String ERITREAN_NAKFA = "ERN";
		public static final String SPANISH_PESETA = "ESP";
		public static final String ETHIOPIAN_BIRR = "ETB";
		public static final String EURO = "EUR";
		public static final String FINNISH_MARKKA = "FIM";
		public static final String FIJIAN_DOLLAR = "FJD";
		public static final String FALKLAND_ISLANDS_POUND = "FKP";
		public static final String FRENCH_FRANC = "FRF";
		public static final String BRITISH_POUND_STERLING = "GBP";
		public static final String GEORGIAN_LARI = "GEL";
		public static final String GHANAIAN_CEDI_1979_2007 = "GHC";
		public static final String GHANAIAN_CEDI = "GHS";
		public static final String GIBRALTAR_POUND = "GIP";
		public static final String GAMBIAN_DALASI = "GMD";
		public static final String GUINEAN_FRANC = "GNF";
		public static final String GREEK_DRACHMA = "GRD";
		public static final String GUATEMALAN_QUETZAL = "GTQ";
		public static final String GUINEA_BISSAU_PESO = "GWP";
		public static final String GUYANAESE_DOLLAR = "GYD";
		public static final String HONG_KONG_DOLLAR = "HKD";
		public static final String HONDURAN_LEMPIRA = "HNL";
		public static final String CROATIAN_KUNA = "HRK";
		public static final String HAITIAN_GOURDE = "HTG";
		public static final String HUNGARIAN_FORINT = "HUF";
		public static final String INDONESIAN_RUPIAH = "IDR";
		public static final String IRISH_POUND = "IEP";
		public static final String ISRAELI_NEW_SHEQEL = "ILS";
		public static final String INDIAN_RUPEE = "INR";
		public static final String IRAQI_DINAR = "IQD";
		public static final String IRANIAN_RIAL = "IRR";
		public static final String ICELANDIC_KRONA = "ISK";
		public static final String ITALIAN_LIRA = "ITL";
		public static final String JAMAICAN_DOLLAR = "JMD";
		public static final String JORDANIAN_DINAR = "JOD";
		public static final String JAPANESE_YEN = "JPY";
		public static final String KENYAN_SHILLING = "KES";
		public static final String KYRGYSTANI_SOM = "KGS";
		public static final String CAMBODIAN_RIEL = "KHR";
		public static final String COMORIAN_FRANC = "KMF";
		public static final String NORTH_KOREAN_WON = "KPW";
		public static final String SOUTH_KOREAN_WON = "KRW";
		public static final String KUWAITI_DINAR = "KWD";
		public static final String CAYMAN_ISLANDS_DOLLAR = "KYD";
		public static final String KAZAKHSTANI_TENGE = "KZT";
		public static final String LAOTIAN_KIP = "LAK";
		public static final String LEBANESE_POUND = "LBP";
		public static final String SRI_LANKAN_RUPEE = "LKR";
		public static final String LIBERIAN_DOLLAR = "LRD";
		public static final String LESOTHO_LOTI = "LSL";
		public static final String LITHUANIAN_LITAS = "LTL";
		public static final String LUXEMBOURGIAN_FRANC = "LUF";
		public static final String LATVIAN_LATS = "LVL";
		public static final String LIBYAN_DINAR = "LYD";
		public static final String MOROCCAN_DIRHAM = "MAD";
		public static final String MOLDOVAN_LEU = "MDL";
		public static final String MALAGASY_ARIARY = "MGA";
		public static final String MALAGASY_FRANC = "MGF";
		public static final String MACEDONIAN_DENAR = "MKD";
		public static final String MYANMA_KYAT = "MMK";
		public static final String MONGOLIAN_TUGRIK = "MNT";
		public static final String MACANESE_PATACA = "MOP";
		public static final String MAURITANIAN_OUGUIYA = "MRO";
		public static final String MALTESE_LIRA = "MTL";
		public static final String MAURITIAN_RUPEE = "MUR";
		public static final String MALDIVIAN_RUFIYAA = "MVR";
		public static final String MALAWIAN_KWACHA = "MWK";
		public static final String MEXICAN_PESO = "MXN";
		public static final String MEXICAN_INVESTMENT_UNIT = "MXV";
		public static final String MALAYSIAN_RINGGIT = "MYR";
		public static final String MOZAMBICAN_METICAL_1980_2006 = "MZM";
		public static final String MOZAMBICAN_METICAL = "MZN";
		public static final String NAMIBIAN_DOLLAR = "NAD";
		public static final String NIGERIAN_NAIRA = "NGN";
		public static final String NICARAGUAN_CORDOBA = "NIO";
		public static final String DUTCH_GUILDER = "NLG";
		public static final String NORWEGIAN_KRONE = "NOK";
		public static final String NEPALESE_RUPEE = "NPR";
		public static final String NEW_ZEALAND_DOLLAR = "NZD";
		public static final String OMANI_RIAL = "OMR";
		public static final String PANAMANIAN_BALBOA = "PAB";
		public static final String PERUVIAN_NUEVO_SOL = "PEN";
		public static final String PAPUA_NEW_GUINEAN_KINA = "PGK";
		public static final String PHILIPPINE_PESO = "PHP";
		public static final String PAKISTANI_RUPEE = "PKR";
		public static final String POLISH_ZLOTY = "PLN";
		public static final String PORTUGUESE_ESCUDO = "PTE";
		public static final String PARAGUAYAN_GUARANI = "PYG";
		public static final String QATARI_RIAL = "QAR";
		public static final String ROMANIAN_LEU_1952_2006 = "ROL";
		public static final String ROMANIAN_LEU = "RON";
		public static final String SERBIAN_DINAR = "RSD";
		public static final String RUSSIAN_RUBLE = "RUB";
		public static final String RUSSIAN_RUBLE_1991_1998 = "RUR";
		public static final String RWANDAN_FRANC = "RWF";
		public static final String SAUDI_RIYAL = "SAR";
		public static final String SOLOMON_ISLANDS_DOLLAR = "SBD";
		public static final String SEYCHELLOIS_RUPEE = "SCR";
		public static final String SUDANESE_DINAR_1992_2007 = "SDD";
		public static final String SUDANESE_POUND = "SDG";
		public static final String SWEDISH_KRONA = "SEK";
		public static final String SINGAPORE_DOLLAR = "SGD";
		public static final String SAINT_HELENA_POUND = "SHP";
		public static final String SLOVENIAN_TOLAR = "SIT";
		public static final String SLOVAK_KORUNA = "SKK";
		public static final String SIERRA_LEONEAN_LEONE = "SLL";
		public static final String SOMALI_SHILLING = "SOS";
		public static final String SURINAMESE_DOLLAR = "SRD";
		public static final String SURINAMESE_GUILDER = "SRG";
		public static final String SOUTH_SUDANESE_POUND = "SSP";
		public static final String SAO_TOME_AND_PRINCIPE_DOBRA = "STD";
		public static final String SALVADORAN_COLON = "SVC";
		public static final String SYRIAN_POUND = "SYP";
		public static final String SWAZI_LILANGENI = "SZL";
		public static final String THAI_BAHT = "THB";
		public static final String TAJIKISTANI_SOMONI = "TJS";
		public static final String TURKMENISTANI_MANAT_1993_2009 = "TMM";
		public static final String TURKMENISTANI_MANAT = "TMT";
		public static final String TUNISIAN_DINAR = "TND";
		public static final String TONGAN_PA_ANGA = "TOP";
		public static final String TIMORESE_ESCUDO = "TPE";
		public static final String TURKISH_LIRA_1922_2005 = "TRL";
		public static final String TURKISH_LIRA = "TRY";
		public static final String TRINIDAD_AND_TOBAGO_DOLLAR = "TTD";
		public static final String NEW_TAIWAN_DOLLAR = "TWD";
		public static final String TANZANIAN_SHILLING = "TZS";
		public static final String UKRAINIAN_HRYVNIA = "UAH";
		public static final String UGANDAN_SHILLING = "UGX";
		public static final String US_DOLLAR = "USD";
		public static final String US_DOLLAR_NEXT_DAY = "USN";
		public static final String US_DOLLAR_SAME_DAY = "USS";
		public static final String URUGUAYAN_PESO = "UYU";
		public static final String UZBEKISTAN_SOM = "UZS";
		public static final String VENEZUELAN_BOLIVAR_1871_2008 = "VEB";
		public static final String VENEZUELAN_BOLIVAR = "VEF";
		public static final String VIETNAMESE_DONG = "VND";
		public static final String VANUATU_VATU = "VUV";
		public static final String SAMOAN_TALA = "WST";
		public static final String CFA_FRANC_BEAC = "XAF";
		public static final String SILVER = "XAG";
		public static final String GOLD = "XAU";
		public static final String EUROPEAN_COMPOSITE_UNIT = "XBA";
		public static final String EUROPEAN_MONETARY_UNIT = "XBB";
		public static final String EUROPEAN_UNIT_OF_ACCOUNT_XBC = "XBC";
		public static final String EUROPEAN_UNIT_OF_ACCOUNT_XBD = "XBD";
		public static final String EAST_CARIBBEAN_DOLLAR = "XCD";
		public static final String SPECIAL_DRAWING_RIGHTS = "XDR";
		public static final String FRENCH_GOLD_FRANC = "XFO";
		public static final String FRENCH_UIC_FRANC = "XFU";
		public static final String CFA_FRANC_BCEAO = "XOF";
		public static final String PALLADIUM = "XPD";
		public static final String CFP_FRANC = "XPF";
		public static final String PLATINUM = "XPT";
		public static final String SUCRE = "XSU";
		public static final String TESTING_CURRENCY_CODE = "XTS";
		public static final String ADB_UNIT_OF_ACCOUNT = "XUA";
		public static final String UNKNOWN_CURRENCY = "XXX";
		public static final String YEMENI_RIAL = "YER";
		public static final String YUGOSLAVIAN_NEW_DINAR_1994_2002 = "YUM";
		public static final String SOUTH_AFRICAN_RAND = "ZAR";
		public static final String ZAMBIAN_KWACHA = "ZMK";
		public static final String ZMW = "ZMW";
		public static final String ZIMBABWEAN_DOLLAR_1980_2008 = "ZWD";
		public static final String ZIMBABWEAN_DOLLAR_2009 = "ZWL";
		public static final String ZWN = "ZWN";
		public static final String ZIMBABWEAN_DOLLAR_2008 = "ZWR";
	}

	public static final class COLOR {
		public static final int PW_YELLOW = 0xffffca00;
		public static final int PW_YELLOW_DARK = 0xffff8a00;
	}

	public static class MINT {
		public static final class DIMENSION {
			public static final int PADDING_XLARGE = 48;
			public static final int PADDING_LARGE = 24;
			public static final int PADDING_NORMAL = 16;
			public static final int PADDING_NARROW = 8;
			public static final int PADDING_LIGHT = 4;
			public static final int MARGIN_NORMAL = 8;
			public static final int MARGIN_LARGE = 16;
			public static final int MARGIN_NARROW = 4;
			public static final int LOGO_HEIGHT_PORTRAIT = 64;
			public static final int LOGO_WIDTH_PORTRAIT = 64;
			public static final int LOGO_HEIGHT_LANDSCAPE = 56;
			public static final int LOGO_WIDTH_LANDSCAPE = 56;
			public static final int ERROR_ICON_HEIGHT = 20;
			public static final int ERROR_ICON_WIDTH = 20;
			public static final int RECOMMENDED_OUTER_SIZE = 48;
			public static final int RECOMMENDED_INNER_SIZE = 40;
			public static final int LOADING_ICON_WIDTH = 32;
			public static final int LOADING_ICON_HEIGHT = 32;
		}
		public static final class INTERNAL_ERROR_CODE {
			//ERROR_00x: internal exception
			//ERROR_01x: network exception
			//ERROR_02x: other exception
			public static final int ERROR_000 = 0x631c;
			public static final int ERROR_001 = 0x94f9;
			public static final int ERROR_002 = 0xa104;
			public static final int ERROR_003 = 0x6093;
			public static final int ERROR_004 = 0x9af3;
			public static final int ERROR_005 = 0x36ed;
			public static final int ERROR_006 = 0x369e;
			public static final int ERROR_007 = 0x3a8b;
			public static final int ERROR_008 = 0x8016;
			public static final int ERROR_009 = 0x4b51;
			public static final int ERROR_010 = 0xa218;
			public static final int ERROR_011 = 0xbf6d;
			public static final int ERROR_012 = 0xd720;
			public static final int ERROR_013 = 0x9bc8;
			public static final int ERROR_014 = 0xd706;
			public static final int ERROR_015 = 0x129f;
			public static final int ERROR_016 = 0x5416;
			public static final int ERROR_017 = 0x482d;
			public static final int ERROR_018 = 0x6411;
			public static final int ERROR_019 = 0x681b;
			public static final int ERROR_020 = 0x6863;
			public static final int ERROR_021 = 0x96ba;
			public static final int ERROR_022 = 0x0e89;
			public static final int ERROR_023 = 0xa288;
			public static final int ERROR_024 = 0x18d1;
			public static final int ERROR_025 = 0x689d;
			public static final int ERROR_026 = 0xaa16;
			public static final int ERROR_027 = 0xfece;
			public static final int ERROR_028 = 0xd6aa;
			public static final int ERROR_029 = 0x9029;



		}
		//It should be fine
		public static final int TIMEOUT = 20 * 1000;
		
		public static final int ERROR_DELAY = 10 * 1000;

		public static class PW_URL {
			public static final String MINT_TERM = "https://paymentwall.com/mint-terms/";
			public static final String PRIVACY_POLICY = "https://paymentwall.com/privacypolicy";
		}

		public static String MINT_AGREEMENT = "By clicking the button \"OK\" you confirm to have accepted Terms of Service and Privacy Policy";

		public static class PW_TEXTSIZE {
			/*public static final float SEMI_SMALL = 17 * RATIO;
			public static final float MEDIUM = 18 * RATIO;
			public static final float LARGE = 22 * RATIO;
			public static final float SMALL = 14 * RATIO;*/
			public static final float SEMI_SMALL = 17;
			public static final float MEDIUM = 18;
			public static final float LARGE = 22;
			public static final float SMALL = 14;
		}

		public static class PW_COLOR {
			public static final int PW_YELLOW = 0xffffca00;
			public static final int PW_YELLOW_DARK = 0xffff8a00;
			public static final int MINT_GREEN = 0xff5ba946;
			public static final int MINT_GREEN_LIGHT = 0xff8ac649;
			public static final int MINT_GREEN_TRANSPARENT = 0x803e7856;
			public static final int MINT_GREEN_LIGHT_TRANSPARENT = 0x808ac649;
			public static final int MINT_BUTTON = 0xff5ba946;
			public static final int MINT_BUTTON_HIGHLIGHT = 0xff66cb4b;
			public static final int TRANSPARENT = 0x00ffffff;
			public static final int BLACK_TRANSPARENT = 0x80000000;
			public static final int MINT_BACKGROUND = 0xfff8f8f8;
			public static final int MINT_TITLE = 0xff87c545;
			public static final int MINT_VALUE_LABEL = 0xff686967;
			public static final int WHITE = 0xffffffff;
			public static final int MINT_ERROR = 0xfff42121;
			public static final int HINT = 0xffbbbbbb;
			public static final int BLACK_TEXT = 0xff454545;
			public static final int TERMS_AND_PRIVACY_COLOR = 0xff808080;
			public static final int LOADING_BAR_COLOR = 0xff9d9d9d;
			public static final int LOADING_CONTAINER_BACKGROUND = 0xffeeeeee;
			public static final int EDITTEXT_BORDER_NORMAL = 0xffb2b2b2;
			public static final int EDITTEXT_BORDER_SELECTED = 0xff87c545;
		}

		public static class PW_CURRENCY {
			public static final String ANDORRAN_PESETA = "ADP";
			public static final String UNITED_ARAB_EMIRATES_DIRHAM = "AED";
			public static final String AFGHAN_AFGHANI_1927_2002 = "AFA";
			public static final String AFGHAN_AFGHANI = "AFN";
			public static final String ALBANIAN_LEK = "ALL";
			public static final String ARMENIAN_DRAM = "AMD";
			public static final String NETHERLANDS_ANTILLEAN_GUILDER = "ANG";
			public static final String ANGOLAN_KWANZA = "AOA";
			public static final String ARGENTINE_PESO = "ARS";
			public static final String AUSTRIAN_SCHILLING = "ATS";
			public static final String AUSTRALIAN_DOLLAR = "AUD";
			public static final String ARUBAN_FLORIN = "AWG";
			public static final String AYM = "AYM";
			public static final String AZERBAIJANI_MANAT_1993_2006 = "AZM";
			public static final String AZERBAIJANI_MANAT = "AZN";
			public static final String BOSNIA_HERZEGOVINA_CONVERTIBLE_MARK = "BAM";
			public static final String BARBADIAN_DOLLAR = "BBD";
			public static final String BANGLADESHI_TAKA = "BDT";
			public static final String BELGIAN_FRANC = "BEF";
			public static final String BULGARIAN_HARD_LEV = "BGL";
			public static final String BULGARIAN_LEV = "BGN";
			public static final String BAHRAINI_DINAR = "BHD";
			public static final String BURUNDIAN_FRANC = "BIF";
			public static final String BERMUDAN_DOLLAR = "BMD";
			public static final String BRUNEI_DOLLAR = "BND";
			public static final String BOLIVIAN_BOLIVIANO = "BOB";
			public static final String BOLIVIAN_MVDOL = "BOV";
			public static final String BRAZILIAN_REAL = "BRL";
			public static final String BAHAMIAN_DOLLAR = "BSD";
			public static final String BHUTANESE_NGULTRUM = "BTN";
			public static final String BOTSWANAN_PULA = "BWP";
			public static final String BELARUSIAN_NEW_RUBLE_1994_1999 = "BYB";
			public static final String BELARUSIAN_RUBLE = "BYR";
			public static final String BELIZE_DOLLAR = "BZD";
			public static final String CANADIAN_DOLLAR = "CAD";
			public static final String CONGOLESE_FRANC = "CDF";
			public static final String SWISS_FRANC = "CHF";
			public static final String CHILEAN_UNIT_OF_ACCOUNT_UF = "CLF";
			public static final String CHILEAN_PESO = "CLP";
			public static final String CHINESE_YUAN = "CNY";
			public static final String COLOMBIAN_PESO = "COP";
			public static final String COSTA_RICAN_COLON = "CRC";
			public static final String SERBIAN_DINAR_2002_2006 = "CSD";
			public static final String CUBAN_CONVERTIBLE_PESO = "CUC";
			public static final String CUBAN_PESO = "CUP";
			public static final String CAPE_VERDEAN_ESCUDO = "CVE";
			public static final String CYPRIOT_POUND = "CYP";
			public static final String CZECH_REPUBLIC_KORUNA = "CZK";
			public static final String GERMAN_MARK = "DEM";
			public static final String DJIBOUTIAN_FRANC = "DJF";
			public static final String DANISH_KRONE = "DKK";
			public static final String DOMINICAN_PESO = "DOP";
			public static final String ALGERIAN_DINAR = "DZD";
			public static final String ESTONIAN_KROON = "EEK";
			public static final String EGYPTIAN_POUND = "EGP";
			public static final String ERITREAN_NAKFA = "ERN";
			public static final String SPANISH_PESETA = "ESP";
			public static final String ETHIOPIAN_BIRR = "ETB";
			public static final String EURO = "EUR";
			public static final String FINNISH_MARKKA = "FIM";
			public static final String FIJIAN_DOLLAR = "FJD";
			public static final String FALKLAND_ISLANDS_POUND = "FKP";
			public static final String FRENCH_FRANC = "FRF";
			public static final String BRITISH_POUND_STERLING = "GBP";
			public static final String GEORGIAN_LARI = "GEL";
			public static final String GHANAIAN_CEDI_1979_2007 = "GHC";
			public static final String GHANAIAN_CEDI = "GHS";
			public static final String GIBRALTAR_POUND = "GIP";
			public static final String GAMBIAN_DALASI = "GMD";
			public static final String GUINEAN_FRANC = "GNF";
			public static final String GREEK_DRACHMA = "GRD";
			public static final String GUATEMALAN_QUETZAL = "GTQ";
			public static final String GUINEA_BISSAU_PESO = "GWP";
			public static final String GUYANAESE_DOLLAR = "GYD";
			public static final String HONG_KONG_DOLLAR = "HKD";
			public static final String HONDURAN_LEMPIRA = "HNL";
			public static final String CROATIAN_KUNA = "HRK";
			public static final String HAITIAN_GOURDE = "HTG";
			public static final String HUNGARIAN_FORINT = "HUF";
			public static final String INDONESIAN_RUPIAH = "IDR";
			public static final String IRISH_POUND = "IEP";
			public static final String ISRAELI_NEW_SHEQEL = "ILS";
			public static final String INDIAN_RUPEE = "INR";
			public static final String IRAQI_DINAR = "IQD";
			public static final String IRANIAN_RIAL = "IRR";
			public static final String ICELANDIC_KRONA = "ISK";
			public static final String ITALIAN_LIRA = "ITL";
			public static final String JAMAICAN_DOLLAR = "JMD";
			public static final String JORDANIAN_DINAR = "JOD";
			public static final String JAPANESE_YEN = "JPY";
			public static final String KENYAN_SHILLING = "KES";
			public static final String KYRGYSTANI_SOM = "KGS";
			public static final String CAMBODIAN_RIEL = "KHR";
			public static final String COMORIAN_FRANC = "KMF";
			public static final String NORTH_KOREAN_WON = "KPW";
			public static final String SOUTH_KOREAN_WON = "KRW";
			public static final String KUWAITI_DINAR = "KWD";
			public static final String CAYMAN_ISLANDS_DOLLAR = "KYD";
			public static final String KAZAKHSTANI_TENGE = "KZT";
			public static final String LAOTIAN_KIP = "LAK";
			public static final String LEBANESE_POUND = "LBP";
			public static final String SRI_LANKAN_RUPEE = "LKR";
			public static final String LIBERIAN_DOLLAR = "LRD";
			public static final String LESOTHO_LOTI = "LSL";
			public static final String LITHUANIAN_LITAS = "LTL";
			public static final String LUXEMBOURGIAN_FRANC = "LUF";
			public static final String LATVIAN_LATS = "LVL";
			public static final String LIBYAN_DINAR = "LYD";
			public static final String MOROCCAN_DIRHAM = "MAD";
			public static final String MOLDOVAN_LEU = "MDL";
			public static final String MALAGASY_ARIARY = "MGA";
			public static final String MALAGASY_FRANC = "MGF";
			public static final String MACEDONIAN_DENAR = "MKD";
			public static final String MYANMA_KYAT = "MMK";
			public static final String MONGOLIAN_TUGRIK = "MNT";
			public static final String MACANESE_PATACA = "MOP";
			public static final String MAURITANIAN_OUGUIYA = "MRO";
			public static final String MALTESE_LIRA = "MTL";
			public static final String MAURITIAN_RUPEE = "MUR";
			public static final String MALDIVIAN_RUFIYAA = "MVR";
			public static final String MALAWIAN_KWACHA = "MWK";
			public static final String MEXICAN_PESO = "MXN";
			public static final String MEXICAN_INVESTMENT_UNIT = "MXV";
			public static final String MALAYSIAN_RINGGIT = "MYR";
			public static final String MOZAMBICAN_METICAL_1980_2006 = "MZM";
			public static final String MOZAMBICAN_METICAL = "MZN";
			public static final String NAMIBIAN_DOLLAR = "NAD";
			public static final String NIGERIAN_NAIRA = "NGN";
			public static final String NICARAGUAN_CORDOBA = "NIO";
			public static final String DUTCH_GUILDER = "NLG";
			public static final String NORWEGIAN_KRONE = "NOK";
			public static final String NEPALESE_RUPEE = "NPR";
			public static final String NEW_ZEALAND_DOLLAR = "NZD";
			public static final String OMANI_RIAL = "OMR";
			public static final String PANAMANIAN_BALBOA = "PAB";
			public static final String PERUVIAN_NUEVO_SOL = "PEN";
			public static final String PAPUA_NEW_GUINEAN_KINA = "PGK";
			public static final String PHILIPPINE_PESO = "PHP";
			public static final String PAKISTANI_RUPEE = "PKR";
			public static final String POLISH_ZLOTY = "PLN";
			public static final String PORTUGUESE_ESCUDO = "PTE";
			public static final String PARAGUAYAN_GUARANI = "PYG";
			public static final String QATARI_RIAL = "QAR";
			public static final String ROMANIAN_LEU_1952_2006 = "ROL";
			public static final String ROMANIAN_LEU = "RON";
			public static final String SERBIAN_DINAR = "RSD";
			public static final String RUSSIAN_RUBLE = "RUB";
			public static final String RUSSIAN_RUBLE_1991_1998 = "RUR";
			public static final String RWANDAN_FRANC = "RWF";
			public static final String SAUDI_RIYAL = "SAR";
			public static final String SOLOMON_ISLANDS_DOLLAR = "SBD";
			public static final String SEYCHELLOIS_RUPEE = "SCR";
			public static final String SUDANESE_DINAR_1992_2007 = "SDD";
			public static final String SUDANESE_POUND = "SDG";
			public static final String SWEDISH_KRONA = "SEK";
			public static final String SINGAPORE_DOLLAR = "SGD";
			public static final String SAINT_HELENA_POUND = "SHP";
			public static final String SLOVENIAN_TOLAR = "SIT";
			public static final String SLOVAK_KORUNA = "SKK";
			public static final String SIERRA_LEONEAN_LEONE = "SLL";
			public static final String SOMALI_SHILLING = "SOS";
			public static final String SURINAMESE_DOLLAR = "SRD";
			public static final String SURINAMESE_GUILDER = "SRG";
			public static final String SOUTH_SUDANESE_POUND = "SSP";
			public static final String SAO_TOME_AND_PRINCIPE_DOBRA = "STD";
			public static final String SALVADORAN_COLON = "SVC";
			public static final String SYRIAN_POUND = "SYP";
			public static final String SWAZI_LILANGENI = "SZL";
			public static final String THAI_BAHT = "THB";
			public static final String TAJIKISTANI_SOMONI = "TJS";
			public static final String TURKMENISTANI_MANAT_1993_2009 = "TMM";
			public static final String TURKMENISTANI_MANAT = "TMT";
			public static final String TUNISIAN_DINAR = "TND";
			public static final String TONGAN_PA_ANGA = "TOP";
			public static final String TIMORESE_ESCUDO = "TPE";
			public static final String TURKISH_LIRA_1922_2005 = "TRL";
			public static final String TURKISH_LIRA = "TRY";
			public static final String TRINIDAD_AND_TOBAGO_DOLLAR = "TTD";
			public static final String NEW_TAIWAN_DOLLAR = "TWD";
			public static final String TANZANIAN_SHILLING = "TZS";
			public static final String UKRAINIAN_HRYVNIA = "UAH";
			public static final String UGANDAN_SHILLING = "UGX";
			public static final String US_DOLLAR = "USD";
			public static final String US_DOLLAR_NEXT_DAY = "USN";
			public static final String US_DOLLAR_SAME_DAY = "USS";
			public static final String URUGUAYAN_PESO = "UYU";
			public static final String UZBEKISTAN_SOM = "UZS";
			public static final String VENEZUELAN_BOLIVAR_1871_2008 = "VEB";
			public static final String VENEZUELAN_BOLIVAR = "VEF";
			public static final String VIETNAMESE_DONG = "VND";
			public static final String VANUATU_VATU = "VUV";
			public static final String SAMOAN_TALA = "WST";
			public static final String CFA_FRANC_BEAC = "XAF";
			public static final String SILVER = "XAG";
			public static final String GOLD = "XAU";
			public static final String EUROPEAN_COMPOSITE_UNIT = "XBA";
			public static final String EUROPEAN_MONETARY_UNIT = "XBB";
			public static final String EUROPEAN_UNIT_OF_ACCOUNT_XBC = "XBC";
			public static final String EUROPEAN_UNIT_OF_ACCOUNT_XBD = "XBD";
			public static final String EAST_CARIBBEAN_DOLLAR = "XCD";
			public static final String SPECIAL_DRAWING_RIGHTS = "XDR";
			public static final String FRENCH_GOLD_FRANC = "XFO";
			public static final String FRENCH_UIC_FRANC = "XFU";
			public static final String CFA_FRANC_BCEAO = "XOF";
			public static final String PALLADIUM = "XPD";
			public static final String CFP_FRANC = "XPF";
			public static final String PLATINUM = "XPT";
			public static final String SUCRE = "XSU";
			public static final String TESTING_CURRENCY_CODE = "XTS";
			public static final String ADB_UNIT_OF_ACCOUNT = "XUA";
			public static final String UNKNOWN_CURRENCY = "XXX";
			public static final String YEMENI_RIAL = "YER";
			public static final String YUGOSLAVIAN_NEW_DINAR_1994_2002 = "YUM";
			public static final String SOUTH_AFRICAN_RAND = "ZAR";
			public static final String ZAMBIAN_KWACHA = "ZMK";
			public static final String ZMW = "ZMW";
			public static final String ZIMBABWEAN_DOLLAR_1980_2008 = "ZWD";
			public static final String ZIMBABWEAN_DOLLAR_2009 = "ZWL";
			public static final String ZWN = "ZWN";
			public static final String ZIMBABWEAN_DOLLAR_2008 = "ZWR";
		}

		public static String[] CURRENCY_ARRAY = { "ADP", "AED", "AFA", "AFN",
				"ALL", "AMD", "ANG", "AOA", "ARS", "ATS", "AUD", "AWG", "AYM",
				"AZM", "AZN", "BAM", "BBD", "BDT", "BEF", "BGL", "BGN", "BHD",
				"BIF", "BMD", "BND", "BOB", "BOV", "BRL", "BSD", "BTN", "BWP",
				"BYB", "BYR", "BZD", "CAD", "CDF", "CHF", "CLF", "CLP", "CNY",
				"COP", "CRC", "CSD", "CUC", "CUP", "CVE", "CYP", "CZK", "DEM",
				"DJF", "DKK", "DOP", "DZD", "EEK", "EGP", "ERN", "ESP", "ETB",
				"EUR", "FIM", "FJD", "FKP", "FRF", "GBP", "GEL", "GHC", "GHS",
				"GIP", "GMD", "GNF", "GRD", "GTQ", "GWP", "GYD", "HKD", "HNL",
				"HRK", "HTG", "HUF", "IDR", "IEP", "ILS", "INR", "IQD", "IRR",
				"ISK", "ITL", "JMD", "JOD", "JPY", "KES", "KGS", "KHR", "KMF",
				"KPW", "KRW", "KWD", "KYD", "KZT", "LAK", "LBP", "LKR", "LRD",
				"LSL", "LTL", "LUF", "LVL", "LYD", "MAD", "MDL", "MGA", "MGF",
				"MKD", "MMK", "MNT", "MOP", "MRO", "MTL", "MUR", "MVR", "MWK",
				"MXN", "MXV", "MYR", "MZM", "MZN", "NAD", "NGN", "NIO", "NLG",
				"NOK", "NPR", "NZD", "OMR", "PAB", "PEN", "PGK", "PHP", "PKR",
				"PLN", "PTE", "PYG", "QAR", "ROL", "RON", "RSD", "RUB", "RUR",
				"RWF", "SAR", "SBD", "SCR", "SDD", "SDG", "SEK", "SGD", "SHP",
				"SIT", "SKK", "SLL", "SOS", "SRD", "SRG", "SSP", "STD", "SVC",
				"SYP", "SZL", "THB", "TJS", "TMM", "TMT", "TND", "TOP", "TPE",
				"TRL", "TRY", "TTD", "TWD", "TZS", "UAH", "UGX", "USD", "USN",
				"USS", "UYU", "UZS", "VEB", "VEF", "VND", "VUV", "WST", "XAF",
				"XAG", "XAU", "XBA", "XBB", "XBC", "XBD", "XCD", "XDR", "XFO",
				"XFU", "XOF", "XPD", "XPF", "XPT", "XSU", "XTS", "XUA", "XXX",
				"YER", "YUM", "ZAR", "ZMK", "ZMW", "ZWD", "ZWL", "ZWN", "ZWR", };

		public static boolean isCurrency(String currency)
				throws IllegalArgumentException {
			Currency.getInstance(currency);
			return true;
		}

		public static final class REGEX {
			public static final String PIN = "^[0-9]{16}$";
		}

		public static final class REQUEST_TYPE {
			public static final int WITH_PIN = 1;
			public static final int WITHOUT_PIN = -1;
			public static final int ERROR = 0;
		}

		public static final class RESPONSE_STATUS {
			public static final int SUCCESSFUL = 0x003e7856;
			public static final int ERROR_NETWORK = 0x3d7ebc6d;
			public static final int ERROR_SERVER = 0x6ce52c42;
			public static final int ERROR_BAD_MESSAGE = 0x4dec450f;
			public static final int ERROR_EXPIRED_PIN = 0x3e419d36;
			public static final int ERROR_INVALID_PIN = 0x3c35d089;
			public static final int ERROR_MANY_PINS = 0x4526a0c3;
			public static final int ERROR_SSL = 0x79378d59;
			public static final int CANCELLED = 0x7ba146b0;
			public static final int FAILED = 0x527919a8;
			public static final int ERROR_PAYMENT = 0x5b2982f1;
			// public static final int
		}

		public static final class PARAMETER {
			public static final String REQUEST = "request_message";
			public static final String WITH_PIN = "with_pin";
			public static final String RESPONSE = "response_message";
		}
	}
	
	public static final String NTP_SERVER = "pool.ntp.org";
	public static final int NTP_TIMEOUT = 10*1000;
}
