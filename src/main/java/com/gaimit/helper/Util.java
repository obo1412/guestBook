package com.gaimit.helper;

/**
 * ê¸°ë³¸? ?¸ ê³µí†µ ê¸°ëŠ¥?“¤?„ ë¬¶ì–´ ?†“?? ?´?˜?Š¤
 */
public class Util {
	// ----------- ?‹±ê¸??†¤ ê°ì²´ ?ƒ?„± ?‹œ?‘ -----------
	private static Util current = null;

	public static Util getInstance() {
		if (current == null) {
			current = new Util();
		}
		return current;
	}

	public static void freeInstance() {
		current = null;
	}

	private Util() {
		super();
	}
	// ----------- ?‹±ê¸??†¤ ê°ì²´ ?ƒ?„± ? -----------

	/**
	 * ë²”ìœ„ë¥? ê°–ëŠ” ?œ?¤ê°’ì„ ?ƒ?„±?•˜?—¬ ë¦¬í„´?•˜?Š” ë©”ì„œ?“œ 
	 * @param min - ë²”ìœ„ ?•ˆ?—?„œ?˜ ìµœì†Œê°?
	 * @param max - ë²”ìœ„ ?•ˆ?—?„œ?˜ ìµœë?ê°?
	 * @return min~max ?•ˆ?—?„œ?˜ ?œ?¤ê°?
	 */
	public int random(int min, int max) {
		int num = (int) ((Math.random() * (max - min + 1)) + min);
		return num;
	}

	/**
	 * ?œ?¤?•œ ë¹„ë?ë²ˆí˜¸ë¥? ?ƒ?„±?•˜?—¬ ë¦¬í„´?•œ?‹¤.
	 * @return String
	 */
	public String getRandomPassword() {
		// ë¦¬í„´?•  ë¬¸ì?—´
		String password = "";

		// A~Z, a~z, 1~0 
		String words = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
		// ê¸??ê¸¸ì´
		int words_len = words.length();

		for (int i = 0; i < 8; i++) {
			// ?œ?¤?•œ ?œ„ì¹˜ì—?„œ ?•œ ê¸??ë¥? ì¶”ì¶œ?•œ?‹¤.
			int random = random(0, words_len - 1);
			String c = words.substring(random, random + 1);

			// ì¶”ì¶œ?•œ ê¸??ë¥? ë¯¸ë¦¬ ì¤?ë¹„í•œ ë³??ˆ˜?— ì¶”ê??•œ?‹¤.
			password += c;
		}

		return password;
	}
	
	public String getRandomPassword(int num) {
		// ë¦¬í„´?•  ë¬¸ì?—´
		String password = "";

		// A~Z, 1~0 ?†Œë¬¸ì ëº??‹¤. ?†Œë¬¸ì ?„£ê³? ?‹¶?œ¼ë©? ?œ„ì²˜ëŸ¼ ?‹¤ ? ?–´?„£?œ¼ë©? ?œ?‹¤. 
		String words = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		// ê¸??ê¸¸ì´
		int words_len = words.length();

		for (int i = 0; i < num; i++) {
			// ?œ?¤?•œ ?œ„ì¹˜ì—?„œ ?•œ ê¸??ë¥? ì¶”ì¶œ?•œ?‹¤.
			int random = random(0, words_len - 1);
			String c = words.substring(random, random + 1);

			// ì¶”ì¶œ?•œ ê¸??ë¥? ë¯¸ë¦¬ ì¤?ë¹„í•œ ë³??ˆ˜?— ì¶”ê??•œ?‹¤.
			password += c;
		}

		return password;
	}
	
	/**
	 * ?ˆ«?ë§? ì¶”ì¶œ?•´?„œ intë¡? ë³??™˜
	 * @param str
	 * @return
	 */
	public int numExtract(String str) {
		int result = 0;
		if(!str.equals("")||str!=null) {
			result = Integer.parseInt(str.replaceAll("[^0-9]", ""));
		}
		return result;
	}
	
	/**
	 * ë¬¸ìë§? ì¶”ì¶œ?•˜ê¸?
	 * @param str
	 * @return
	 */
	public String strExtract(String str) {
		String result ="";
		if(!str.equals("")||str!=null) {
			result = str.replaceAll("[0-9]", "");
		}
		return result;
	}
	
	
	public String makeStrLength(int len, String str, int i) {
		String result = null;
		String k = "0";
		for(int j=0; j<len; j++) {
			result = str + k + i;
			k += "0";
			if(result.length() == len) {
				break;
			}
		}
		return result;
	}
}
