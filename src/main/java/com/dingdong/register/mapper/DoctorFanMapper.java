package com.dingdong.register.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dingdong.conf.PageInfo;
import com.dingdong.register.model.DoctorFan;

/**
 * 
 * @author ChanLueng
 * 
 */
@Repository
public interface DoctorFanMapper {

	public List<DoctorFan> findAllDoctorFans();

	public DoctorFan findById(@Param(value = "id") long id);

	public List<DoctorFan> findByUserId(@Param(value = "userId") long userId);

	public List<DoctorFan> findByDoctorId(
			@Param(value = "doctorId") long doctorId,
			@Param(value = "filterText") String filterText,
			@Param(value = "page") PageInfo page);

	public void addDoctorFan(@Param(value = "doctorFan") DoctorFan doctorFan);

	public void addDoctorFans(
			@Param(value = "doctorFans") List<DoctorFan> doctorFans);

	public void updateDoctorFan(@Param(value = "doctorFan") DoctorFan doctorFan);

	public void updateDoctorFans(
			@Param(value = "doctorFans") List<DoctorFan> doctorFans);

	public void deleteById(@Param(value = "id") long id);

	public DoctorFan findByUserIdAndDoctorId(
			@Param(value = "userId") long userId,
			@Param(value = "doctorId") long doctorId);

}
