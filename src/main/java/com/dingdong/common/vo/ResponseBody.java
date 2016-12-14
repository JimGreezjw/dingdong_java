package com.dingdong.common.vo;

import com.dingdong.common.exception.CommonErrorCode;
import com.dingdong.core.exception.ErrorCode;

/**
 * 
 * @author chenliang
 * @version 1.0
 */
public class ResponseBody {

	/** 状态值 */
	private int responseStatus = CommonErrorCode.OK.getCode();
	/** 状态描述 */
	private String responseDesc = CommonErrorCode.OK.getMessage();

	private Integer pages = 0;

	private Long total = 0l;

	public ResponseBody() {
	}

	public ResponseBody(int status, String desc) {
		this.responseStatus = status;
		this.responseDesc = desc;
	}

	public int getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(int status) {
		this.responseStatus = status;
	}

	public String getResponseDesc() {
		return responseDesc;
	}

	public void setResponseDesc(String desc) {
		this.responseDesc = desc;
	}

	public ResponseBody setErrorCode(ErrorCode errorCode) {
		this.responseStatus = errorCode.getCode();
		this.responseDesc = errorCode.getMessage();
		return this;
	}

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}
}
