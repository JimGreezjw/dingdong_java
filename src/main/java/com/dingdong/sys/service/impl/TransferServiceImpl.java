package com.dingdong.sys.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dingdong.common.exception.LockFailureException;
import com.dingdong.common.vo.ResponseBody;
import com.dingdong.conf.DDPageInfo;
import com.dingdong.conf.PageInfo;
import com.dingdong.register.mapper.AccountMapper;
import com.dingdong.register.model.Account;
import com.dingdong.sys.exception.SysErrorCode;
import com.dingdong.sys.exception.TransferErrorCode;
import com.dingdong.sys.mapper.TransferMapper;
import com.dingdong.sys.mapper.UserMapper;
import com.dingdong.sys.model.Transfer;
import com.dingdong.sys.model.Transfer.Status;
import com.dingdong.sys.model.Transfer.Type;
import com.dingdong.sys.model.User;
import com.dingdong.sys.service.TransferService;
import com.dingdong.sys.vo.response.TransferResponse;
import com.github.pagehelper.PageHelper;

/**
 * 
 * @author ChanLueng
 * 
 */
@Service
@Transactional
public class TransferServiceImpl implements TransferService {
	private static final Logger LOG = LoggerFactory
			.getLogger(TransferServiceImpl.class);
	@Autowired
	private TransferMapper transferMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private AccountMapper accountMapper;

	@Override
	public ResponseBody executeTransferBalance(long userId, float amount,
			int reason) {

		LOG.info("updateTransferSuccess userId={}, amount={}", userId, amount);
		ResponseBody response = new ResponseBody();
		Transfer transfer = new Transfer();

		transfer.setFromUserName("叮咚门诊");
		transfer.setAmount(amount);
		transfer.setUserId(userId);

		User user = userMapper.findById(userId);
		if (user == null) {
			response.setErrorCode(SysErrorCode.USER_ID_NOT_EXIST);
			return response;
		}
		transfer.setUserName(user.getName());
		// 先使用数字
		transfer.setType(Type.BALANCE.getId());
		transfer.setStatus(Status.SUCCESS.getId());
		transfer.setReason(reason);

		transfer.setCreateTime(new Date());

		transferMapper.addTransfer(transfer);
		// 账户金额变更
		int rows = userMapper.addBalance(userId, amount, user.getBalance());
		if (rows < 1)
			throw new LockFailureException("无法锁定用户账户金额");

		return response;
	}

	@Override
	public ResponseBody executeTransferScore(long userId, int score, int reason) {
		LOG.info("updateTransferSuccess userId={}, score={}", userId, score);
		ResponseBody response = new ResponseBody();
		Transfer transfer = new Transfer();

		transfer.setFromUserName("叮咚门诊");
		transfer.setAmount(score);
		transfer.setUserId(userId);
		transfer.setReason(reason);

		User user = userMapper.findById(userId);
		if (user == null) {
			response.setErrorCode(SysErrorCode.USER_ID_NOT_EXIST);
			return response;
		}
		transfer.setUserName(user.getName());
		// 先使用数字
		transfer.setType(Type.SCORE.getId());

		transfer.setStatus(Status.SUCCESS.getId());
		transfer.setCreateTime(new Date());
		transferMapper.addTransfer(transfer);
		// 账户金额变更
		int rows = userMapper.addScore(userId, score, user.getScore());
		if (rows < 1)
			throw new LockFailureException("无法锁定用户账户积分");

		return response;
	}

	@Override
	public ResponseBody updateTransferSuccess(long id, String transactionId) {

		ResponseBody response = new ResponseBody();
		Transfer transfer = this.transferMapper.findById(id);
		LOG.info("updateTransferSuccess id={}, transactionId={}", id,
				transactionId);
		if (transfer != null) {
			if (transfer.getStatus() == 0) {
				LOG.info("updateTransferSuccess begin to updateTransferSuccess**************************");
				int rows = this.transferMapper.updateTransferSuccess(id,
						transactionId, new Date());
				LOG.info("**************************update rows =  " + rows);
				if (rows > 0) {// 更新成功的情况下，才更新用户账户余额
					LOG.info("************updateTransferSuccess begin to addBalance，the amount is "
							+ transfer.getAmount()
							+ ", the userId is  "
							+ transfer.getUserId());
					User user = this.userMapper.findById(transfer.getUserId());
					if (null == user) {
						response.setErrorCode(SysErrorCode.USER_ID_NOT_EXIST);
						return response;
					}

					rows = this.userMapper.addBalance(transfer.getUserId(),
							transfer.getAmount(), user.getBalance());
					if (rows < 1)
						throw new LockFailureException("无法锁定用户账户金额");
					LOG.info("**************************update user rows =  "
							+ rows);
				}
			}
		}

		return response;
	}

	@Override
	public TransferResponse getAccountDetails(long userId, int type,
			PageInfo page) {
		// TODO Auto-generated method stub
		int status = Transfer.Status.SUCCESS.getId();
		TransferResponse transferResponse = new TransferResponse();
		transferResponse.setTransfers(this.transferMapper
				.findByUserIdTypeStatus(userId, type, status, page));
		return transferResponse;
	}

	@Override
	public TransferResponse findById(long id) {
		TransferResponse transferResponse = new TransferResponse();
		Transfer t = transferMapper.findById(id);
		List<Transfer> transferList = new ArrayList<Transfer>();
		transferList.add(t);
		transferResponse.setTransfers(transferList);
		return transferResponse;
	}

	@Override
	public TransferResponse addGetCashApply(Long accountId, Float amount) {
		TransferResponse response = new TransferResponse();
		Account account = accountMapper.findById(accountId);
		if (account == null) {
			response.setErrorCode(SysErrorCode.ACCOUNT_NOT_EXIST);
			return response;
		}

		User user = userMapper.findById(account.getUserId());
		if (user == null) {
			response.setErrorCode(SysErrorCode.USER_ID_NOT_EXIST);
			return response;
		}

		if (Math.abs(amount) > Transfer.MAX_CASH) {
			response.setErrorCode(TransferErrorCode.TRANSFER_GETCASH_TOO_MUCH);
			return response;
		}

		List<Transfer> t = transferMapper.findCurrentDateTransfer();

		if (t != null && t.size() > 0) {
			response.setErrorCode(TransferErrorCode.TRANSFER_GETCASH_ALREADY_TODAY);
			return response;
		}

		if (amount + user.getBalance() < 0) {
			response.setErrorCode(TransferErrorCode.TRANSFER_BALANCE_INSUFFICIENT);
			return response;
		}

		Transfer transfer = new Transfer();

		transfer.setFromUserName("叮咚门诊");
		transfer.setAmount(amount);
		transfer.setUserId(account.getUserId());
		transfer.setAccountId(accountId);
		transfer.setUserName(user.getName());
		// 先使用数字
		transfer.setType(Type.CASH.getId());
		transfer.setReason(Transfer.Reason.YHTX.getId());
		transfer.setStatus(Status.DRAFT.getId());
		transfer.setCreateTime(new Date());
		transferMapper.addTransfer(transfer);

		// 账户金额变更
		int rows = userMapper.addBalance(account.getUserId(), amount,
				user.getBalance());
		if (rows < 1)
			throw new LockFailureException("无法锁定用户账户金额");

		List<Transfer> transferList = new ArrayList<Transfer>();
		transferList.add(transfer);
		response.setTransfers(transferList);
		return response;
	}

	@Override
	public ResponseBody confirmTransfer(long id) {
		TransferResponse transferResponse = new TransferResponse();
		transferMapper.confirmTransfer(id);
		return transferResponse;
	}

	@Override
	public TransferResponse findSolvingTransfer(Date beginDate, Date endDate,
			DDPageInfo<Transfer> pageInfo) {
		TransferResponse response = new TransferResponse();

		PageHelper.startPage(pageInfo.getPage(), pageInfo.getSize(), true);

		pageInfo.setPageInfo(transferMapper.findAllSolvingTransfer(beginDate,
				endDate));
		List<Transfer> transfers = pageInfo.getPageInfo().getResult();
		for (Transfer transfer : transfers) {
			if (transfer.getAccountId() > 0) {
				Account account = accountMapper.findById(transfer
						.getAccountId());
				transfer.setAccount(account);
			}
		}
		response.setTransfers(transfers);
		response.setPages(pageInfo.getPages());
		response.setTotal(pageInfo.getTotal());
		return response;
	}
}
