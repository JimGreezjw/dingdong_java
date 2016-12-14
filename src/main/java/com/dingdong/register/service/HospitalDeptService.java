package com.dingdong.register.service;

import com.dingdong.conf.DDPageInfo;
import com.dingdong.register.model.HospitalDept;
import com.dingdong.register.vo.response.DeptResponse;
import com.dingdong.register.vo.response.HospitalDeptResponse;

public interface HospitalDeptService {

	/**
	 * 按医院查找
	 * 
	 * @param hospitalId
	 * @return
	 */
	public HospitalDeptResponse findByHospitalId(long hospitalId,
			DDPageInfo<HospitalDept> pageInfo);

	public HospitalDeptResponse addHospitalDept(long hospitalId, String deptStr);

	public HospitalDeptResponse deleteById(long id);

	public DeptResponse getHospitalDeptRemain(long idhospitalId,
			String filterText);

	public HospitalDeptResponse findDeptTotalByHospitalId(long hospitalId);

	public HospitalDeptResponse findById(long id);

	public HospitalDeptResponse findByHospitalIdAndDeptId(long hospitalId,
			long deptId);

}
