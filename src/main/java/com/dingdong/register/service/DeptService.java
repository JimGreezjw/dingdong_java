package com.dingdong.register.service;

import com.dingdong.conf.PageInfo;
import com.dingdong.register.vo.response.DeptResponse;
import com.dingdong.register.vo.response.DoctorResponse;

public interface DeptService {

	public DeptResponse findAllDepts(String filterText, PageInfo pageInfo);

	public DeptResponse findDeptById(long id);

	public DeptResponse findDeptByParentId(long partentId);

	/**
	 * 获取顶层科室的信息
	 * 
	 * @return
	 */
	public DeptResponse findTopDepts();

	/**
	 * 获取父科室全部层级的子科室
	 * 
	 * @param parentId
	 * @return
	 */
	public DeptResponse findRecursiveSubDepts(long parentId);

	/**
	 * 获取科室（包含子科室）下面全部的医生信息
	 * 
	 * @param id
	 * @return
	 */
	public DoctorResponse findRecursiveDoctors(long id);
}
