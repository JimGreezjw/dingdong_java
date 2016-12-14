package com.dingdong.sys.vo.response;

import com.dingdong.common.vo.ResponseBody;

/**
 * 文件上传响应对象
 * 
 * @author chenliang
 * 
 */
public class UploadFileResponse extends ResponseBody {
	/**
	 * 返回的消息
	 */
	private String fileUrl;

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
}
