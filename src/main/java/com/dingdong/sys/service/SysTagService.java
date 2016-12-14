package com.dingdong.sys.service;

import com.dingdong.sys.vo.response.SysTagResponse;

public interface SysTagService {

	public SysTagResponse findAllTags();

	public SysTagResponse addTag(String tagName);

	public SysTagResponse deleteById(long id);

}
