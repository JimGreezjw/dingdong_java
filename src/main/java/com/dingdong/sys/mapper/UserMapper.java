package com.dingdong.sys.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dingdong.sys.model.User;

/**
 * 
 * @author ChanLueng
 * 
 */
@Repository
public interface UserMapper {

	public List<User> findAllUsers();

	public User findById(@Param(value = "id") long id);

	/**
	 * 根据手机号号码查询
	 * 
	 * @param mobileNo
	 * @return
	 */
	public List<User> findByMobileNo(@Param(value = "mobileNo") String mobileNo);

	/**
	 * 按照用户ids查询
	 * 
	 * @param ids
	 * @return
	 */
	public List<User> findByIds(@Param(value = "ids") List<Long> ids);

	/*
	 * 通过微信OpenId搜索,支持多个公众号，依靠角色进行区分
	 */
	public User findUserByOpenId(@Param(value = "openId") String openId,
			@Param(value = "role") Integer role);

	public void addUser(@Param(value = "user") User user);

	public void addUsers(@Param(value = "users") List<User> users);

	public void updateUser(@Param(value = "user") User user);

	// 更新电话号码
	public int updateUserPhone(@Param(value = "id") long id,
			@Param(value = "phone") String phone);

	//
	/**
	 * 添加金额
	 * 
	 * @param id
	 * @param add_balance
	 *            金额增量
	 * @param expect_balance
	 *            更新前锁住的金额
	 * 
	 */
	public int addBalance(@Param(value = "id") long id,
			@Param(value = "add_balance") float add_balance,
			@Param(value = "expect_balance") float expect_balance);

	//
	/**
	 * 添加积分
	 * 
	 * @param id
	 * @param add_score
	 *            * 积分增量
	 * @param expect_balance
	 *            更新前锁住的积分
	 */
	public int addScore(@Param(value = "id") long id,
			@Param(value = "add_score") int add_score,
			@Param(value = "expect_score") float expect_score);

	public void deleteById(@Param(value = "id") long id);

	public void delectUsers(List<Integer> idList);

	public User findByPhoneAndPassword(@Param(value = "phone") String phone,
			@Param(value = "password") String password);

	public User findByPhoneAndRole(@Param(value = "phone") String phone,
			@Param(value = "role") int role);

}
