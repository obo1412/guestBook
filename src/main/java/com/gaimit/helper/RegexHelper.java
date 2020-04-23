package com.gaimit.helper;

import java.util.regex.Pattern;

public class RegexHelper {
	// ----------- ½Ì±ÛÅæ °´Ã¼ »ı¼º ½ÃÀÛ ----------
	private static RegexHelper current = null;

	public static RegexHelper getInstance() {
		if (current == null) {
			current = new RegexHelper();
		}
		return current;
	}

	public static void freeInstance() {
		current = null;
	}

	private RegexHelper() {
		super();
	}
	// ----------- ½Ì±ÛÅæ °´Ã¼ »ı¼º ³¡ ----------
	
	/**
	 * ÁÖ¾îÁø ¹®ÀÚ¿­ÀÌ °ø¹éÀÌ°Å³ª nullÀÎÁö¸¦ °Ë»ç
	 * 
	 * @param str
	 *            - °Ë»çÇÒ ¹®ÀÚ¿­
	 * @return boolean - °ø¹é,nullÀÌ ¾Æ´Ò °æ¿ì true ¸®ÅÏ
	 */
	public boolean isValue(String str) {
		boolean result = false;
		if (str != null) {
			result = !str.trim().equals("");
		}
		return result;
	}

	/**
	 * ¼ıÀÚ ¸ğ¾ç¿¡ ´ëÇÑ Çü½Ä °Ë»ç
	 * 
	 * @param str
	 *            - °Ë»çÇÒ ¹®ÀÚ¿­
	 * @return boolean - Çü½Ä¿¡ ¸ÂÀ» °æ¿ì true, ¸ÂÁö ¾ÊÀ» °æ¿ì false
	 */
	public boolean isNum(String str) {
		boolean result = false;
		if (isValue(str)) {
			result = Pattern.matches("^[0-9]*$", str);
		}
		return result;
	}

	/**
	 * ¿µ¹®À¸·Î¸¸ ±¸¼ºµÇ¾ú´ÂÁö¿¡ ´ëÇÑ Çü½Ä °Ë»ç
	 * 
	 * @param str
	 *            - °Ë»çÇÒ ¹®ÀÚ¿­
	 * @return boolean - Çü½Ä¿¡ ¸ÂÀ» °æ¿ì true, ¸ÂÁö ¾ÊÀ» °æ¿ì false
	 */
	public boolean isEng(String str) {
		boolean result = false;
		if (isValue(str)) {
			result = Pattern.matches("^[a-zA-Z]*$", str);
		}
		return result;
	}

	/**
	 * ÇÑ±Û·Î¸¸ ±¸¼ºµÇ¾ú´ÂÁö¿¡ ´ëÇÑ Çü½Ä °Ë»ç
	 * 
	 * @param str
	 *            - °Ë»çÇÒ ¹®ÀÚ¿­
	 * @return boolean - Çü½Ä¿¡ ¸ÂÀ» °æ¿ì true, ¸ÂÁö ¾ÊÀ» °æ¿ì false
	 */
	public boolean isKor(String str) {
		boolean result = false;
		if (isValue(str)) {
			result = Pattern.matches("^[¤¡-¤¾°¡-ÆR]*$", str);
		}
		return result;
	}

	/**
	 * ¿µ¹®°ú ¼ıÀÚ·Î¸¸ ±¸¼ºµÇ¾ú´ÂÁö¿¡ ´ëÇÑ Çü½Ä °Ë»ç
	 * 
	 * @param str
	 *            - °Ë»çÇÒ ¹®ÀÚ¿­
	 * @return boolean - Çü½Ä¿¡ ¸ÂÀ» °æ¿ì true, ¸ÂÁö ¾ÊÀ» °æ¿ì false
	 */
	public boolean isEngNum(String str) {
		boolean result = false;
		if (isValue(str)) {
			result = Pattern.matches("^[a-zA-Z0-9]*$", str);
		}
		return result;
	}

	/**
	 * ÇÑ±Û°ú ¼ıÀÚ·Î¸¸ ±¸¼ºµÇ¾ú´ÂÁö¿¡ ´ëÇÑ Çü½Ä °Ë»ç
	 * 
	 * @param str
	 *            - °Ë»çÇÒ ¹®ÀÚ¿­
	 * @return boolean - Çü½Ä¿¡ ¸ÂÀ» °æ¿ì true, ¸ÂÁö ¾ÊÀ» °æ¿ì false
	 */
	public boolean isKorNum(String str) {
		boolean result = false;
		if (isValue(str)) {
			result = Pattern.matches("^[¤¡-¤¾°¡-ÆR0-9]*$", str);
		}
		return result;
	}

	/**
	 * ÀÌ¸ŞÀÏ Çü½ÄÀÎÁö¿¡ ´ëÇÑ °Ë»ç.
	 * 
	 * @param str
	 *            - °Ë»çÇÒ ¹®ÀÚ¿­
	 * @return boolean - Çü½Ä¿¡ ¸ÂÀ» °æ¿ì true, ¸ÂÁö ¾ÊÀ» °æ¿ì false
	 */
	public boolean isEmail(String str) {
		boolean result = false;
		if (isValue(str)) {
			result = Pattern.matches("[0-9a-zA-Z]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", str);
		}
		return result;
	}

	/**
	 * "-"¾øÀÌ ÇÚµåÆù¹øÈ£ÀÎÁö¿¡ ´ëÇÑ Çü½Ä°Ë»ç.
	 * 
	 * @param str
	 *            - °Ë»çÇÒ ¹®ÀÚ¿­
	 * @return boolean - Çü½Ä¿¡ ¸ÂÀ» °æ¿ì true, ¸ÂÁö ¾ÊÀ» °æ¿ì false
	 */
	public boolean isCellPhone(String str) {
		boolean result = false;
		if (isValue(str)) {
			result = Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", str);
		}
		return result;
	}

	/**
	 * "-"¾øÀÌ ÀüÈ­¹øÈ£ÀÎÁö¿¡ ´ëÇÑ Çü½Ä°Ë»ç.
	 * 
	 * @param str
	 *            - °Ë»çÇÒ ¹®ÀÚ¿­
	 * @return boolean - Çü½Ä¿¡ ¸ÂÀ» °æ¿ì true, ¸ÂÁö ¾ÊÀ» °æ¿ì false
	 */
	public boolean isTel(String str) {
		boolean result = false;
		if (isValue(str)) {
			result = Pattern.matches("^\\d{2,3}\\d{3,4}\\d{4}$", str);
		}
		return result;
	}

	/**
	 * "-"¾øÀÌ ÁÖ¹Î¹øÈ£¿¡ ´ëÇÑ Çü½Ä°Ë»ç
	 * 
	 * @param str
	 *            - °Ë»çÇÒ ¹®ÀÚ¿­
	 * @return boolean - Çü½Ä¿¡ ¸ÂÀ» °æ¿ì true, ¸ÂÁö ¾ÊÀ» °æ¿ì false
	 */
	public boolean isJumin(String str) {
		boolean result = false;
		if (isValue(str)) {
			result = Pattern.matches("^\\d{6}[1-4]\\d{6}", str);
		}
		return result;
	}
	
	
}
