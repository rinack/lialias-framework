import org.apache.commons.lang.StringUtils;

public class DigestHeader {

	public DigestHeader() {}

	public DigestHeader(String header, String method) {

		for (String keyValuePair : header.split(",")) {
			int index = keyValuePair.indexOf('=');
			String key = keyValuePair.substring(0, index).replace("Digest", "");
			String value = keyValuePair.substring(index + 1);
			key = StringUtils.deleteWhitespace(key);
			value = value.replace("\"", "");
			switch (key) {
			case "username":
				this.userName = value;
				break;
			case "realm":
				this.realm = value;
				break;
			case "nonce":
				this.nonce = value;
				break;
			case "uri":
				this.uri = value;
				break;
			case "nc":
				this.nc = value;
				break;
			case "cnonce":
				this.cnonce = value;
				break;
			case "response":
				this.response = value;
				break;
			case "method":
				this.method = value;
				break;
			default:
				break;
			}
		}

		if (StringUtils.isBlank(this.method))
			this.method = method;
	}

	private String cnonce;
	private String nonce;
	private String realm;
	private String userName;
	private String uri;
	private String response;
	private String method;
	private String nc;

	public String getCnonce() {
		return cnonce;
	}

	public void setCnonce(String cnonce) {
		this.cnonce = cnonce;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getNc() {
		return nc;
	}

	public void setNc(String nc) {
		this.nc = nc;
	}

	@Override
	public String toString() {
		return "DigestHeader [cnonce=" + cnonce + ", nonce=" + nonce + ", realm=" + realm + ", userName=" + userName
				+ ", uri=" + uri + ", response=" + response + ", method=" + method + ", nc=" + nc + "]";
	}

}