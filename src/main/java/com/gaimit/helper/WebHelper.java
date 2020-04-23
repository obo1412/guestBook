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
	/** ì¿ í‚¤?—?„œ ?‚¬?š©?•  ?„ë©”ì¸ */
	private static final String DOMAIN = "localhost";

	/** JSP?˜ request ?‚´?¥ ê°ì²´ */
	// --> import javax.servlet.http.HttpServletRequest;
	private HttpServletRequest request;

	/** JSP?˜ response ?‚´?¥ ê°ì²´ */
	// --> import javax.servlet.http.HttpServletResponse;
	private HttpServletResponse response;

	/** JSP?˜ out ?‚´?¥ ê°ì²´ */
	// --> import java.io.PrintWriter;
	private PrintWriter out;

	/** JSP?˜ session ?‚´?¥ ê°ì²´ */
	// --> import javax.servlet.http.HttpSession;
	private HttpSession session;

	/**
	 * WebHelper ê¸°ëŠ¥?„ ì´ˆê¸°?™” ?•œ?‹¤. Spring?´ ? œê³µí•˜?Š” ServletRequestAttributes ê°ì²´ë¥? ?†µ?•´?„œ
	 * request, responseê°ì²´ë¥? ì§ì ‘ ?ƒ?„±?•  ?ˆ˜ ?ˆ?‹¤.
	 */
	public void init() {

		/** JSP ?‚´?¥ê°ì²´ë¥? ?‹´ê³? ?ˆ?Š” Spring?˜ ê°ì²´ë¥? ?†µ?•´?„œ ?‚´?¥ê°ì²´ ?š?“?•˜ê¸? */
		// --> import
		// org.springframework.web.context.request.RequestContextHolder;
		// --> import
		// org.springframework.web.context.request.ServletRequestAttributes;
		ServletRequestAttributes requestAttr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

		// request?? response ê°ì²´ë¥? ì¶”ì¶œ?•œ?‹¤.
		this.request = requestAttr.getRequest();
		this.response = requestAttr.getResponse();

		// ?„¸?…˜ê°ì²´ ?ƒ?„±?•˜ê¸?
		this.session = request.getSession();

		// ?˜?´ì§? ?´?™ ?—†?´ ?„¸?…˜?´ ?œ ì§??˜?Š” ?‹œê°? ?„¤? • (ì´?)
		// --> 24?‹œê°?
		this.session.setMaxInactiveInterval(60 * 60 * 24);

		/** ?‚´?¥ê°ì²´ ì´ˆê¸°?™” -> utf-8 ?„¤? •, outê°ì²´ ?ƒ?„± */
		try {
			// ?¸ì½”ë”© ?„¤? •?•˜ê¸?
			this.request.setCharacterEncoding("utf-8");
			this.response.setCharacterEncoding("utf-8");
			// outê°ì²´ ?ƒ?„±?•˜ê¸?
			this.out = response.getWriter();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ë©”ì‹œì§? ?‘œ?‹œ ?›„, ?˜?´ì§?ë¥? ì§?? •?œ ê³³ìœ¼ë¡? ?´?™?•œ?‹¤.
	 * @param url - ?´?™?•  ?˜?´ì§??˜ URL, Null?¼ ê²½ìš° ?´? „?˜?´ì§?ë¡? ?´?™
	 * @param msg - ?™”ë©´ì— ?‘œ?‹œ?•  ë©”ì‹œì§?. Null?¼ ê²½ìš° ?‘œ?‹œ ?•ˆ?•¨
	 */
	// --> import org.springframework.web.servlet.ModelAndView;
	public ModelAndView redirect(String url, String msg) {
		// ê°??ƒ?˜ Viewë¡? ë§Œë“¤ê¸? ?œ„?•œ HTML ?ƒœê·? êµ¬ì„±
		String html = "<!doctype html>";
		html += "<html>";
		html += "<head>";
		html += "<meta charset='utf-8'>";

		// ë©”ì‹œì§? ?‘œ?‹œ
		if (msg != null) {
			html += "<script type='text/javascript'>alert('" + msg + "');</script>";
		}

		// ?˜?´ì§? ?´?™
		if (url != null) {
			html += "<meta http-equiv='refresh' content='0; url=" + url + "' />";
		} else {
			html += "<script type='text/javascript'>history.back();</script>";
		}

		html += "</head>";
		html += "<body></body>";
		html += "</html>";

		// êµ¬ì„±?œ HTML?„ ì¶œë ¥?•œ?‹¤.
		// out.print(html);

		// ?µëª…í´?˜?Š¤ ë°©ì‹?? ?ƒ?ˆ˜ë§? ?¸?‹?•  ?ˆ˜ ?ˆ?œ¼ë¯?ë¡?, HTML?ƒœê·¸ë?? ?ƒ?ˆ˜?— ë³µì‚¬
		final String page_content = html;

		/** ê°??ƒ?˜ Viewë¥? ?µëª? ?´?˜?Š¤ ë°©ì‹?œ¼ë¡? ?ƒ?„±?•˜?—¬ ë¦¬í„´ */
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

		// ê°??ƒ?˜ ë·°ë?? ë¦¬í„´?•œ?‹¤.
		return new ModelAndView(view);
	}

	/**
	 * ?ŒŒ?¼ë¯¸í„°ë¥? ? „?‹¬ë°›ì•„?„œ ë¦¬í„´?•œ?‹¤.
	 * 
	 * @param fieldName
	 *            - ?ŒŒ?¼ë¯¸í„° ?´ë¦?
	 * @param defaultValue
	 *            - ê°’ì´ ?—†?„ ê²½ìš° ?‚¬?š©?  ê¸°ë³¸ê°?
	 * @return String
	 */
	public String getString(String fieldName, String defaultValue) {
		// ë¦¬í„´?„ ?œ„?•œ ê°’ì„ ?‘ ë²ˆì§¸ ?ŒŒ?¼ë¯¸í„°(ê¸°ë³¸ê°?)ë¡? ?„¤? •?•´ ?‘”?‹¤.
		String result = defaultValue;
		// GET,POST ?ŒŒ?¼ë¯¸í„°ë¥? ë°›ëŠ”?‹¤.
		String param = this.request.getParameter(fieldName);

		if (param != null) { // ê°’ì´ null?´ ?•„?‹ˆ?¼ë©??
			param = param.trim(); // ?•?’¤ ê³µë°±?„ ? œê±°í•œ?‹¤.
			if (!param.equals("")) { // ê³µë°±? œê±? ê²°ê³¼ê°? ë¹? ë¬¸ì?—´?´ ?•„?‹ˆ?¼ë©??
				result = param; // ë¦¬í„´?„ ?œ„?•´?„œ ì¤?ë¹„í•œ ë³??ˆ˜?— ?ˆ˜?‹ ?•œ ê°’ì„ ë³µì‚¬?•œ?‹¤.
			}
		}

		// ê°’ì„ ë¦¬í„´. paramê°’ì´ ì¡´ì¬?•˜ì§? ?•Š?„ ê²½ìš° ë¯¸ë¦¬ ì¤?ë¹„í•œ ê¸°ë³¸ê°’ì´ ê·¸ë?ë¡? ë¦¬í„´?œ?‹¤.
		return result;
	}

	/**
	 * ?ŒŒ?¼ë¯¸í„°ë¥? ? „?‹¬ë°›ì•„?„œ intë¡? ?˜•ë³??™˜ ?•˜?—¬ ë¦¬í„´?•œ?‹¤.
	 * 
	 * @param fieldName
	 *            - ?ŒŒ?¼ë¯¸í„° ?´ë¦?
	 * @param defaultValue
	 *            - ê°’ì´ ?—†?„ ê²½ìš° ?‚¬?š©?  ê¸°ë³¸ê°?
	 * @return int
	 */
	public int getInt(String fieldName, int defaultValue) {
		// ë¦¬í„´?„ ?œ„?•œ ê°’ì„ ?‘ ë²ˆì§¸ ?ŒŒ?¼ë¯¸í„°(ê¸°ë³¸ê°?)ë¡? ?„¤? •?•´ ?‘”?‹¤.
		int result = defaultValue;
		// getString()ë©”ì„œ?“œë¥? ?†µ?•´?„œ ?ŒŒ?¼ë¯¸í„°ë¥? ë¬¸ì?—´ ?˜•?ƒœë¡? ë°›ëŠ”?‹¤.
		// ?ŒŒ?¼ë¯¸í„°ê°? ì¡´ì¬?•˜ì§? ?•Š?Š”?‹¤ë©? ?‘ ë²ˆì§¸ë¡? ? „?‹¬?•œ ê°’ì´ ë¦¬í„´?œ?‹¤.
		String param = this.getString(fieldName, null);

		// ?ŒŒ?¼ë¯¸í„°ë¡? ? „?‹¬?œ ê°’ì„ ?ˆ«?ë¡? ?˜•ë³??™˜ ?•œ?‹¤.
		try {
			result = Integer.parseInt(param);
		} catch (NumberFormatException e) {
			// ?˜•ë³??™˜?— ?‹¤?Œ¨?•  ê²½ìš° catchë¸”ë¡?œ¼ë¡? ? œ?–´ê°? ?´?™?•˜ê³?,resultê°’ì? ë¯¸ë¦¬ ?„¤? •?•´ ?‘”
			// defaultValue?¸ ?ƒ?ƒœë¥? ?œ ì§??•œ?‹¤.
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * ë°°ì—´ ?˜•?ƒœ?˜ ?ŒŒ?¼ë¯¸í„°ë¥? ë¦¬í„´?•œ?‹¤. ì²´í¬ë°•ìŠ¤ ? „?š© ê¸°ëŠ¥
	 * 
	 * @param fieldName
	 *            - ?ŒŒ?¼ë¯¸í„° ?´ë¦?
	 * @param defaultValue
	 *            - ê°’ì´ ?—†ê±°ë‚˜ ë°°ì—´?˜ ê¸¸ì´ê°? 0?¸ ê²½ìš° ?‚¬?š©?  ê¸°ë³¸ê°?
	 * @return String[]
	 */
	public String[] getStringArray(String fieldName, String[] defaultValue) {
		// ë¦¬í„´?„ ?œ„?•œ ê°’ì„ ?‘ ë²ˆì§¸ ?ŒŒ?¼ë¯¸í„°(ê¸°ë³¸ê°?)ë¡? ?„¤? •?•´ ?‘”?‹¤.
		String[] result = defaultValue;
		// ë°°ì—´ ?˜•?ƒœ?˜ GET,POST ?ŒŒ?¼ë¯¸í„°ë¥? ë°›ëŠ”?‹¤.
		String[] param = this.request.getParameterValues(fieldName);

		if (param != null) { // ?ˆ˜?‹ ?œ ?ŒŒ?¼ë¯¸í„°ê°? ì¡´ì¬?•œ?‹¤ë©??
			if (param.length > 0) { // ë°°ì—´?˜ ê¸¸ì´ê°? 0ë³´ë‹¤ ?¬?‹¤ë©??
				result = param; // ë¦¬í„´?„ ?œ„?•´?„œ ì¤?ë¹„í•œ ë³??ˆ˜?— ?ˆ˜?‹ ?•œ ê°’ì„ ë³µì‚¬?•œ?‹¤.
			}
		}

		// ê°’ì„ ë¦¬í„´. paramê°’ì´ ì¡´ì¬?•˜ì§? ?•Š?„ ê²½ìš° ë¯¸ë¦¬ ì¤?ë¹„í•œ ê¸°ë³¸ê°’ì´ ê·¸ë?ë¡? ë¦¬í„´?œ?‹¤.
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
	 * ì¿ í‚¤ê°’ì„ ???¥?•œ?‹¤.
	 * 
	 * @param key
	 *            - ì¿ í‚¤?´ë¦?
	 * @param value
	 *            - ê°?
	 * @param timeout
	 *            - ?„¤? •?‹œê°?. ë¸Œë¼?š°??ë¥? ?‹«?œ¼ë©? ì¦‰ì‹œ ?‚­? œ?•  ê²½ìš° -1
	 */
	public void setCookie(String key, String value, int timeout) {
		/** ? „?‹¬?œ ê°’ì„ URLEncoding ì²˜ë¦¬ ?•œ?‹¤. */
		if (value != null) {
			try {
				// import java.net.URLEncoder
				value = URLEncoder.encode(value, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		/** ì¿ í‚¤ ê°ì²´ ?ƒ?„± ë°? ê¸°ë³¸ ?„¤? • */
		Cookie cookie = new Cookie(key, value);
		cookie.setPath("/");
		cookie.setDomain(DOMAIN);

		/** ?œ ?š¨?‹œê°? ?„¤? • */
		// ?‹œê°„ê°’?´ 0ë³´ë‹¤ ?‘?? ê²½ìš°?Š” ?´ ë©”ì„œ?“œë¥? ?„¤? •?•˜ì§? ?•Š?„ë¡? ?•œ?‹¤. (ë¸Œë¼?š°??ë¥? ?‹«?œ¼ë©? ?‚­? œ)
		// 0?œ¼ë¡? ?„¤? •?•  ê²½ìš° setMaxAge(0)?´?¼ê³? ?„¤? •?˜ë¯?ë¡? ì¦‰ì‹œ ?‚­? œ?œ?‹¤.
		if (timeout > -1) {
			cookie.setMaxAge(timeout);
		}

		/** ì¿ í‚¤ ???¥?•˜ê¸? */
		this.response.addCookie(cookie);
	}

	/**
	 * ì¿ í‚¤ê°’ì„ ì¡°íšŒ?•œ?‹¤.
	 * 
	 * @param key
	 *            - ì¿ í‚¤?´ë¦?
	 * @param defaultValue
	 *            - ê°’ì´ ?—†?„ ê²½ìš° ?‚¬?š©?  ê¸°ë³¸ê°?
	 * @return String
	 */
	public String getCookie(String key, String defaultValue) {
		/** ë¦¬í„´?•  ê°’ì„ ?„¤? • */
		String result = defaultValue;

		/** ì¿ í‚¤ ë°°ì—´ ê°?? ¸?˜¤ê¸? */
		// import javax.servlet.http.Cookie
		Cookie[] cookies = this.request.getCookies();

		/** ì¿ í‚¤ê°? ?ˆ?‹¤ë©?? ì¶”ì¶œ?œ ë°°ì—´?˜ ?•­ëª? ?ˆ˜ ë§Œí¼ ë°˜ë³µ?•˜ë©´ì„œ ?›?•˜?Š” ?´ë¦„ì˜ ê°’ì„ ê²??ƒ‰ */
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				// ì¿ í‚¤?˜ ?´ë¦? ?–»ê¸?
				String cookieName = cookies[i].getName();
				// ?›?•˜?Š” ?´ë¦„ì´?¼ë©??
				if (cookieName.equals(key)) {
					// ê°’ì„ ì¶”ì¶œ --> ?´ ê°’ì´ ë¦¬í„´?œ?‹¤.
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
	 * ì¿ í‚¤ê°’ì„ ì¡°íšŒ?•œ?‹¤. ê°’ì´ ?—†?„ ê²½ìš° Null?„ ë¦¬í„´?•œ?‹¤.
	 * 
	 * @param key
	 *            - ì¿ í‚¤?´ë¦?
	 * @return String
	 */
	public String getCookie(String key) {
		return this.getCookie(key, null);
	}

	/**
	 * ì§?? •?œ ?‚¤?— ???•œ ì¿ í‚¤ë¥? ?‚­? œ?•œ?‹¤.
	 * 
	 * @param key
	 */
	public void removeCookie(String key) {
		this.setCookie(key, null, 0);
	}

	/**
	 * ?„¸?…˜ê°’ì„ ???¥?•œ?‹¤.
	 * 
	 * @param key
	 *            - ?„¸?…˜?´ë¦?
	 * @param value
	 *            - ???¥?•  ?°?´?„°
	 */
	public void setSession(String key, Object value) {
		this.session.setAttribute(key, value);
	}

	/**
	 * ?„¸?…˜ê°’ì„ ì¡°íšŒ?•œ?‹¤.
	 * 
	 * @param key
	 *            - ì¡°íšŒ?•  ?„¸?…˜?˜ ?´ë¦?
	 * @param defaultValue
	 *            - ê°’ì´ ?—†?„ ê²½ìš° ??ì²´í•  ê¸°ë³¸ê°?
	 * @return Object?´ë¯?ë¡? ëª…ì‹œ?  ?˜•ë³??™˜ ?•„?š”?•¨
	 */
	public Object getSession(String key, Object defaultValue) {
		Object value = this.session.getAttribute(key);

		if (value == null) {
			value = defaultValue;
		}

		return value;
	}

	/**
	 * ?„¸?…˜ê°’ì„ ì¡°íšŒ?•œ?‹¤. ê°’ì´ ?—†?„ ê²½ìš°?— ???•œ ê¸°ë³¸ê°’ì„ nullë¡? ?„¤? •
	 * 
	 * @param key
	 *            - ?„¸?…˜ ?´ë¦?
	 * @return Object?´ë¯?ë¡? ëª…ì‹œ?  ?˜•ë³??™˜ ?•„?š”?•¨
	 */
	public Object getSession(String key) {
		return this.getSession(key, null);
	}

	/**
	 * ?Š¹? • ?„¸?…˜ê°’ì„ ?‚­? œ?•œ?‹¤.
	 * 
	 * @param key
	 *            - ?„¸?…˜ ?´ë¦?
	 */
	public void removeSession(String key) {
		this.session.removeAttribute(key);
	}

	/**
	 * ?˜„?¬ ?‚¬?š©??— ???•œ ëª¨ë“  ?„¸?…˜ê°’ì„ ?¼ê´? ?‚­? œ?•œ?‹¤.
	 */
	public void removeAllSession() {
		this.session.invalidate();
	}

	/**
	 * ?˜„?¬ ?”„ë¡œì ?Š¸?˜ ìµœìƒ?œ„ ê²½ë¡œê°’ì„ "/?”„ë¡œì ?Š¸ëª?" ?˜•?‹?œ¼ë¡? ë¦¬í„´?•œ?‹¤.
	 * 
	 * @return
	 */
	public String getRootPath() {
		return this.request.getContextPath();
	}

	/**
	 * ?˜„?¬ ? ‘?†??— ???•œ IPì£¼ì†Œë¥? ì¡°íšŒ?•˜?—¬ ë¦¬í„´?•œ?‹¤.
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
	 * ê²°ê³¼ ë©”ì‹œì§?ë¥? JSON?œ¼ë¡? ì¶œë ¥?•œ?‹¤. JSON Api?—?„œ web.redirect() ê¸°ëŠ¥?„ ??ì²´í•  ?š©?„.
	 * 
	 * @param rt
	 *            - JSON?— ?¬?•¨?•  ë©”ì‹œì§? ?‚´?š©
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
	 * ë¬¸ì?—´?— ?¬?•¨?œ HTML?ƒœê·¸ì? ì¤„ë°”ê¿? ë¬¸ìë¥? HTML?Š¹?ˆ˜ë¬¸ì ?˜•?ƒœë¡? ë³??™˜
	 * 
	 * @param content
	 * @return String
	 */
	public String convertHtmlTag(String content) {
		// ë³?ê²? ê²°ê³¼ë¥? ???¥?•  ê°ì²´
		StringBuilder builder = new StringBuilder();

		// ë¬¸ì?—´?— ?¬?•¨?œ ?•œ ê¸??
		char chrBuff;

		// ê¸?? ?ˆ˜ ë§Œí¼ ë°˜ë³µ?•œ?‹¤.
		for (int i = 0; i < content.length(); i++) {
			// ?•œ ê¸??ë¥? ì¶”ì¶œ
			chrBuff = (char) content.charAt(i);

			// ?Š¹?ˆ˜ë¬¸ì ?˜•?ƒœ?— ë¶??•©?•  ê²½ìš° ë³??™˜?•˜?—¬ builder?— ì¶”ê?
			// ê·¸ë ‡ì§? ?•Š?„ ê²½ìš° ?›ë³? ê·¸ë?ë¡? builder?— ì¶”ê?
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

		// ì¡°ë¦½?œ ê²°ê³¼ë¥? ë¬¸ì?—´ë¡? ë³??™˜?•´?„œ ë¦¬í„´?•œ?‹¤.
		return builder.toString();
	}
}
