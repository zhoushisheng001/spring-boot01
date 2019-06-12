package com.zhuguang.zhou.pojo;

/**
 * 应用代码
 * @author liheng
 * @since 1.0
 */
public interface AppCode {
	int getCode();
	void setCode(int code);

	String getMessage();
	void setMessage(String message);

	default boolean isSuccess() {
		return this.getCode() == 0;
	}
}
