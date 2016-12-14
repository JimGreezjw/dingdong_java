package com.dingdong.register.service;

import com.dingdong.register.model.DoctorTag;
import com.dingdong.register.vo.response.DoctorTagResponse;

/**
 * 医生标签
 * 
 * @author niukai
 * 
 */
public interface DoctorTagService {

	public DoctorTag findByDoctorIdAndTagId(long doctorId, long tagId);

	public DoctorTagResponse findByDoctorId(long doctorId);

	public DoctorTagResponse findByDoctorIdTopN(long doctorId, int topNum);

	public DoctorTagResponse insertDoctorTag(DoctorTag doctorTag);

	public DoctorTagResponse insertDoctorTag(long doctorId, String doctorName,
			long tagId, String tagName);

	public DoctorTagResponse updateDoctorTag(long doctorId, long tagId);
}
