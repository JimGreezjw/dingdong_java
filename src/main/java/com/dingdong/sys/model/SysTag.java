package com.dingdong.sys.model;

import org.apache.ibatis.type.Alias;

import com.dingdong.common.model.IdEntity;

/**
 * 系统标签
 * 
 * @author niukai
 * @created on January 24th, 2016
 * 
 */
@Alias("systag")
public class SysTag extends IdEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 933729889473062916L;

	private String tagName;

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
}
