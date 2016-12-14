package com.dingdong.register.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.dingdong.register.model.Register;
import com.dingdong.register.model.Schedule;

/**
 * 
 * @author chenliang
 * 
 */
@Repository
public interface RegisterMapper {

	public Register findById(@Param(value = "id") long registerId);

	@Insert("insert into register (seq, status, register_time, schedule_id, create_time, user_id, user_name, patient_id, patient_name,doctor_id,"
			+ " doctor_name, hospital_id, hospital_name, schedule_date, time_slot, revisit, phenomenon, attach_no,deposit,hospital_register_id)"
			+ " values(#{register.seq}, #{register.status}, #{register.registerTime}, #{register.scheduleId},"
			+ " #{register.createTime}, #{register.userId}, #{register.userName}, #{register.patientId}, #{register.patientName}, #{register.doctorId}, #{register.doctorName},"
			+ " #{register.hospitalId}, #{register.hospitalName}, #{register.scheduleDate}, #{register.timeSlot}, "
			+ " #{register.revisit}, #{register.phenomenon}, #{register.attachNo}, #{register.deposit}, #{register.hospitalRegisterId})")
	@SelectKey(keyProperty = "register.id", statement = "SELECT LAST_INSERT_ID() as id", before = false, resultType = Long.class)
	public int add(@Param("register") Register register);

	public int addList(@Param("registerList") List<Register> registerList);

	public List<Register> findByScheduleId(
			@Param(value = "scheduleId") long scheduleId);

	public List<Register> findByDoctorIdAndHospitalIdAndScheduleDateAndStatus(
			@Param(value = "doctorId") long doctorId,
			@Param(value = "hospitalId") long hospitalId,
			@Param(value = "scheduleDate") Date scheduleDate,
			@Param("status") int status);

	public List<Register> findByDoctorIdHospitalIdStatusOrderByCreateTime(
			@Param(value = "doctorId") long doctorId,
			@Param(value = "hospitalId") long hospitalId,
			@Param("status") int status);

	// 获取已经到号的预约
	public List<Register> getArrivedQueue(
			@Param(value = "schedule") Schedule schedule);

	public List<Register> findUnFinishedRegistersByDoctorId(
			@Param(value = "doctorId") long doctorId);

	// 获取某一医院的未挂号列表
	public List<Register> findUnFinishedRegistersByHospitalId(
			@Param(value = "hospitalId") long hospitalId);

	/**
	 * 获取某一日程的未完成挂号
	 * 
	 * @param scheduleId
	 * @return
	 */
	public List<Register> findUnFinishedByScheduleId(
			@Param(value = "scheduleId") long scheduleId);

	public List<Register> findUnFinishedByUserIdDoctorIdHospitalId(
			@Param(value = "userId") long userId,
			@Param(value = "doctorId") long doctorId,
			@Param(value = "hospitalId") long hospitalId);

	public List<Register> findByDoctorIdScheduleDateOrderByUserName(
			@Param(value = "doctorId") long doctorId,
			@Param(value = "scheduleDate") Date scheduleDate);

	public List<Register> findByDoctorIdAndStatus(
			@Param(value = "doctorId") long doctorId,
			@Param("status") int status);

	public List<Register> findUnFinishedRegistersByUserId(
			@Param(value = "userId") long userId);

	/**
	 * 获取某个用户某个医生某个医院的未处理挂号信息
	 * 
	 * @param userId
	 * @param doctorId
	 * @param hospitalId
	 * @return
	 */
	public List<Register> findUnFinishedRegistersByUserIdDoctorIdHospitalId(
			@Param(value = "userId") long userId,
			@Param(value = "doctorId") long doctorId,
			@Param(value = "hospitalId") long hospitalId);

	public List<Register> findByUserIdAndStatus(
			@Param(value = "userId") long userId, @Param("status") int status);

	/**
	 * 寻找某个日期范围内某种状态的预约对象
	 * 
	 * @param status
	 * @param queryDate
	 * @return
	 */
	public List<Register> findByStatusAndDate(
			@Param(value = "status") int status,
			@Param(value = "queryDate") Date queryDate);

	/**
	 * 根据用户和预约号查询,暂时用户不支持多选
	 * 
	 * @param userId
	 * @param exceptStatus
	 *            例外状态
	 * @param scheduleId
	 * @return
	 */
	public List<Register> findByUserIdAndScheduleId(
			@Param(value = "userId") long userId,
			@Param("scheduleId") long scheduleId);

	@Update("update register set status = #{register.status}, register_time = #{register.registerTime},"
			+ " create_time = #{register.createTime}, patient_id = #{register.patientId},"
			+ " patient_name = #{register.patientName}, schedule_id = #{register.scheduleId},"
			+ " doctor_id = #{register.doctorId}, doctor_name = #{register.doctorName}, hospital_id"
			+ " = #{register.hospitalId}, hospital_name = #{register.hospitalName}, schedule_date = #{register.scheduleDate},"
			+ " time_slot = #{register.timeSlot},hospital_register_id= #{register.hospitalRegisterId}  where id = #{register.id}")
	public int updateRegister(@Param("register") Register register);

	@Update("update register set status = #{register.status}, patient_id = #{register.patientId},patient_name = #{register.patientName}, "
			+ " revisit = #{register.revisit}, phenomenon = #{register.phenomenon}, attach_no = #{register.attachNo}, deposit = #{register.deposit}"
			+ " where id = #{register.id}")
	public int confirmRegister(@Param("register") Register register);

	@Update("update register set status = #{status} where id = #{id} ")
	public int updateStatusById(@Param(value = "id") long id,
			@Param(value = "status") int status);

	public int updateList(@Param("registerList") List<Register> registerList);

	@Delete("update register set status=4 where id=#{id}")
	public int deleteById(@Param(value = "id") long id);

	@Update("update register set status = #{newState} where id = #{id} and status = #{oldState}")
	public int updateStateById(@Param(value = "id") long id,
			@Param(value = "newState") int newState,
			@Param(value = "oldState") int oldState);

	/**
	 * 查询已经失效的挂号
	 * 
	 * @param status
	 * @param expireTime
	 * @return
	 */
	public List<Register> findExpireRegister(
			@Param(value = "status") int status,
			@Param(value = "expireTime") Date expireTime);

	/**
	 * 更新挂号描述信息
	 * 
	 * @return
	 */
	public int updateRegisterDesc(@Param(value = "register") Register register);

	/**
	 * 统计用户的各个信息
	 * 
	 * @param id
	 * @return
	 */
	public List<Register> statUserMsg(@Param(value = "userId") long userId);

	public List<Register> findByDoctorIdAndHospitalIdAndStatus(
			@Param(value = "doctorId") long doctorId,
			@Param(value = "hospitalId") long hospitalId,
			@Param(value = "status") int status);
}
