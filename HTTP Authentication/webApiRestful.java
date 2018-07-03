import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.evun.tms.common.dto.WebApiRestful;
import cn.evun.tms.common.util.BasicAuthenticationUtil;
import cn.evun.tms.common.util.DigestAuthentication;

/**
 * 
 * @author Geely
 *
 */
@RestController
@RequestMapping("/webapi/channel")
public class webApiRestful {

	public static final String YEAR_MONTH_DAY_PATTERN = "yyyy-MM-dd";

	public static final String BASICAUTH_SESSION_NAME = "basicAuth";

	public static final int PAGE_SIZE = 500;

	/**
	 * 获取计划承运单
	 */
	@RequestMapping(value = "/access", method = RequestMethod.POST)
	@ResponseBody
	public WebApiRestful<String, String> getAccess(HttpServletRequest request, HttpServletResponse response) {

		if (!BasicAuthenticationUtil.checkUserAuth(request, BASICAUTH_SESSION_NAME)
				&& !BasicAuthenticationUtil.checkHeaderAuth(request, BASICAUTH_SESSION_NAME)) {
			BasicAuthenticationUtil.redirect(response);
			return null;
		}

		WebApiRestful<String, String> result = new WebApiRestful<>();
		result.setSuccess(false);
		result.setMessage("失败");

		String client = request.getParameter("client");

		if (StringUtils.isBlank(client)) {
			result.setMessage("请输入客户端编号");
			return result;
		}

		result.setSuccess(true);
		result.setMessage("成功");
		return result;

	}

	/**
	 *
	 * @param request
	 * @param response
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/digest", method = { RequestMethod.GET, RequestMethod.POST}, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public WebApiRestful<String, String> digest(HttpServletRequest request, HttpServletResponse response,
			@RequestBody(required=false) String json) {
		WebApiRestful<String, String> result = new WebApiRestful<>();
		result.setSuccess(false);
		result.setMessage("失败");

		if (!DigestAuthentication.digestAuth(request)) {
			DigestAuthentication.redirect(response);
			return null;
		}

		// 身份验证通过后,自定义代码

		result.setSuccess(true);
		result.setMessage("成功");

		return result;

	}

}
