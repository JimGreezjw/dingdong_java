package com.dingdong.sys.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.conf.DDPageInfo;
import com.dingdong.conf.PageInfo;
import com.dingdong.conf.PageUtils;
import com.dingdong.sys.model.Transfer;
import com.dingdong.sys.service.TransferService;
import com.dingdong.sys.vo.response.TransferResponse;

/**
 * 
 * @author ChanLueng
 * 
 */
@Controller
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
@Api(value = "转账信息")
public class TransferController {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(TransferController.class);
	@Autowired
	private TransferService TransferService;

	@ApiOperation(value = "获得所有转账信息", notes = "type=0 表示余额， =1表示 积分.")
	@RequestMapping(value = "users/{userId}/Transfers", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<TransferResponse> getAllTransfers(
			@ApiParam(value = "用户编号") @PathVariable(value = "userId") long userId,
			@ApiParam(value = "类型", required = true, defaultValue = "0") @RequestParam(value = "type", required = false, defaultValue = "0") int type,
			@ApiParam(value = "分页-每页大小", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) @RequestParam(value = "size", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) Integer size,
			@ApiParam(value = "分页-当前页数", required = false, defaultValue = PageUtils.PAGE_NUM_STR) @RequestParam(value = "page", required = false, defaultValue = PageUtils.PAGE_NUM_STR) Integer page) {
		PageInfo pageInfo = new PageInfo(page, size, null, null);
		TransferResponse response = this.TransferService.getAccountDetails(
				userId, type, pageInfo);
		return new ResponseEntity<TransferResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "获得转账信息", notes = "获得转账信息-获得指定id转账信息")
	@RequestMapping(value = "/Transfers/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<TransferResponse> getTransferById(
			@PathVariable(value = "id") Long id) {
		TransferResponse response = this.TransferService.findById(id);
		return new ResponseEntity<TransferResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "提交提现申请", notes = "获得提现申请详细信息")
	@RequestMapping(value = "/transfers/getCashApply", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity<TransferResponse> getCashApply(
			@ApiParam(value = "账户Id", required = true) @RequestParam(value = "accountId", required = true) Long accountId,
			@ApiParam(value = "提现金额", required = true) @RequestParam(value = "amount", required = true) Float amount) {
		TransferResponse response = this.TransferService.addGetCashApply(
				accountId, amount);
		return new ResponseEntity<TransferResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "确认提现申请", notes = "获得提现申请详细信息")
	@RequestMapping(value = "/transfers/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.PATCH)
	public ResponseEntity<ResponseBody> getCashApply(
			@PathVariable(value = "id") Long id) {
		ResponseBody response = this.TransferService.confirmTransfer(id);
		return new ResponseEntity<ResponseBody>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "查询待处理的交易(带分页)", notes = "获得尚待处理的交易详细信息")
	@RequestMapping(value = "/transfers/unSolvingTransfersWithPage", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<TransferResponse> findSolvingTransfer(
			@ApiParam(value = "开始日期", required = false) @RequestParam(value = "beginDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date beginDate,
			@ApiParam(value = "结束日期", required = false) @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@ApiParam(value = "分页-每页大小", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) @RequestParam(value = "size", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) Integer size,
			@ApiParam(value = "分页-当前页数", required = false, defaultValue = PageUtils.PAGE_NUM_STR) @RequestParam(value = "page", required = false, defaultValue = PageUtils.PAGE_NUM_STR) Integer page) {
		DDPageInfo<Transfer> pageInfo = new DDPageInfo<Transfer>(page, size,
				"", "");
		TransferResponse response = TransferService.findSolvingTransfer(
				beginDate, endDate, pageInfo);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
