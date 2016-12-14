package com.dingdong.register.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.dingdong.register.model.CloseSchedule;

/**
 * 
 * @author yushansoft
 * 
 */
@Repository
public interface CloseScheduleMapper {

	public CloseSchedule findById(@Param("id") long id);

	/**
	 * 通过 周计划id 查找停诊信息
	 * 
	 * @param weekScheduleId
	 * @return
	 */
	public List<CloseSchedule> findByWeekScheduleIdStatus(
			@Param("weekScheduleId") long weekScheduleId,
			@Param("status") int status);

	@Insert("insert into close_schedule (status, week_schedule_id, from_date,to_date,reason,creator_id,create_time)"
			+ " values (#{closeSchedule.status}, #{closeSchedule.weekScheduleId},#{closeSchedule.fromDate}, #{closeSchedule.toDate}, #{closeSchedule.reason}, #{closeSchedule.creatorId}, #{closeSchedule.createTime})")
	@SelectKey(keyProperty = "closeSchedule.id", statement = "select LAST_INSERT_ID() as id", before = false, resultType = Long.class)
	public int add(@Param("closeSchedule") CloseSchedule closeSchedule);

	@Update("update close_schedule set status=#{closeSchedule.status},from_date=#{closeSchedule.fromDate}, to_date=#{closeSchedule.toDate},reason= #{closeSchedule.reason}, creator_id=#{closeSchedule.creatorId}, create_time=#{closeSchedule.createTime} where id = #{closeSchedule.id}")
	public int update(@Param("closeSchedule") CloseSchedule closeSchedule);

	// 删除停诊信息
	@Update(" update  close_schedule set status=2  where id = #{id}")
	public void deleteById(@Param("id") long id);

}
