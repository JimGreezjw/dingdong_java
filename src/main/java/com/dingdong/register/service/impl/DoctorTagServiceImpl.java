package com.dingdong.register.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dingdong.common.exception.LockFailureException;
import com.dingdong.register.mapper.DoctorTagMapper;
import com.dingdong.register.model.DoctorTag;
import com.dingdong.register.service.DoctorTagService;
import com.dingdong.register.vo.response.DoctorTagResponse;

@Service
@Transactional
public class DoctorTagServiceImpl implements DoctorTagService {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory
			.getLogger(DoctorTagService.class);

	@Autowired
	private DoctorTagMapper doctorTagMapper;

	@Override
	public DoctorTag findByDoctorIdAndTagId(long doctorId, long tagId) {
		DoctorTag doctorTag = doctorTagMapper.findByDoctorIdAndTagId(doctorId,
				tagId);

		return doctorTag;
	}

	@Override
	public DoctorTagResponse findByDoctorId(long doctorId) {
		DoctorTagResponse response = new DoctorTagResponse();
		List<DoctorTag> tagList = doctorTagMapper.findByDoctorId(doctorId);

		response.setTagList(tagList);
		return response;
	}

	@Override
	public DoctorTagResponse findByDoctorIdTopN(long doctorId, int topNum) {
		DoctorTagResponse response = new DoctorTagResponse();
		List<DoctorTag> tagList = doctorTagMapper.findByDoctorIdTopN(doctorId,
				topNum);

		response.setTagList(tagList);
		return response;
	}

	@Override
	public DoctorTagResponse insertDoctorTag(DoctorTag doctorTag) {
		DoctorTagResponse response = new DoctorTagResponse();

		int rows = doctorTagMapper.insertDoctorTag(doctorTag);
		if (rows < 1) {
			throw new LockFailureException("已存在相应的记录");
		}

		return response;
	}

	@Override
	public DoctorTagResponse insertDoctorTag(long doctorId, String doctorName,
			long tagId, String tagName) {
		DoctorTag doctorTag = new DoctorTag();
		doctorTag.setDoctorId(doctorId);
		doctorTag.setDoctorName(doctorName);
		doctorTag.setTagId(tagId);
		doctorTag.setTagName(tagName);
		doctorTag.setTimes(1);

		return this.insertDoctorTag(doctorTag);
	}

	@Override
	public DoctorTagResponse updateDoctorTag(long doctorId, long tagId) {
		DoctorTagResponse response = new DoctorTagResponse();

		int rows = doctorTagMapper.updateDoctorTag(doctorId, tagId);
		if (rows < 1) {
			throw new LockFailureException("已存在相应的记录");
		}

		return response;
	}

}
