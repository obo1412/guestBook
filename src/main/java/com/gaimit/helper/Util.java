package com.gaimit.helper;

/**
 * κΈ°λ³Έ? ?Έ κ³΅ν΅ κΈ°λ₯?€? λ¬Άμ΄ ??? ?΄??€
 */
public class Util {
	// ----------- ?±κΈ??€ κ°μ²΄ ??± ?? -----------
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
	// ----------- ?±κΈ??€ κ°μ²΄ ??± ? -----------

	/**
	 * λ²μλ₯? κ°λ ??€κ°μ ??±??¬ λ¦¬ν΄?? λ©μ? 
	 * @param min - λ²μ ???? μ΅μκ°?
	 * @param max - λ²μ ???? μ΅λ?κ°?
	 * @return min~max ???? ??€κ°?
	 */
	public int random(int min, int max) {
		int num = (int) ((Math.random() * (max - min + 1)) + min);
		return num;
	}

	/**
	 * ??€? λΉλ?λ²νΈλ₯? ??±??¬ λ¦¬ν΄??€.
	 * @return String
	 */
	public String getRandomPassword() {
		// λ¦¬ν΄?  λ¬Έμ?΄
		String password = "";

		// A~Z, a~z, 1~0 
		String words = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
		// κΈ??κΈΈμ΄
		int words_len = words.length();

		for (int i = 0; i < 8; i++) {
			// ??€? ?μΉμ? ? κΈ??λ₯? μΆμΆ??€.
			int random = random(0, words_len - 1);
			String c = words.substring(random, random + 1);

			// μΆμΆ? κΈ??λ₯? λ―Έλ¦¬ μ€?λΉν λ³??? μΆκ???€.
			password += c;
		}

		return password;
	}
	
	public String getRandomPassword(int num) {
		// λ¦¬ν΄?  λ¬Έμ?΄
		String password = "";

		// A~Z, 1~0 ?λ¬Έμ λΊ??€. ?λ¬Έμ ?£κ³? ?Ά?Όλ©? ?μ²λΌ ?€ ? ?΄?£?Όλ©? ??€. 
		String words = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		// κΈ??κΈΈμ΄
		int words_len = words.length();

		for (int i = 0; i < num; i++) {
			// ??€? ?μΉμ? ? κΈ??λ₯? μΆμΆ??€.
			int random = random(0, words_len - 1);
			String c = words.substring(random, random + 1);

			// μΆμΆ? κΈ??λ₯? λ―Έλ¦¬ μ€?λΉν λ³??? μΆκ???€.
			password += c;
		}

		return password;
	}
	
	/**
	 * ?«?λ§? μΆμΆ?΄? intλ‘? λ³??
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
	 * λ¬Έμλ§? μΆμΆ?κΈ?
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
