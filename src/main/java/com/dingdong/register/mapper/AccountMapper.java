package com.dingdong.register.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dingdong.register.model.Account;

/**
 * 
 * 
 */
@Repository
public interface AccountMapper {

	public Account findById(@Param(value = "id") long id);

	public void addAccount(@Param(value = "account") Account account);

	public void updateAccount(@Param(value = "account") Account account);

	public void deleteById(@Param(value = "id") long id);

	public List<Account> findByUserId(Long userId);

}
