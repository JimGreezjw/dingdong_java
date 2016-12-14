package com.dingdong.sys.vo.response;

import java.util.List;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.sys.model.YusFile;

/**
 * 医生信息响应对象
 * 
 * @author chenliang
 * 
 */
public class YusFileResponse extends ResponseBody {
	/**
	 * 返回的消息
	 */
	private List<YusFile> yusFiles;

	public List<YusFile> getYusFiles() {
		return yusFiles;
	}

	public void setYusFiles(List<YusFile> yusFiles) {
		this.yusFiles = yusFiles;
	}

}
