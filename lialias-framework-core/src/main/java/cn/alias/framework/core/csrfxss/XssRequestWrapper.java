package cn.alias.framework.core.csrfxss;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.StringEscapeUtils;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;

public class XssRequestWrapper extends HttpServletRequestWrapper {

	private static Policy policy = null;

	static {
		try {
			policy = Policy.getInstance( XssRequestWrapper.class.getClassLoader()
					.getResourceAsStream("antisamy-anythinggoes.xml"));
		} catch (PolicyException e) {
			e.printStackTrace();
		}
	}

	public XssRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	@SuppressWarnings("rawtypes")
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> request_map = super.getParameterMap();
		Iterator iterator = request_map.entrySet().iterator();
		System.out.println("request_map" + request_map.size());
		while (iterator.hasNext()) {
			Map.Entry me = (Map.Entry) iterator.next();
			// System.out.println(me.getKey()+":");
			String[] values = (String[]) me.getValue();
			for (int i = 0; i < values.length; i++) {
				System.out.println(values[i]);
				values[i] = xssClean(values[i]);
			}
		}
		return request_map;
	}

	public String[] getParameterValues(String paramString) {
		String[] arrayOfString1 = super.getParameterValues(paramString);
		if (arrayOfString1 == null)
			return null;
		int i = arrayOfString1.length;
		String[] arrayOfString2 = new String[i];
		for (int j = 0; j < i; j++)
			arrayOfString2[j] = xssClean(arrayOfString1[j]);
		return arrayOfString2;
	}

	public String getParameter(String paramString) {
		String str = super.getParameter(paramString);
		if (str == null)
			return null;
		return xssClean(str);
	}

	public String getHeader(String paramString) {
		String str = super.getHeader(paramString);
		if (str == null)
			return null;
		return xssClean(str);
	}

	private String xssClean(String value) {
		AntiSamy antiSamy = new AntiSamy();
		try {
			
			// 安全的HTML输出
			//final CleanResults cr = antiSamy.scan(value, policy);
			//return cr.getCleanHTML();
			
			final CleanResults cr = antiSamy.scan(value, policy);
			// 安全的HTML输出
			String html = StringEscapeUtils.unescapeHtml(cr.getCleanHTML());
			html = html.replace((antiSamy.scan("&nbsp;", policy)).getCleanHTML(), "");
			return html;
			
		} catch (ScanException e) {
			e.printStackTrace();
		} catch (PolicyException e) {
			e.printStackTrace();
		}
		return value;
	}

}
