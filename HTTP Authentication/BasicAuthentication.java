import java.io.IOException;
import java.security.MessageDigest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import sun.misc.*;

/**
 * basic Auth 认证方式
 * 
 * @author Geely
 *
 */
@Component
public class BasicAuthentication {

	/**
	 * 
	 * @param request
	 * @param response
	 * @param sessionName
	 * @return
	 */
	public static boolean checkHeaderAuth(HttpServletRequest request, String sessionName) {
		String authorization = request.getHeader("Authorization");
		if (StringUtils.isBlank(authorization) || authorization.length() < 6) {
			return false;
		}

		authorization = authorization.substring(6, authorization.length());
		String decodedAuth = base64Decode(authorization);
		if (decodedAuth == null || "".equals(decodedAuth)) {
			decodedAuth = "";
		}

		String[] useAuth = decodedAuth.split(":");
		if (useAuth.length < 2) {
			return false;
		}

		//LoginSysUser sysUser = sysUserService.getUserByLogin(useAuth[0], encoderByMd5(useAuth[1]));
		//if (sysUser == null) {
		//	return false;
		//}

		if (StringUtils.isNotBlank(sessionName)) {
			request.getSession().setAttribute(sessionName, decodedAuth);
		}

		return true;

	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param sessionName
	 * @return
	 */
	public static boolean checkUserAuth(HttpServletRequest request, String sessionName) {
		String sessionAuth = null;
		if (StringUtils.isNotBlank(sessionName)) {
			sessionAuth = (String) request.getSession().getAttribute(sessionName);
			if (sessionAuth == null || "".equals(sessionAuth)) {
				return false;
			}

			String[] useAuth = sessionAuth.split(":");
			if (useAuth.length < 2) {
				return false;
			} else {
				//LoginSysUser sysUser = sysUserService.getUserByLogin(useAuth[0], encoderByMd5(useAuth[1]));
				//if (sysUser != null) {
				//	return true;
				//}
			}

			return false;
		}
		return true;
	}

	public static void redirect(HttpServletResponse response) {
		response.setStatus(401);
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		response.setHeader("WWW-authenticate", "Basic Realm=\"test\"");
	}

	/**
	 * 编码
	 * 
	 * @param bstr
	 * @return String
	 */
	@SuppressWarnings("restriction")
	public static String base64Encode(byte[] bstr) {
		String strEncode = new BASE64Encoder().encode(bstr);
		return strEncode;
	}

	/**
	 * 解码
	 * 
	 * @param str
	 * @return
	 */
	@SuppressWarnings("restriction")
	public static String base64Decode(String str) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		String s = null;
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] b = decoder.decodeBuffer(str);
			s = new String(b, "UTF8");
		} catch (IOException e) {
			s = null;
		}
		return s;
	}
	
	/**
	 * 对字符串md5加密(大写+数字)
	 * 
	 * @param str
	 *            传入要加密的字符串
	 * @return MD5加密后的字符串
	 */
	public static String encoderByMd5(String s) {
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		String strMd5 = null;
		try {
			byte[] bytes = s.getBytes("UTF8");
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			md.update(bytes);
			// 获得密文
			byte[] mdt = md.digest();
			// 把密文转换成十六进制的字符串形式
			int j = mdt.length;
			char[] str = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = mdt[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			strMd5 = new String(str).toLowerCase();
		} catch (Exception e) {
			strMd5 = null;
		}
		return strMd5;
	}

}
