package com.dingdong.sys.service;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.conf.DDPageInfo;
import com.dingdong.conf.PageInfo;
import com.dingdong.sys.model.Transfer;
import com.dingdong.sys.vo.response.TransferResponse;

public interface TransferService {

	/**
	 * 执行余额转账信
	 * 
	 * @param userId
	 * @param amount
	 * @return
	 */
	public ResponseBody executeTransferBalance(long userId, float amount,
			int reason);

	/**
	 * 设置转账成功
	 * 
	 * @param id
	 * @param transactionId
	 * @return
	 */
	public ResponseBody updateTransferSuccess(long id, String transactionId);

	/**
	 * 执行积分转账
	 * 
	 * @param userId
	 * @param score
	 * @return
	 */
	public ResponseBody executeTransferScore(long userId, int score, int reason);

	/**
	 * 获取账户明细
	 * 
	 * @param userId
	 * @param type
	 * @param page
	 * @return
	 */
	public TransferResponse getAccountDetails(
			@Param(value = "userId") long userId,
			@Param(value = "type") int type,
			@Param(value = "page") PageInfo page);

	/**
	 * 获取指定转账信息
	 * 
	 * @param id
	 * @return
	 */
	public TransferResponse findById(@Param(value = "id") long id);

	public TransferResponse addGetCashApply(Long accountId, Float amount);

	public ResponseBody confirmTransfer(@Param(value = "id") long id);

	public TransferResponse findSolvingTransfer(Date beginDate, Date endDate,
			DDPageInfo<Transfer> pageInfo);

}
