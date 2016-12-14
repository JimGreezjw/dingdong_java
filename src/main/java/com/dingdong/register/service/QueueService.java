//package com.dingdong.register.service;
//
//import java.util.Set;
//
//import com.dingdong.common.vo.ResponseBody;
//import com.dingdong.register.vo.response.QueueMutiResponse;
//import com.dingdong.register.vo.response.QueueResponse;
//import com.dingdong.register.vo.response.QueueSingleResponse;
//
///**
// * 预约挂号服务
// * 
// * @author chenliang
// * @version 2015年12月13日 上午1:35:01
// */
//public interface QueueService {
//
//	/**
//	 * 用户排队，当排队的用户达到一定数量，将为名医给出出诊日程建议。调用此方法将在排队表中插入 一行记录，同时在日程表中增加排队人数数量，面临并发问题
//	 * 
//	 * @param userId
//	 *            排队的用户id
//	 * @param doctorId
//	 *            排队的医院id
//	 * @param hospitalId
//	 *            排队的医院id
//	 * @return
//	 */
//	public QueueSingleResponse queueUp(long userId, long doctorId,
//			long hospitalId);
//
//	/**
//	 * 用户排队，同时在医生下面的多个医院排队
//	 * 
//	 * @param userId
//	 * @param doctorId
//	 * @param hospitalIds
//	 * @return
//	 */
//	public QueueMutiResponse queueUp(long userId, long doctorId,
//			Set<Long> hospitalIds);
//
//	/**
//	 * 用户取消排队；当用户预约成功之后，不可以取消预约
//	 * 
//	 * @param queueId
//	 *            排队id
//	 * @return
//	 */
//	public ResponseBody cancelQueue(long queueId);
//
//	/**
//	 * 获取名医在各个医院的排队人数统计，默认状态
//	 * 
//	 * @param doctorId
//	 *            医生
//	 * @return
//	 */
//	public QueueResponse statQueueNumByHospital(long doctorId);
//
//	/**
//	 * 获取医院在各个医生的排队人数统计，默认状态
//	 * 
//	 * @param hospitalId
//	 *            医院编号
//	 * @return
//	 */
//	public QueueResponse statQueueNumByDoctor(long hospitalId, int status);
//
//	/**
//	 * 获取名医在各个医院的排队人数统计，具有查询状态
//	 * 
//	 * @param doctorId
//	 *            医生
//	 * @param status
//	 *            状态
//	 * @return
//	 */
//	public QueueResponse statQueueNumByHospital(long doctorId, int status);
//
//	/**
//	 * 获得指定名医被人排队预约的信息
//	 * 
//	 * @param doctorId
//	 * @return
//	 */
//	public QueueResponse getByDoctorIdHospitalId(long doctorId, long hospitalId);
//
//	/**
//	 * 获得默认指定名医被人排队预约的信息
//	 * 
//	 * @param doctorId
//	 * @return
//	 */
//	public QueueResponse getByUserId(long userId);
// }
