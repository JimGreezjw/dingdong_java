package com.dingdong.sys.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dingdong.conf.PageInfo;
import com.dingdong.sys.model.Transfer;

/**
 * 
 * @author ChanLueng
 * 
 */
@Repository
public interface TransferMapper {

	public Transfer findById(@Param(value = "id") long id);

	public void addTransfer(@Param(value = "transfer") Transfer transfer);

	public int updateTransferSuccess(@Param(value = "id") long id,
			@Param(value = "transactionId") String transactionId,
			@Param(value = "transactionTime") Date transactionTime);

	public List<Transfer> findByUserIdTypeStatus(
			@Param(value = "userId") long userId,
			@Param(value = "type") int type,
			@Param(value = "status") int status,
			@Param(value = "page") PageInfo page);

	public List<Transfer> findCurrentDateTransfer();

	public void confirmTransfer(@Param(value = "id") long id);

	public List<Transfer> findAllSolvingTransfer(
			@Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

}
