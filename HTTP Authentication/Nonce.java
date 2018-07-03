public class Nonce {

	private static Map<String, Map<String, Object>> nonces = new HashMap<>();

	public static String generate() {
		String nonce = "";
		byte[] bytes = new byte[16];
		SecureRandom random;
		try {

			random = SecureRandom.getInstance("SHA1PRNG");
			random.nextBytes(bytes);
			nonce = encoderByMd5(bytes);

			Map<String, Object> map = new HashMap<>();
			map.put("coun", 0);
			map.put("time", System.currentTimeMillis() + (1000 * 300));

			nonces.put(nonce, map);

		} catch (NoSuchAlgorithmException e) {

		}

		return nonce;
	}

	private static String nonceGenerate() {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			s.append(Integer.toHexString(new Random().nextInt(16)));
		}
		return s.toString();
	}

	public static boolean isValid(String nonce, String nc) {
		Map<String, Object> map = nonces.get(nonce);
		if (map != null && map.size() > 0) {
			Integer coun = (Integer) map.get("coun");
			Long time = (Long) map.get("time");
			if (Integer.parseInt(nc) > coun && time > System.currentTimeMillis()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 对字符串md5加密(大写+数字)
	 * 
	 * @param str
	 *            传入要加密的字符串
	 * @return MD5加密后的字符串
	 */
	public static String encoderByMd5(byte[] bytes) {
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		String strMd5 = null;
		try {
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
