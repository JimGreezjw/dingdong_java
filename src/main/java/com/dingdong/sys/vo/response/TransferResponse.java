package com.dingdong.sys.vo.response;

import java.util.List;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.sys.model.Transfer;

/**
 * 医生信息响应对象
 * 
 * @author chenliang
 * 
 */
public class TransferResponse extends ResponseBody {
	/**
	 * 返回的消息
	 */
	private List<Transfer> transfers;

	public List<Transfer> getTransfers() {
		return transfers;
	}

	public void setTransfers(List<Transfer> transfers) {
		this.transfers = transfers;
	}

}
