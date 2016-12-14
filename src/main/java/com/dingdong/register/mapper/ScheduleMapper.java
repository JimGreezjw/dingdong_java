package com.dingdong.register.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.dingdong.register.model.Schedule;

/**
 * 日程映射
 * 
 * @author chenliang
 * @version 2015年12月13日 下午2:29:02
 */
@Repository
public interface ScheduleMapper {

	public Schedule findById(@Param("id") long id);

	public List<Schedule> findDoctorScheduleList(
			@Param("doctorId") long doctorId,
			@Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

	public List<Schedule> findDoctorScheduleListWithDraft(
			@Param("doctorId") long doctorId,
			@Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

	/**
	 * 通过
	 * 
	 * @param doctorId
	 *            医生编号
	 * @param hospitalId
	 *            医院编号
	 * @param beginDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @param status
	 *            状态
	 * @return
	 */
	public List<Schedule> findByDoctorIdHospitalIdBeginDateEndDateStatus(
			@Param("doctorId") long doctorId,
			@Param("hospitalId") long hospitalId,
			@Param("beginDate") Date beginDate, @Param("endDate") Date endDate,
			@Param("status") int status);

	// 寻找在有效期内未激活的日程
	public List<Schedule> findUnactiveScheduleList(@Param("status") int status,
			@Param("queryDate") Date queryDate);

	// 寻找特定时间段内未完成的日程
	public List<Schedule> findUnFullScheduleList(@Param("status") int status,
			@Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

	@Update("update schedule s set s.registered_num = #{newNum} "
			+ "where s.id = #{scheduleId} and s.registered_num = #{expect}")
	public int acquireScheduleLock(@Param("scheduleId") long scheduleId,
			@Param("newNum") int newNum, @Param("expect") int expect);

	@Insert("insert into schedule (status, doctor_id, doctor_name, hospital_id, hospital_name, schedule_date,"
			+ " time_slot, issue_num, create_time,start_time,end_time) values (#{schedule.status},"
			+ " #{schedule.doctorId},#{schedule.doctorName}, #{schedule.hospitalId}, #{schedule.hospitalName}, #{schedule.scheduleDate},"
			+ " #{schedule.timeSlot}, #{schedule.issueNum}, #{schedule.createTime}, #{schedule.startTime}, #{schedule.endTime})")
	@SelectKey(keyProperty = "schedule.id", statement = "select LAST_INSERT_ID() as id", before = false, resultType = Long.class)
	public int addSchedule(@Param("schedule") Schedule schedule);

	// 更改日程状态
	@Update(" update  schedule set status = #{status} where id = #{id}")
	public int updateScheduleState(@Param("id") long id,
			@Param("status") int status);

	// 删除日程信息
	@Update(" update  schedule set status=4  where id = #{id}")
	public void deleteById(@Param("id") long id);

	// 更新日程信息，只更新日程的几个重要信息
	@Update(" update schedule set status = #{schedule.status}, schedule_date = #{schedule.scheduleDate},"
			+ " time_slot = #{schedule.timeSlot}, issue_num = #{schedule.issueNum} where id = #{schedule.id}")
	public int updateScheduleInfo(@Param("schedule") Schedule schedule);

	@Update("update schedule set status = #{schedule.status}, schedule_date = #{schedule.scheduleDate},"
			+ " doctor_id = #{schedule.doctorId}, doctor_name = #{schedule.doctorName},"
			+ " hospital_id = #{schedule.hospitalId}, hospital_name = #{schedule.hospitalName},"
			+ " time_slot = #{schedule.timeSlot}, issue_num = #{schedule.issueNum}, registered_num"
			+ " = #{schedule.registeredNum}, create_time = #{schedule.createTime},creator_id = #{schedule.creatorId},start_time=#{schedule.startTime},end_time=#{schedule.endTime} where id = #{schedule.id}")
	public int save(@Param("schedule") Schedule schedule);
}
