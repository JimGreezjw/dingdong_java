package com.dingdong.register.vo.response;

import java.util.List;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.model.DoctorTag;

/**
 * 
 * @author niukai
 * 
 */
public class DoctorTagResponse extends ResponseBody {

	private List<DoctorTag> tagList;

	public List<DoctorTag> getTagList() {
		return tagList;
	}

	public void setTagList(List<DoctorTag> tagList) {
		this.tagList = tagList;
	}

}
