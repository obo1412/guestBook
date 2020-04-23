package com.gaimit.helper;

/**
 * 기본?��?�� 공통 기능?��?�� 묶어 ?��?? ?��?��?��
 */
public class Util {
	// ----------- ?���??�� 객체 ?��?�� ?��?�� -----------
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
	// ----------- ?���??�� 객체 ?��?�� ?�� -----------

	/**
	 * 범위�? 갖는 ?��?��값을 ?��?��?��?�� 리턴?��?�� 메서?�� 
	 * @param min - 범위 ?��?��?��?�� 최소�?
	 * @param max - 범위 ?��?��?��?�� 최�?�?
	 * @return min~max ?��?��?��?�� ?��?���?
	 */
	public int random(int min, int max) {
		int num = (int) ((Math.random() * (max - min + 1)) + min);
		return num;
	}

	/**
	 * ?��?��?�� 비�?번호�? ?��?��?��?�� 리턴?��?��.
	 * @return String
	 */
	public String getRandomPassword() {
		// 리턴?�� 문자?��
		String password = "";

		// A~Z, a~z, 1~0 
		String words = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
		// �??��길이
		int words_len = words.length();

		for (int i = 0; i < 8; i++) {
			// ?��?��?�� ?��치에?�� ?�� �??���? 추출?��?��.
			int random = random(0, words_len - 1);
			String c = words.substring(random, random + 1);

			// 추출?�� �??���? 미리 �?비한 �??��?�� 추�??��?��.
			password += c;
		}

		return password;
	}
	
	public String getRandomPassword(int num) {
		// 리턴?�� 문자?��
		String password = "";

		// A~Z, 1~0 ?��문자 �??��. ?��문자 ?���? ?��?���? ?��처럼 ?�� ?��?��?��?���? ?��?��. 
		String words = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		// �??��길이
		int words_len = words.length();

		for (int i = 0; i < num; i++) {
			// ?��?��?�� ?��치에?�� ?�� �??���? 추출?��?��.
			int random = random(0, words_len - 1);
			String c = words.substring(random, random + 1);

			// 추출?�� �??���? 미리 �?비한 �??��?�� 추�??��?��.
			password += c;
		}

		return password;
	}
	
	/**
	 * ?��?���? 추출?��?�� int�? �??��
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
	 * 문자�? 추출?���?
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
