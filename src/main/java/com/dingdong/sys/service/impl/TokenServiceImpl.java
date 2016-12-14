package com.dingdong.sys.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dingdong.register.mapper.HospitalMapper;
import com.dingdong.register.model.Hospital;
import com.dingdong.sys.exception.SysErrorCode;
import com.dingdong.sys.exception.TokenErrorCode;
import com.dingdong.sys.mapper.TokenMapper;
import com.dingdong.sys.mapper.UserMapper;
import com.dingdong.sys.model.Token;
import com.dingdong.sys.model.User;
import com.dingdong.sys.service.TokenService;
import com.dingdong.sys.vo.response.TokenResponse;

@Service
@Transactional
public class TokenServiceImpl implements TokenService {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(TokenServiceImpl.class);

	@Autowired
	private TokenMapper tokenMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private HospitalMapper hospitalMapper;

	private final int DEFAULT_DELAY_TIME = 360;

	@Override
	public TokenResponse validateToken(Long userId, String tokenId,
			String userAgent) {
		TokenResponse response = new TokenResponse();
		List<Token> tokens = new ArrayList<Token>();

		if (userId == null || StringUtils.isBlank(tokenId)
				|| StringUtils.isBlank(userAgent)) {
			response.setErrorCode(TokenErrorCode.TOKEN_INFORMATION_INCOMPLETE);
			return response;
		}
		User user = userMapper.findById(userId);
		Token token = tokenMapper.findByTokenId(tokenId);
		if (null != user && null != token) {
			if (User.Role.HOSPITALADMIN.getId() == user.getRole()) {// 如果是医院运维
				long hospitalId = user.getOpHospitalId();
				if (hospitalId != 0) {
					Hospital hospital = hospitalMapper.findById(hospitalId);
					if (hospital != null) {
						user.setHospital(hospital);
					} else {
						response.setErrorCode(SysErrorCode.OP_HOSPITAL_NOT_EXIST);
						return response;
					}
				} else {
					response.setErrorCode(SysErrorCode.OP_HOSPITAL_NOT_EXIST);
					return response;
				}
			}

			boolean isTheUser = userId.equals(token.getUserId());
			boolean isTheAgent = userAgent.equals(token.getUserAgent());
			Date now = new Date();

			boolean isValid = now.before(token.getDeadline());
			if (!isValid) { // token过期，设置该token无效
				tokenMapper.deleteById(token.getId());
			}
			if (!isTheUser || !isTheAgent || !isValid) {
				response.setErrorCode(TokenErrorCode.TOKEN_INFORMATION_ERROR);
				return response;
			}
		} else {
			response.setErrorCode(TokenErrorCode.TOKEN_INFORMATION_INCOMPLETE);
			return response;
		}
		token.setUser(user);
		tokens.add(token);
		return response.setTokens(tokens);
	}

	@Override
	public String generateToken(long userId, String userAgent) {

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, DEFAULT_DELAY_TIME);
		Date now = new Date();
		Date newDeadLine = calendar.getTime();

		Token token = tokenMapper.findByUserIdAndUserAgent(userId, userAgent);
		if (token == null) {
			token = new Token();
			token.setUserId(userId);
			token.setTokenId(UUID.randomUUID().toString());
			token.setUserAgent(userAgent);
			token.setDeadline(newDeadLine);
			token.setCreateTime(now);
			this.tokenMapper.addToken(token);
		} else {
			token.setDeadline(newDeadLine);
			this.tokenMapper.updateTokenDeadline(token.getId(), newDeadLine);
		}
		return token.getTokenId();

	}
}
