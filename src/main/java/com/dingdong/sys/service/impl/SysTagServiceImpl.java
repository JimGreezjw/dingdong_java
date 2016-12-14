package com.dingdong.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dingdong.sys.mapper.SysTagMapper;
import com.dingdong.sys.model.SysTag;
import com.dingdong.sys.service.SysTagService;
import com.dingdong.sys.vo.response.SysTagResponse;

@Service
@Transactional
public class SysTagServiceImpl implements SysTagService {

	@Autowired
	private SysTagMapper sysTagMapper;

	@Override
	public SysTagResponse findAllTags() {
		SysTagResponse response = new SysTagResponse();

		List<SysTag> tagList = sysTagMapper.findAllTags();
		response.setTagList(tagList);
		return response;
	}

	@Override
	public SysTagResponse addTag(String tagName) {
		SysTagResponse response = new SysTagResponse();

		SysTag sysTag = new SysTag();
		sysTag.setTagName(tagName);

		sysTagMapper.addTag(sysTag);
		return response;
	}

	@Override
	public SysTagResponse deleteById(long id) {
		SysTagResponse response = new SysTagResponse();
		sysTagMapper.deleteById(id);

		return response;
	}

}
