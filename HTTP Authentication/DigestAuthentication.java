import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * HTTP 摘要认证
 * Authorization: Digest 
 * @author Geely
 *
 */
public class DigestAuthentication {

	/**
	 * 身份验证
	 * 
	 * @param request
	 * @param response
	 * @param sessionName
	 * @return
	 */
	public static boolean digestAuth(HttpServletRequest request) {
		
		String authorization = request.getHeader("Authorization");
		if (StringUtils.isBlank(authorization) || !authorization.startsWith("Digest")) {
			return false;
		}
		
		String method = request.getMethod();
		DigestHeader digestHeader = new DigestHeader(authorization, method);
		if (!Nonce.isValid(digestHeader.getNonce(), digestHeader.getNc())) {
			return false;
		}
		
		try {

			// 根据  digestHeader.getUserName() 从服务端获取到对应的 password
			String password = "123";

			String ha1 = MessageFormat.format("{0}:{1}:{2}", digestHeader.getUserName(), digestHeader.getRealm(),
					password);
			byte[] ha1bt = ha1.getBytes("UTF8");
			ha1 = Nonce.encoderByMd5(ha1bt);

			String ha2 = MessageFormat.format("{0}:{1}", digestHeader.getMethod(), digestHeader.getUri());
			byte[] ha2bt = ha2.getBytes("UTF8");
			ha2 = Nonce.encoderByMd5(ha2bt);

			String response = MessageFormat.format("{0}:{1}:{2}:{3}:{4}:{5}", ha1, digestHeader.getNonce(),
					digestHeader.getNc(), digestHeader.getCnonce(), "auth", ha2);

			byte[] resbt = response.getBytes("UTF8");
			response = Nonce.encoderByMd5(resbt);

			if (StringUtils.equalsIgnoreCase(digestHeader.getResponse(), response)) {
				return true;
			}

		} catch (UnsupportedEncodingException e) {

		}
		
		return false;
		
	}

	/**
	 * 注销
	 * 
	 * @param response
	 */
	public static void destroy(HttpServletResponse response) {
		response.setStatus(401);
		response.setHeader("WWW-Authenticate", "");
	}

	/**
	 * 重定向
	 * 
	 * @param response
	 */
	public static void redirect(HttpServletResponse response) {
		// WWW-Authenticate: Digest realm="Realm Name",domain="domain",nonce="nonce",algorithm=MD5,qop="auth"
		response.setStatus(401);
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		response.setHeader("WWW-Authenticate", "Digest realm='Geely',domain='tms.geely.com',nonce='" + Nonce.generate()
				+ "',algorithm='MD5',qop='auth',opaque='" + Nonce.encoderByMd5("geely") + "'");
	}

}
