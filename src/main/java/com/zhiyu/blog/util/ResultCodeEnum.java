package com.zhiyu.blog.util;

/**
 * 结果code
 * 
 * @author xinyuan.wei
 * @date 2019年4月29日
 */
public enum ResultCodeEnum {
	Success(0), 
	Fail(1), 
	AuthFail(-1);

	private final int value;

	
	ResultCodeEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
