package com.dingdong.sys.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dingdong.sys.model.CommonMessageVO;

/**
 * 一般短信的接口
 * 
 * @author niukai
 * @created on December 27th, 2015
 * 
 */
@Repository
public interface CommonMessageMapper {

	/**
	 * 将信息插入数据库中进行持久化
	 * 
	 * @param messageVO
	 */
	public void insertMessageVO(
			@Param(value = "messageVO") CommonMessageVO messageVO);
}
