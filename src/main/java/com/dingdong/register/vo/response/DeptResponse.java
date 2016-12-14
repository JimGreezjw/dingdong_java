package com.dingdong.register.vo.response;

import java.util.List;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.model.Dept;

/**
 * 医院信息响应对象
 * 
 * @author chenliang
 * 
 */
public class DeptResponse extends ResponseBody {

	private List<Dept> depts;

	public List<Dept> getDepts() {
		return depts;
	}

	public DeptResponse setDepts(List<Dept> depts) {
		this.depts = depts;
		return this;
	}
}
