package com.example.demo.controller.dto;

import io.swagger.annotations.ApiModelProperty;

public class BaseResDto {
	/**
	 * 状态码
	 */
	@ApiModelProperty(value = "状态码")
	private String code;
	/**
	 * 是否成功
	 */
	@ApiModelProperty(value = "是否成功")
	private boolean success;
	/**
	 * 返回信息
	 */
	@ApiModelProperty(value = "返回信息")
	private String mesg;
	/**
	 * 其他附加值
	 */
	@ApiModelProperty(value = "其他附加值")
	private Object values;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMesg() {
		return mesg;
	}

	public void setMesg(String mesg) {
		this.mesg = mesg;
	}

	public Object getValues() {
		return values;
	}

	public void setValues(Object values) {
		this.values = values;
	}
}
