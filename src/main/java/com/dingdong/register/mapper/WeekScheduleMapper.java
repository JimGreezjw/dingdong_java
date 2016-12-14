package com.dingdong.register.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.dingdong.register.model.WeekSchedule;

/**
 * 
 * @author yushansoft
 * 
 */
@Repository
public interface WeekScheduleMapper {

	public WeekSchedule findById(@Param("id") long id);

	/**
	 * 通过
	 * 
	 * @param doctorId
	 *            医生编号
	 * @param hospitalId
	 *            医院编号
	 * @param status
	 *            状态
	 * @return
	 */
	public List<WeekSchedule> findByDoctorIdHospitalIdStatus(
			@Param("doctorId") long doctorId,
			@Param("hospitalId") long hospitalId, @Param("status") int status);

	/**
	 * 取得某一星期几的日程
	 * 
	 * @param day
	 * @return
	 */
	public List<WeekSchedule> findByDay(@Param("day") int day);

	@Insert("insert into week_schedule (status, doctor_id, doctor_name, hospital_id, hospital_name, day,"
			+ " time_slot, creator_id, create_time,start_time,end_time,issue_Num) values (#{weekSchedule.status},"
			+ " #{weekSchedule.doctorId},#{weekSchedule.doctorName}, #{weekSchedule.hospitalId}, #{weekSchedule.hospitalName}, #{weekSchedule.day},"
			+ " #{weekSchedule.timeSlot}, #{weekSchedule.creatorId}, #{weekSchedule.createTime}, #{weekSchedule.startTime}, #{weekSchedule.endTime}, #{weekSchedule.issueNum})")
	@SelectKey(keyProperty = "weekSchedule.id", statement = "select LAST_INSERT_ID() as id", before = false, resultType = Long.class)
	public int add(@Param("weekSchedule") WeekSchedule weekSchedule);

	@Update("update week_schedule set status=#{weekSchedule.status}, day=#{weekSchedule.day},time_slot= #{weekSchedule.timeSlot}"
			+ " ,start_time=#{weekSchedule.startTime},end_time=#{weekSchedule.endTime},issue_Num=#{weekSchedule.issueNum} , creator_id=#{weekSchedule.creatorId}, create_time=#{weekSchedule.createTime} where id=#{weekSchedule.id}")
	public int update(@Param("weekSchedule") WeekSchedule weekSchedule);

	// 删除停诊信息
	@Update(" update  week_schedule set status=2  where id = #{id}")
	public void deleteById(@Param("id") long id);

}
