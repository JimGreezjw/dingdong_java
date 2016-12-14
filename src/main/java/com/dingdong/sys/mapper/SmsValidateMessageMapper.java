package com.dingdong.sys.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dingdong.sys.model.SmsValidateMessageVO;

@Repository
public interface SmsValidateMessageMapper {

	/**
	 * 根据手机号码查找发送过的信息
	 * 
	 * @param mobileNo
	 * @return
	 */
	public List<SmsValidateMessageVO> findMessageVoByMobileNo(
			@Param(value = "mobileNo") String mobileNo);

	/**
	 * 将信息插入数据库中进行持久化
	 * 
	 * @param messageVO
	 */
	public void insertMessageVO(
			@Param(value = "messageVO") SmsValidateMessageVO messageVO);

	/**
	 * 进行信息的更新
	 * 
	 * @param messageVO
	 */
	public void updateMessageVO(
			@Param(value = "messageVO") SmsValidateMessageVO messageVO);

	/**
	 * 清除失效的信息
	 * 
	 * @param expireTime
	 */
	public void removeExpireMessage(@Param(value = "expireTime") Date expireTime);
}
