package com.dingdong.sys.vo.response;

import java.util.List;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.sys.model.SysTag;

public class SysTagResponse extends ResponseBody {

	private List<SysTag> tagList;

	public List<SysTag> getTagList() {
		return tagList;
	}

	public void setTagList(List<SysTag> tagList) {
		this.tagList = tagList;
	}

}
