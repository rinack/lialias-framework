package cn.alias.framework.core.csrfxss;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.PatternMatchUtils;

@WebFilter(filterName = "XssFilter", urlPatterns = "/*")
public class XssFilter implements Filter {

	/**
	 * 需要排除的页面
	 */
	private String excludedPages;

	private String[] excludedPageArray;

	@SuppressWarnings("unused")
	private FilterConfig filterConfig;

	public void destroy() {
		this.filterConfig = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		boolean isExcludedPage = false;
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		// 判断是否需要XSS攻击防护
		isExcludedPage = isMatchUrl(excludedPageArray, httpServletRequest);

		if (isExcludedPage) {
			chain.doFilter(request, response);
		} else {
			chain.doFilter(new XssRequestWrapper(httpServletRequest), response);
		}
	}

	/**
	 * 自定义过滤规则
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		excludedPages = filterConfig.getInitParameter("excludedPages");
		excludedPageArray = new String[] {};
		if (StringUtils.isNotEmpty(excludedPages)) {
			excludedPageArray = excludedPages.replaceAll("[\\s]", "").split(",");
		}
	}

	/**
	 * URL是否符合规则列表
	 * 
	 * @param patterns
	 * @param request
	 * @return
	 */
	public static boolean isMatchUrl(String[] patterns, HttpServletRequest request) {
		String ctx_path = request.getContextPath();
		String request_uri = request.getRequestURI();
		String action = request_uri.substring(ctx_path.length()).replaceAll("//", "/");
		return PatternMatchUtils.simpleMatch(patterns, action);
	}

}
