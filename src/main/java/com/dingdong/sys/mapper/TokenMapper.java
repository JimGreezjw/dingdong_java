package com.dingdong.sys.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dingdong.sys.model.Token;

/**
 * 
 * @author ChanLueng
 * 
 */
@Repository
public interface TokenMapper {

	public Token findById(@Param(value = "id") long id);

	public Token findByTokenId(@Param(value = "tokenId") String tokenId);

	public void addToken(@Param(value = "token") Token token);

	public void deleteById(@Param(value = "id") long id);

	public Token findByUserIdAndUserAgent(@Param(value = "userId") long userId,
			@Param(value = "userAgent") String userAgent);

	public void updateTokenDeadline(@Param(value = "id") long id,
			@Param(value = "deadline") Date deadline);

}
