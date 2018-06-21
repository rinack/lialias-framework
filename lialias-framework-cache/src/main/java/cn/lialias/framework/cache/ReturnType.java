package cn.lialias.framework.cache;

public enum ReturnType {

	/**
	 * 无返回值
	 */
	VOID("0"),
	
	/**
	 * 对象
	 */
	OBJECT("1"),

	/**
	 * 集合
	 */
	COLLECTION("2");

	private String value;

	public String getValue() {
		return value;
	}

	private ReturnType(String value) {
		this.value = value;
	}

	// 通过value获取对应的枚举对象
	public static ReturnType getEnumTypeOfvalue(String value) {
		for (ReturnType examType : ReturnType.values()) {
			if (examType.getValue().equals(value)) {
				return examType;
			}
		}
		return null;
	}

}
