package com.dingdong.register.service;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.conf.DDPageInfo;
import com.dingdong.register.model.Hospital;
import com.dingdong.register.vo.request.HospitalRequest;
import com.dingdong.register.vo.response.HospitalResponse;

public interface HospitalService {

	public HospitalResponse findAllHospitals(String filterText,
			DDPageInfo<Hospital> pageInfo);

	public HospitalResponse findHospitalById(long id);

	public HospitalResponse addHospital(HospitalRequest request);

	public HospitalResponse findAllHospitalDepts();

	public HospitalResponse updateHospitalBySelective(HospitalRequest request);

	public ResponseBody delHospitalById(long id);

	public HospitalResponse findHospitalByOpTele(String opTele);

	/**
	 * 按照状态查询医院
	 * 
	 * @param status
	 * @return
	 */
	public HospitalResponse findByStatus(int status);
}
