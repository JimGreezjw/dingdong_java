//package com.dingdong.register.mapper;
//
//import java.util.Collection;
//import java.util.List;
//
//import org.apache.ibatis.annotations.Delete;
//import org.apache.ibatis.annotations.Insert;
//import org.apache.ibatis.annotations.Param;
//import org.apache.ibatis.annotations.SelectKey;
//import org.apache.ibatis.annotations.Update;
//import org.springframework.stereotype.Repository;
//
//import com.dingdong.register.model.Queue;
//
///**
// * 
// * @author chenliang
// * @version 2015年12月14日 下午11:44:48
// */
//
//@Repository
//public interface QueueMapper {
//
//	public Queue findById(@Param("id") long id);
//
//	public List<Queue> findByUserIdAndDoctorIdAndHospitalIdAndStatus(
//			@Param("userId") long userId, @Param("doctorId") long doctorId,
//			@Param("hospitalId") long hospitalId, @Param("status") int status);
//
//	@Insert("insert into queue (user_id, user_name, doctor_id,doctor_name, hospital_id,hospital_name, status, create_time, appointment_time,queue_num)"
//			+ " values (#{queue.userId}, #{queue.userName}, #{queue.doctorId}, #{queue.doctorName}, #{queue.hospitalId}, #{queue.hospitalName}, #{queue.status},"
//			+ " #{queue.createTime}, #{queue.appointmentTime}, #{queue.queueNum})")
//	@SelectKey(keyProperty = "queue.id", statement = "select LAST_INSERT_ID() as id", before = false, resultType = Long.class)
//	public int add(@Param("queue") Queue queue);
//
//	// @Insert("insert into queue (user_id, user_name, doctor_id, hospital_id, status, create_time, appointment_time)"
//	// + " values"
//	// +
//	// " <foreach item=\"param1\" collection=\"queueList\" open=\" \" separator=\",\" close=\" \">"
//	// +
//	// " (#{param1.userId}, #{param1.userName}, #{param1.doctorId}, #{param1.hospitalId},"
//	// + " #{param1.status},"
//	// + " #{param1.createTime}, #{param1.appointmentTime})"
//	// + " </foreach>")
//	public int addList(@Param("queueList") Collection<Queue> queueList);
//
//	/**
//	 * 获取每个医生在各个医院的排队总数
//	 * 
//	 * @param doctorId
//	 *            医生主键
//	 * @param status
//	 *            需要查询的状态，默认应该是创建状态
//	 * @return
//	 */
//	public List<Queue> statQueueNumByHospital(@Param("doctorId") long doctorId,
//			@Param("status") int status);
//
//	/**
//	 * 获取每个医院在各个医生的排队总数
//	 * 
//	 * @param doctorId
//	 *            医院主键
//	 * @param status
//	 *            需要查询的状态，默认应该是创建状态
//	 * @return
//	 */
//	public List<Queue> statQueueNumByDoctor(
//			@Param("hospitalId") long hospitalId, @Param("status") int status);
//
//	public List<Queue> findByDoctotrIdAndHospitalIdAndStatusOrderByCreateTime(
//			@Param("doctorId") long doctorId,
//			@Param("hospitalId") long hospitalId, @Param("status") int status);
//
//	/**
//	 * 取得医生在特定医院的排队人数,并进行分页显示,这里只需要第一页,所以不使用page作为参数
//	 * 
//	 * @param doctorId
//	 * @param hospitalId
//	 * @param status
//	 * @param presetNum
//	 * @return
//	 */
//	public List<Queue> findByDoctotrIdAndHospitalIdAndStatusOrderByCreateTimeLimitNum(
//			@Param("doctorId") long doctorId,
//			@Param("hospitalId") long hospitalId, @Param("status") int status,
//			@Param("presetNum") int presetNum);
//
//	public List<Queue> findByUserIdAndStatusOrderByCreateTime(
//			@Param("userId") long userId, @Param("status") int status);
//
//	@Update("update queue set user_id = #{queue.userId}, user_name = #{queue.userName},"
//			+ " doctor_id = #{queue.doctorId},doctor_name = #{queue.doctorName}, hospital_id = #{queue.hospitalId}, hospital_name = #{queue.hospitalName}, status = #{queue.status},"
//			+ " appointment_time = #{queue.appointmentTime}, create_time = #{queue.createTime}, queue_num=#{queue.queueNum}"
//			+ " where id = #{queue.id}")
//	public int save(@Param("queue") Queue queue);
//
//	@Update(" update queue set status = #{status} where id = #{id} ")
//	public void updateStatus(@Param("id") long id, @Param("status") long status);
//
//	@Update(" update queue set queue_num = #{newQueueNum} where id = #{id} and not exists"
//			+ " (select aa.id from"
//			+ " (select a.id from queue a where a.doctor_id = #{doctorId} and a.hospital_id = #{hospitalId} and a.status = 0 and a.queue_num = #{exceptQueueNum}) aa)")
//	public int updateQueueNum(@Param("id") long id,
//			@Param("doctorId") long doctorId,
//			@Param("hospitalId") long hospitalId,
//			@Param("newQueueNum") int newQueueNum,
//			@Param("exceptQueueNum") int exceptQueueNum);
//
//	public void updatequeueListStatus(
//			@Param("queueList") Collection<Queue> queueList);
//
//	// @Update("<foreach item=\"queue\" collection=\"queueList\" open=\"\" close=\"\" separator=\";\">"
//	// +
//	// " update queue set user_id = #{queue.userId}, user_name = #{queue.userName},"
//	// +
//	// " doctor_id = #{queue.doctorId}, hospital_id = #{queue.hospitalId}, status = #{queue.status},"
//	// +
//	// " appointment_time = #{queue.appointmentTime}, create_time = #{queue.createTime}"
//	// + " where id = #{queue.id}"
//	// + " </foreach>")
//	public int saveList(@Param("queueList") Collection<Queue> queueList);
//
//	@Delete("update queue set status=3 where id=#{id}")
//	public int deleteById(@Param(value = "id") long id);
// }
