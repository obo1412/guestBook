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
	/** μΏ ν€?? ?¬?©?  ?λ©μΈ */
	private static final String DOMAIN = "localhost";

	/** JSP? request ?΄?₯ κ°μ²΄ */
	// --> import javax.servlet.http.HttpServletRequest;
	private HttpServletRequest request;

	/** JSP? response ?΄?₯ κ°μ²΄ */
	// --> import javax.servlet.http.HttpServletResponse;
	private HttpServletResponse response;

	/** JSP? out ?΄?₯ κ°μ²΄ */
	// --> import java.io.PrintWriter;
	private PrintWriter out;

	/** JSP? session ?΄?₯ κ°μ²΄ */
	// --> import javax.servlet.http.HttpSession;
	private HttpSession session;

	/**
	 * WebHelper κΈ°λ₯? μ΄κΈ°? ??€. Spring?΄ ? κ³΅ν? ServletRequestAttributes κ°μ²΄λ₯? ?΅?΄?
	 * request, responseκ°μ²΄λ₯? μ§μ  ??±?  ? ??€.
	 */
	public void init() {

		/** JSP ?΄?₯κ°μ²΄λ₯? ?΄κ³? ?? Spring? κ°μ²΄λ₯? ?΅?΄? ?΄?₯κ°μ²΄ ???κΈ? */
		// --> import
		// org.springframework.web.context.request.RequestContextHolder;
		// --> import
		// org.springframework.web.context.request.ServletRequestAttributes;
		ServletRequestAttributes requestAttr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

		// request?? response κ°μ²΄λ₯? μΆμΆ??€.
		this.request = requestAttr.getRequest();
		this.response = requestAttr.getResponse();

		// ?Έ?κ°μ²΄ ??±?κΈ?
		this.session = request.getSession();

		// ??΄μ§? ?΄? ??΄ ?Έ??΄ ? μ§??? ?κ°? ?€?  (μ΄?)
		// --> 24?κ°?
		this.session.setMaxInactiveInterval(60 * 60 * 24);

		/** ?΄?₯κ°μ²΄ μ΄κΈ°? -> utf-8 ?€? , outκ°μ²΄ ??± */
		try {
			// ?Έμ½λ© ?€? ?κΈ?
			this.request.setCharacterEncoding("utf-8");
			this.response.setCharacterEncoding("utf-8");
			// outκ°μ²΄ ??±?κΈ?
			this.out = response.getWriter();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * λ©μμ§? ?? ?, ??΄μ§?λ₯? μ§?? ? κ³³μΌλ‘? ?΄???€.
	 * @param url - ?΄??  ??΄μ§?? URL, Null?Ό κ²½μ° ?΄? ??΄μ§?λ‘? ?΄?
	 * @param msg - ?λ©΄μ ???  λ©μμ§?. Null?Ό κ²½μ° ?? ??¨
	 */
	// --> import org.springframework.web.servlet.ModelAndView;
	public ModelAndView redirect(String url, String msg) {
		// κ°??? Viewλ‘? λ§λ€κΈ? ?? HTML ?κ·? κ΅¬μ±
		String html = "<!doctype html>";
		html += "<html>";
		html += "<head>";
		html += "<meta charset='utf-8'>";

		// λ©μμ§? ??
		if (msg != null) {
			html += "<script type='text/javascript'>alert('" + msg + "');</script>";
		}

		// ??΄μ§? ?΄?
		if (url != null) {
			html += "<meta http-equiv='refresh' content='0; url=" + url + "' />";
		} else {
			html += "<script type='text/javascript'>history.back();</script>";
		}

		html += "</head>";
		html += "<body></body>";
		html += "</html>";

		// κ΅¬μ±? HTML? μΆλ ₯??€.
		// out.print(html);

		// ?΅λͺν΄??€ λ°©μ?? ??λ§? ?Έ??  ? ??Όλ―?λ‘?, HTML?κ·Έλ?? ??? λ³΅μ¬
		final String page_content = html;

		/** κ°??? Viewλ₯? ?΅λͺ? ?΄??€ λ°©μ?Όλ‘? ??±??¬ λ¦¬ν΄ */
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

		// κ°??? λ·°λ?? λ¦¬ν΄??€.
		return new ModelAndView(view);
	}

	/**
	 * ??Όλ―Έν°λ₯? ? ?¬λ°μ? λ¦¬ν΄??€.
	 * 
	 * @param fieldName
	 *            - ??Όλ―Έν° ?΄λ¦?
	 * @param defaultValue
	 *            - κ°μ΄ ?? κ²½μ° ?¬?©?  κΈ°λ³Έκ°?
	 * @return String
	 */
	public String getString(String fieldName, String defaultValue) {
		// λ¦¬ν΄? ?? κ°μ ? λ²μ§Έ ??Όλ―Έν°(κΈ°λ³Έκ°?)λ‘? ?€? ?΄ ??€.
		String result = defaultValue;
		// GET,POST ??Όλ―Έν°λ₯? λ°λ?€.
		String param = this.request.getParameter(fieldName);

		if (param != null) { // κ°μ΄ null?΄ ???Όλ©??
			param = param.trim(); // ??€ κ³΅λ°±? ? κ±°ν?€.
			if (!param.equals("")) { // κ³΅λ°±? κ±? κ²°κ³Όκ°? λΉ? λ¬Έμ?΄?΄ ???Όλ©??
				result = param; // λ¦¬ν΄? ??΄? μ€?λΉν λ³??? ?? ? κ°μ λ³΅μ¬??€.
			}
		}

		// κ°μ λ¦¬ν΄. paramκ°μ΄ μ‘΄μ¬?μ§? ?? κ²½μ° λ―Έλ¦¬ μ€?λΉν κΈ°λ³Έκ°μ΄ κ·Έλ?λ‘? λ¦¬ν΄??€.
		return result;
	}

	/**
	 * ??Όλ―Έν°λ₯? ? ?¬λ°μ? intλ‘? ?λ³?? ??¬ λ¦¬ν΄??€.
	 * 
	 * @param fieldName
	 *            - ??Όλ―Έν° ?΄λ¦?
	 * @param defaultValue
	 *            - κ°μ΄ ?? κ²½μ° ?¬?©?  κΈ°λ³Έκ°?
	 * @return int
	 */
	public int getInt(String fieldName, int defaultValue) {
		// λ¦¬ν΄? ?? κ°μ ? λ²μ§Έ ??Όλ―Έν°(κΈ°λ³Έκ°?)λ‘? ?€? ?΄ ??€.
		int result = defaultValue;
		// getString()λ©μ?λ₯? ?΅?΄? ??Όλ―Έν°λ₯? λ¬Έμ?΄ ??λ‘? λ°λ?€.
		// ??Όλ―Έν°κ°? μ‘΄μ¬?μ§? ???€λ©? ? λ²μ§Έλ‘? ? ?¬? κ°μ΄ λ¦¬ν΄??€.
		String param = this.getString(fieldName, null);

		// ??Όλ―Έν°λ‘? ? ?¬? κ°μ ?«?λ‘? ?λ³?? ??€.
		try {
			result = Integer.parseInt(param);
		} catch (NumberFormatException e) {
			// ?λ³??? ?€?¨?  κ²½μ° catchλΈλ‘?Όλ‘? ? ?΄κ°? ?΄??κ³?,resultκ°μ? λ―Έλ¦¬ ?€? ?΄ ?
			// defaultValue?Έ ??λ₯? ? μ§???€.
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * λ°°μ΄ ??? ??Όλ―Έν°λ₯? λ¦¬ν΄??€. μ²΄ν¬λ°μ€ ? ?© κΈ°λ₯
	 * 
	 * @param fieldName
	 *            - ??Όλ―Έν° ?΄λ¦?
	 * @param defaultValue
	 *            - κ°μ΄ ?κ±°λ λ°°μ΄? κΈΈμ΄κ°? 0?Έ κ²½μ° ?¬?©?  κΈ°λ³Έκ°?
	 * @return String[]
	 */
	public String[] getStringArray(String fieldName, String[] defaultValue) {
		// λ¦¬ν΄? ?? κ°μ ? λ²μ§Έ ??Όλ―Έν°(κΈ°λ³Έκ°?)λ‘? ?€? ?΄ ??€.
		String[] result = defaultValue;
		// λ°°μ΄ ??? GET,POST ??Όλ―Έν°λ₯? λ°λ?€.
		String[] param = this.request.getParameterValues(fieldName);

		if (param != null) { // ?? ? ??Όλ―Έν°κ°? μ‘΄μ¬??€λ©??
			if (param.length > 0) { // λ°°μ΄? κΈΈμ΄κ°? 0λ³΄λ€ ?¬?€λ©??
				result = param; // λ¦¬ν΄? ??΄? μ€?λΉν λ³??? ?? ? κ°μ λ³΅μ¬??€.
			}
		}

		// κ°μ λ¦¬ν΄. paramκ°μ΄ μ‘΄μ¬?μ§? ?? κ²½μ° λ―Έλ¦¬ μ€?λΉν κΈ°λ³Έκ°μ΄ κ·Έλ?λ‘? λ¦¬ν΄??€.
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
	 * μΏ ν€κ°μ ???₯??€.
	 * 
	 * @param key
	 *            - μΏ ν€?΄λ¦?
	 * @param value
	 *            - κ°?
	 * @param timeout
	 *            - ?€? ?κ°?. λΈλΌ?°??λ₯? ?«?Όλ©? μ¦μ ?­? ?  κ²½μ° -1
	 */
	public void setCookie(String key, String value, int timeout) {
		/** ? ?¬? κ°μ URLEncoding μ²λ¦¬ ??€. */
		if (value != null) {
			try {
				// import java.net.URLEncoder
				value = URLEncoder.encode(value, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		/** μΏ ν€ κ°μ²΄ ??± λ°? κΈ°λ³Έ ?€?  */
		Cookie cookie = new Cookie(key, value);
		cookie.setPath("/");
		cookie.setDomain(DOMAIN);

		/** ? ?¨?κ°? ?€?  */
		// ?κ°κ°?΄ 0λ³΄λ€ ??? κ²½μ°? ?΄ λ©μ?λ₯? ?€? ?μ§? ??λ‘? ??€. (λΈλΌ?°??λ₯? ?«?Όλ©? ?­? )
		// 0?Όλ‘? ?€? ?  κ²½μ° setMaxAge(0)?΄?Όκ³? ?€? ?λ―?λ‘? μ¦μ ?­? ??€.
		if (timeout > -1) {
			cookie.setMaxAge(timeout);
		}

		/** μΏ ν€ ???₯?κΈ? */
		this.response.addCookie(cookie);
	}

	/**
	 * μΏ ν€κ°μ μ‘°ν??€.
	 * 
	 * @param key
	 *            - μΏ ν€?΄λ¦?
	 * @param defaultValue
	 *            - κ°μ΄ ?? κ²½μ° ?¬?©?  κΈ°λ³Έκ°?
	 * @return String
	 */
	public String getCookie(String key, String defaultValue) {
		/** λ¦¬ν΄?  κ°μ ?€?  */
		String result = defaultValue;

		/** μΏ ν€ λ°°μ΄ κ°?? Έ?€κΈ? */
		// import javax.servlet.http.Cookie
		Cookie[] cookies = this.request.getCookies();

		/** μΏ ν€κ°? ??€λ©?? μΆμΆ? λ°°μ΄? ?­λͺ? ? λ§νΌ λ°λ³΅?λ©΄μ ??? ?΄λ¦μ κ°μ κ²?? */
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				// μΏ ν€? ?΄λ¦? ?»κΈ?
				String cookieName = cookies[i].getName();
				// ??? ?΄λ¦μ΄?Όλ©??
				if (cookieName.equals(key)) {
					// κ°μ μΆμΆ --> ?΄ κ°μ΄ λ¦¬ν΄??€.
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
	 * μΏ ν€κ°μ μ‘°ν??€. κ°μ΄ ?? κ²½μ° Null? λ¦¬ν΄??€.
	 * 
	 * @param key
	 *            - μΏ ν€?΄λ¦?
	 * @return String
	 */
	public String getCookie(String key) {
		return this.getCookie(key, null);
	}

	/**
	 * μ§?? ? ?€? ??? μΏ ν€λ₯? ?­? ??€.
	 * 
	 * @param key
	 */
	public void removeCookie(String key) {
		this.setCookie(key, null, 0);
	}

	/**
	 * ?Έ?κ°μ ???₯??€.
	 * 
	 * @param key
	 *            - ?Έ??΄λ¦?
	 * @param value
	 *            - ???₯?  ?°?΄?°
	 */
	public void setSession(String key, Object value) {
		this.session.setAttribute(key, value);
	}

	/**
	 * ?Έ?κ°μ μ‘°ν??€.
	 * 
	 * @param key
	 *            - μ‘°ν?  ?Έ?? ?΄λ¦?
	 * @param defaultValue
	 *            - κ°μ΄ ?? κ²½μ° ??μ²΄ν  κΈ°λ³Έκ°?
	 * @return Object?΄λ―?λ‘? λͺμ?  ?λ³?? ???¨
	 */
	public Object getSession(String key, Object defaultValue) {
		Object value = this.session.getAttribute(key);

		if (value == null) {
			value = defaultValue;
		}

		return value;
	}

	/**
	 * ?Έ?κ°μ μ‘°ν??€. κ°μ΄ ?? κ²½μ°? ??? κΈ°λ³Έκ°μ nullλ‘? ?€? 
	 * 
	 * @param key
	 *            - ?Έ? ?΄λ¦?
	 * @return Object?΄λ―?λ‘? λͺμ?  ?λ³?? ???¨
	 */
	public Object getSession(String key) {
		return this.getSession(key, null);
	}

	/**
	 * ?Ή?  ?Έ?κ°μ ?­? ??€.
	 * 
	 * @param key
	 *            - ?Έ? ?΄λ¦?
	 */
	public void removeSession(String key) {
		this.session.removeAttribute(key);
	}

	/**
	 * ??¬ ?¬?©?? ??? λͺ¨λ  ?Έ?κ°μ ?Όκ΄? ?­? ??€.
	 */
	public void removeAllSession() {
		this.session.invalidate();
	}

	/**
	 * ??¬ ?λ‘μ ?Έ? μ΅μ? κ²½λ‘κ°μ "/?λ‘μ ?Έλͺ?" ???Όλ‘? λ¦¬ν΄??€.
	 * 
	 * @return
	 */
	public String getRootPath() {
		return this.request.getContextPath();
	}

	/**
	 * ??¬ ? ??? ??? IPμ£Όμλ₯? μ‘°ν??¬ λ¦¬ν΄??€.
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
	 * κ²°κ³Ό λ©μμ§?λ₯? JSON?Όλ‘? μΆλ ₯??€. JSON Api?? web.redirect() κΈ°λ₯? ??μ²΄ν  ?©?.
	 * 
	 * @param rt
	 *            - JSON? ?¬?¨?  λ©μμ§? ?΄?©
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
	 * λ¬Έμ?΄? ?¬?¨? HTML?κ·Έμ? μ€λ°κΏ? λ¬Έμλ₯? HTML?Ή?λ¬Έμ ??λ‘? λ³??
	 * 
	 * @param content
	 * @return String
	 */
	public String convertHtmlTag(String content) {
		// λ³?κ²? κ²°κ³Όλ₯? ???₯?  κ°μ²΄
		StringBuilder builder = new StringBuilder();

		// λ¬Έμ?΄? ?¬?¨? ? κΈ??
		char chrBuff;

		// κΈ?? ? λ§νΌ λ°λ³΅??€.
		for (int i = 0; i < content.length(); i++) {
			// ? κΈ??λ₯? μΆμΆ
			chrBuff = (char) content.charAt(i);

			// ?Ή?λ¬Έμ ??? λΆ??©?  κ²½μ° λ³????¬ builder? μΆκ?
			// κ·Έλ μ§? ?? κ²½μ° ?λ³? κ·Έλ?λ‘? builder? μΆκ?
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

		// μ‘°λ¦½? κ²°κ³Όλ₯? λ¬Έμ?΄λ‘? λ³???΄? λ¦¬ν΄??€.
		return builder.toString();
	}
}
