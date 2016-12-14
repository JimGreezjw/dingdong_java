package com.dingdong.register.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dingdong.register.model.DoctorTag;

/**
 * 
 * @author niukai
 * 
 */
@Repository
public interface DoctorTagMapper {

	/**
	 * 查看医生是否具有某种标签
	 * 
	 * @param doctorId
	 * @param tagId
	 * @return
	 */
	public DoctorTag findByDoctorIdAndTagId(
			@Param(value = "doctorId") long doctorId,
			@Param(value = "tagId") long tagId);

	/**
	 * 医生的标签
	 * 
	 * @param doctorId
	 * @return
	 */
	public List<DoctorTag> findByDoctorId(
			@Param(value = "doctorId") long doctorId);

	/**
	 * 医生前多少个标签
	 * 
	 * @param doctorId
	 * @param topNum
	 * @return
	 */
	public List<DoctorTag> findByDoctorIdTopN(
			@Param(value = "doctorId") long doctorId,
			@Param(value = "topNum") int topNum);

	/**
	 * 新增标签
	 * 
	 * @param doctorTag
	 * @return
	 */
	public int insertDoctorTag(@Param(value = "doctorTag") DoctorTag doctorTag);

	/**
	 * 更新被评价的次数
	 * 
	 * @param doctorId
	 * @param tagId
	 * @return
	 */
	public int updateDoctorTag(@Param(value = "doctorId") long doctorId,
			@Param(value = "tagId") long tagId);
}
