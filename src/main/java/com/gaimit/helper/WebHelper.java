package com.gaimit.helper;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractView;

import com.fasterxml.jackson.databind.ObjectMapper;

public class WebHelper {
	/** 쿠키?��?�� ?��?��?�� ?��메인 */
	private static final String DOMAIN = "localhost";

	/** JSP?�� request ?��?�� 객체 */
	// --> import javax.servlet.http.HttpServletRequest;
	private HttpServletRequest request;

	/** JSP?�� response ?��?�� 객체 */
	// --> import javax.servlet.http.HttpServletResponse;
	private HttpServletResponse response;

	/** JSP?�� out ?��?�� 객체 */
	// --> import java.io.PrintWriter;
	private PrintWriter out;

	/** JSP?�� session ?��?�� 객체 */
	// --> import javax.servlet.http.HttpSession;
	private HttpSession session;

	/**
	 * WebHelper 기능?�� 초기?�� ?��?��. Spring?�� ?��공하?�� ServletRequestAttributes 객체�? ?��?��?��
	 * request, response객체�? 직접 ?��?��?�� ?�� ?��?��.
	 */
	public void init() {

		/** JSP ?��?��객체�? ?���? ?��?�� Spring?�� 객체�? ?��?��?�� ?��?��객체 ?��?��?���? */
		// --> import
		// org.springframework.web.context.request.RequestContextHolder;
		// --> import
		// org.springframework.web.context.request.ServletRequestAttributes;
		ServletRequestAttributes requestAttr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

		// request?? response 객체�? 추출?��?��.
		this.request = requestAttr.getRequest();
		this.response = requestAttr.getResponse();

		// ?��?��객체 ?��?��?���?
		this.session = request.getSession();

		// ?��?���? ?��?�� ?��?�� ?��?��?�� ?���??��?�� ?���? ?��?�� (�?)
		// --> 24?���?
		this.session.setMaxInactiveInterval(60 * 60 * 24);

		/** ?��?��객체 초기?�� -> utf-8 ?��?��, out객체 ?��?�� */
		try {
			// ?��코딩 ?��?��?���?
			this.request.setCharacterEncoding("utf-8");
			this.response.setCharacterEncoding("utf-8");
			// out객체 ?��?��?���?
			this.out = response.getWriter();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 메시�? ?��?�� ?��, ?��?���?�? �??��?�� 곳으�? ?��?��?��?��.
	 * @param url - ?��?��?�� ?��?���??�� URL, Null?�� 경우 ?��?��?��?���?�? ?��?��
	 * @param msg - ?��면에 ?��?��?�� 메시�?. Null?�� 경우 ?��?�� ?��?��
	 */
	// --> import org.springframework.web.servlet.ModelAndView;
	public ModelAndView redirect(String url, String msg) {
		// �??��?�� View�? 만들�? ?��?�� HTML ?���? 구성
		String html = "<!doctype html>";
		html += "<html>";
		html += "<head>";
		html += "<meta charset='utf-8'>";

		// 메시�? ?��?��
		if (msg != null) {
			html += "<script type='text/javascript'>alert('" + msg + "');</script>";
		}

		// ?��?���? ?��?��
		if (url != null) {
			html += "<meta http-equiv='refresh' content='0; url=" + url + "' />";
		} else {
			html += "<script type='text/javascript'>history.back();</script>";
		}

		html += "</head>";
		html += "<body></body>";
		html += "</html>";

		// 구성?�� HTML?�� 출력?��?��.
		// out.print(html);

		// ?��명클?��?�� 방식?? ?��?���? ?��?��?�� ?�� ?��?���?�?, HTML?��그�?? ?��?��?�� 복사
		final String page_content = html;

		/** �??��?�� View�? ?���? ?��?��?�� 방식?���? ?��?��?��?�� 리턴 */
		// --> import org.springframework.web.servlet.View;
		// --> import org.springframework.web.servlet.view.AbstractView;
		View view = new AbstractView() {
			@Override
			protected void renderMergedOutputModel(Map<String, Object> map, 
					HttpServletRequest request,HttpServletResponse response) throws Exception {
				out.println(page_content);
				out.flush();
			}
		};

		// �??��?�� 뷰�?? 리턴?��?��.
		return new ModelAndView(view);
	}

	/**
	 * ?��?��미터�? ?��?��받아?�� 리턴?��?��.
	 * 
	 * @param fieldName
	 *            - ?��?��미터 ?���?
	 * @param defaultValue
	 *            - 값이 ?��?�� 경우 ?��?��?�� 기본�?
	 * @return String
	 */
	public String getString(String fieldName, String defaultValue) {
		// 리턴?�� ?��?�� 값을 ?�� 번째 ?��?��미터(기본�?)�? ?��?��?�� ?��?��.
		String result = defaultValue;
		// GET,POST ?��?��미터�? 받는?��.
		String param = this.request.getParameter(fieldName);

		if (param != null) { // 값이 null?�� ?��?��?���??
			param = param.trim(); // ?��?�� 공백?�� ?��거한?��.
			if (!param.equals("")) { // 공백?���? 결과�? �? 문자?��?�� ?��?��?���??
				result = param; // 리턴?�� ?��?��?�� �?비한 �??��?�� ?��?��?�� 값을 복사?��?��.
			}
		}

		// 값을 리턴. param값이 존재?���? ?��?�� 경우 미리 �?비한 기본값이 그�?�? 리턴?��?��.
		return result;
	}

	/**
	 * ?��?��미터�? ?��?��받아?�� int�? ?���??�� ?��?�� 리턴?��?��.
	 * 
	 * @param fieldName
	 *            - ?��?��미터 ?���?
	 * @param defaultValue
	 *            - 값이 ?��?�� 경우 ?��?��?�� 기본�?
	 * @return int
	 */
	public int getInt(String fieldName, int defaultValue) {
		// 리턴?�� ?��?�� 값을 ?�� 번째 ?��?��미터(기본�?)�? ?��?��?�� ?��?��.
		int result = defaultValue;
		// getString()메서?���? ?��?��?�� ?��?��미터�? 문자?�� ?��?���? 받는?��.
		// ?��?��미터�? 존재?���? ?��?��?���? ?�� 번째�? ?��?��?�� 값이 리턴?��?��.
		String param = this.getString(fieldName, null);

		// ?��?��미터�? ?��?��?�� 값을 ?��?���? ?���??�� ?��?��.
		try {
			result = Integer.parseInt(param);
		} catch (NumberFormatException e) {
			// ?���??��?�� ?��?��?�� 경우 catch블록?���? ?��?���? ?��?��?���?,result값�? 미리 ?��?��?�� ?��
			// defaultValue?�� ?��?���? ?���??��?��.
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 배열 ?��?��?�� ?��?��미터�? 리턴?��?��. 체크박스 ?��?�� 기능
	 * 
	 * @param fieldName
	 *            - ?��?��미터 ?���?
	 * @param defaultValue
	 *            - 값이 ?��거나 배열?�� 길이�? 0?�� 경우 ?��?��?�� 기본�?
	 * @return String[]
	 */
	public String[] getStringArray(String fieldName, String[] defaultValue) {
		// 리턴?�� ?��?�� 값을 ?�� 번째 ?��?��미터(기본�?)�? ?��?��?�� ?��?��.
		String[] result = defaultValue;
		// 배열 ?��?��?�� GET,POST ?��?��미터�? 받는?��.
		String[] param = this.request.getParameterValues(fieldName);

		if (param != null) { // ?��?��?�� ?��?��미터�? 존재?��?���??
			if (param.length > 0) { // 배열?�� 길이�? 0보다 ?��?���??
				result = param; // 리턴?�� ?��?��?�� �?비한 �??��?�� ?��?��?�� 값을 복사?��?��.
			}
		}

		// 값을 리턴. param값이 존재?���? ?��?�� 경우 미리 �?비한 기본값이 그�?�? 리턴?��?��.
		return result;
	}

	public String getString(String fieldName) {
		return this.getString(fieldName, null);
	}

	public int getInt(String fieldName) {
		return this.getInt(fieldName, 0);
	}

	public String[] getStringArray(String fieldName) {
		return this.getStringArray(fieldName, null);
	}

	/**
	 * 쿠키값을 ???��?��?��.
	 * 
	 * @param key
	 *            - 쿠키?���?
	 * @param value
	 *            - �?
	 * @param timeout
	 *            - ?��?��?���?. 브라?��??�? ?��?���? 즉시 ?��?��?�� 경우 -1
	 */
	public void setCookie(String key, String value, int timeout) {
		/** ?��?��?�� 값을 URLEncoding 처리 ?��?��. */
		if (value != null) {
			try {
				// import java.net.URLEncoder
				value = URLEncoder.encode(value, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		/** 쿠키 객체 ?��?�� �? 기본 ?��?�� */
		Cookie cookie = new Cookie(key, value);
		cookie.setPath("/");
		cookie.setDomain(DOMAIN);

		/** ?��?��?���? ?��?�� */
		// ?��간값?�� 0보다 ?��?? 경우?�� ?�� 메서?���? ?��?��?���? ?��?���? ?��?��. (브라?��??�? ?��?���? ?��?��)
		// 0?���? ?��?��?�� 경우 setMaxAge(0)?��?���? ?��?��?���?�? 즉시 ?��?��?��?��.
		if (timeout > -1) {
			cookie.setMaxAge(timeout);
		}

		/** 쿠키 ???��?���? */
		this.response.addCookie(cookie);
	}

	/**
	 * 쿠키값을 조회?��?��.
	 * 
	 * @param key
	 *            - 쿠키?���?
	 * @param defaultValue
	 *            - 값이 ?��?�� 경우 ?��?��?�� 기본�?
	 * @return String
	 */
	public String getCookie(String key, String defaultValue) {
		/** 리턴?�� 값을 ?��?�� */
		String result = defaultValue;

		/** 쿠키 배열 �??��?���? */
		// import javax.servlet.http.Cookie
		Cookie[] cookies = this.request.getCookies();

		/** 쿠키�? ?��?���?? 추출?�� 배열?�� ?���? ?�� 만큼 반복?��면서 ?��?��?�� ?��름의 값을 �??�� */
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				// 쿠키?�� ?���? ?���?
				String cookieName = cookies[i].getName();
				// ?��?��?�� ?��름이?���??
				if (cookieName.equals(key)) {
					// 값을 추출 --> ?�� 값이 리턴?��?��.
					result = cookies[i].getValue();
					try {
						// import java.net.URLDecoder
						result = URLDecoder.decode(result, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					break;
				} // end if
			} // end for
		} // end if

		return result;
	}

	/**
	 * 쿠키값을 조회?��?��. 값이 ?��?�� 경우 Null?�� 리턴?��?��.
	 * 
	 * @param key
	 *            - 쿠키?���?
	 * @return String
	 */
	public String getCookie(String key) {
		return this.getCookie(key, null);
	}

	/**
	 * �??��?�� ?��?�� ???�� 쿠키�? ?��?��?��?��.
	 * 
	 * @param key
	 */
	public void removeCookie(String key) {
		this.setCookie(key, null, 0);
	}

	/**
	 * ?��?��값을 ???��?��?��.
	 * 
	 * @param key
	 *            - ?��?��?���?
	 * @param value
	 *            - ???��?�� ?��?��?��
	 */
	public void setSession(String key, Object value) {
		this.session.setAttribute(key, value);
	}

	/**
	 * ?��?��값을 조회?��?��.
	 * 
	 * @param key
	 *            - 조회?�� ?��?��?�� ?���?
	 * @param defaultValue
	 *            - 값이 ?��?�� 경우 ??체할 기본�?
	 * @return Object?���?�? 명시?�� ?���??�� ?��?��?��
	 */
	public Object getSession(String key, Object defaultValue) {
		Object value = this.session.getAttribute(key);

		if (value == null) {
			value = defaultValue;
		}

		return value;
	}

	/**
	 * ?��?��값을 조회?��?��. 값이 ?��?�� 경우?�� ???�� 기본값을 null�? ?��?��
	 * 
	 * @param key
	 *            - ?��?�� ?���?
	 * @return Object?���?�? 명시?�� ?���??�� ?��?��?��
	 */
	public Object getSession(String key) {
		return this.getSession(key, null);
	}

	/**
	 * ?��?�� ?��?��값을 ?��?��?��?��.
	 * 
	 * @param key
	 *            - ?��?�� ?���?
	 */
	public void removeSession(String key) {
		this.session.removeAttribute(key);
	}

	/**
	 * ?��?�� ?��?��?��?�� ???�� 모든 ?��?��값을 ?���? ?��?��?��?��.
	 */
	public void removeAllSession() {
		this.session.invalidate();
	}

	/**
	 * ?��?�� ?��로젝?��?�� 최상?�� 경로값을 "/?��로젝?���?" ?��?��?���? 리턴?��?��.
	 * 
	 * @return
	 */
	public String getRootPath() {
		return this.request.getContextPath();
	}

	/**
	 * ?��?�� ?��?��?��?�� ???�� IP주소�? 조회?��?�� 리턴?��?��.
	 * 
	 * @return String
	 */
	public String getClientIP() {
		String ip = request.getHeader("X-FORWARDED-FOR");
		if (ip == null || ip.length() == 0) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 결과 메시�?�? JSON?���? 출력?��?��. JSON Api?��?�� web.redirect() 기능?�� ??체할 ?��?��.
	 * 
	 * @param rt
	 *            - JSON?�� ?��?��?�� 메시�? ?��?��
	 */
	public void printJsonRt(String rt) {
		Map<String, String> data = new HashMap<String, String>();
		data.put("rt", rt);

		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 문자?��?�� ?��?��?�� HTML?��그�? 줄바�? 문자�? HTML?��?��문자 ?��?���? �??��
	 * 
	 * @param content
	 * @return String
	 */
	public String convertHtmlTag(String content) {
		// �?�? 결과�? ???��?�� 객체
		StringBuilder builder = new StringBuilder();

		// 문자?��?�� ?��?��?�� ?�� �??��
		char chrBuff;

		// �??�� ?�� 만큼 반복?��?��.
		for (int i = 0; i < content.length(); i++) {
			// ?�� �??���? 추출
			chrBuff = (char) content.charAt(i);

			// ?��?��문자 ?��?��?�� �??��?�� 경우 �??��?��?�� builder?�� 추�?
			// 그렇�? ?��?�� 경우 ?���? 그�?�? builder?�� 추�?
			switch (chrBuff) {
			case '<':
				builder.append("&lt;");
				break;
			case '>':
				builder.append("&gt;");
				break;
			case '&':
				builder.append("&amp;");
				break;
			case '\n':
				builder.append("&lt;br/&gt;");
				break;
			default:
				builder.append(chrBuff);
			}
		}

		// 조립?�� 결과�? 문자?���? �??��?��?�� 리턴?��?��.
		return builder.toString();
	}
}
