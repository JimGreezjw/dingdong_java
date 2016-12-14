package com.dingdong.sys.service;

import com.dingdong.sys.vo.response.TokenResponse;

public interface TokenService {

	public TokenResponse validateToken(Long userId, String tokenId,
			String userAgent);

	public String generateToken(long userId, String userAgent);
}
