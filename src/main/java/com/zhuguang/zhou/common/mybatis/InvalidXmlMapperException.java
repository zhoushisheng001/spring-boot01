package com.zhuguang.zhou.common.mybatis;

/**
 * 无效XML Mapper异常
 * @author liheng
 * @since 1.0
 *
 */
public class InvalidXmlMapperException extends RuntimeException {
	private static final long serialVersionUID = -3846556576046399727L;
	
	public InvalidXmlMapperException(String message) {
		super(message);
	}
	
    public InvalidXmlMapperException(String message, Throwable cause) {
        super(message, cause);
    }

}
