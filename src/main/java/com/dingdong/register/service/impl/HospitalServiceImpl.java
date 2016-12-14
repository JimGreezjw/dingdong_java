package com.dingdong.register.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.conf.DDPageInfo;
import com.dingdong.register.exception.DoctorErrorCode;
import com.dingdong.register.exception.HospitalErrorCode;
import com.dingdong.register.mapper.HospitalDeptMapper;
import com.dingdong.register.mapper.HospitalMapper;
import com.dingdong.register.model.Hospital;
import com.dingdong.register.model.HospitalDept;
import com.dingdong.register.service.HospitalService;
import com.dingdong.register.vo.request.HospitalRequest;
import com.dingdong.register.vo.response.HospitalResponse;
import com.dingdong.sys.mapper.UserMapper;
import com.dingdong.sys.model.User;
import com.github.pagehelper.PageHelper;

/**
 * 
 * @author ChanLueng
 * 
 */
@Service
@Transactional
public class HospitalServiceImpl implements HospitalService {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(HospitalServiceImpl.class);
	@Autowired
	private HospitalMapper hospitalMapper;

	@Autowired
	private HospitalDeptMapper hospitalDeptMapper;

	@Autowired
	private UserMapper userMapper;

	@Override
	public HospitalResponse findAllHospitals(String filterText,
			DDPageInfo<Hospital> pageInfo) {
		HospitalResponse response = new HospitalResponse();
		PageHelper.startPage(pageInfo.getPage(), pageInfo.getSize(), true);
		PageHelper.orderBy(pageInfo.getOrderBy());
		pageInfo.setPageInfo(hospitalMapper.findAllHospitals(filterText));

		response.setHospitals(pageInfo.getPageInfo().getResult());
		response.setPages(pageInfo.getPages());
		response.setTotal(pageInfo.getTotal());
		return response;
	}

	@Override
	public HospitalResponse findHospitalById(long id) {
		HospitalResponse response = new HospitalResponse();
		List<Hospital> hospitals = new ArrayList<>();
		hospitals.add(this.hospitalMapper.findById(id));
		response.setHospitals(hospitals);
		return response;
	}

	@Override
	public HospitalResponse addHospital(HospitalRequest request) {
		HospitalResponse response = new HospitalResponse();
		List<Hospital> hospitals = request.getHospitals();
		for (Hospital hospital : hospitals) {
			String opTele = hospital.getOpTele();
			if (StringUtils.isNotBlank(opTele)) {
				List<Hospital> hospitalsList = hospitalMapper
						.findByOpTele(opTele);
				if (hospitalsList != null && hospitalsList.size() > 0) {
					response.setErrorCode(HospitalErrorCode.OP_TELE_ALREADY_EXIST);
					return response;
				}
			}

			if (hospital.getIntroduction().length() > 1000) {
				response.setErrorCode(HospitalErrorCode.HOSPITAL_INTRODUCE_EXCEED);
				return response;
			}

			this.hospitalMapper.addHospital(hospital);
			User user = new User();
			user.setName(hospital.getName() + "运维");
			user.setOpHospitalId(hospital.getId());
			user.setPhone(opTele);
			user.setRole(User.Role.HOSPITALADMIN.getId());
			user.setStatus(User.Status.ACTIVATED.getValue());
			userMapper.addUser(user);

		}
		response.setHospitals(hospitals);

		return response;
	}

	@Override
	public HospitalResponse findAllHospitalDepts() {
		HospitalResponse response = new HospitalResponse();
		List<Hospital> hospitals = hospitalMapper.findAllHospitals(null);
		for (Hospital hospital : hospitals) {
			List<HospitalDept> hospitalDepts = hospitalDeptMapper
					.findByHospitalId(hospital.getId());
			if (hospitalDepts != null) {
				hospital.setHospitalDepts(hospitalDepts);
			}
		}
		response.setHospitals(hospitals);
		return response;
	}

	@Override
	public HospitalResponse updateHospitalBySelective(HospitalRequest request) {
		HospitalResponse response = new HospitalResponse();
		List<Hospital> hospitals = request.getHospitals();
		for (Hospital hospital : hospitals) {
			String opTele = hospital.getOpTele();
			if (hospital.getIntroduction().length() > 1000) {
				response.setErrorCode(HospitalErrorCode.HOSPITAL_INTRODUCE_EXCEED);
				return response;
			}

			if (StringUtils.isNotBlank(opTele)) {
				List<Hospital> hospitalsList = hospitalMapper
						.findByOpTele(opTele);
				if (hospitalsList != null && hospitalsList.size() > 0
						&& hospitalsList.get(0).getId() != hospital.getId()) {
					response.setErrorCode(HospitalErrorCode.OP_TELE_ALREADY_EXIST);
					return response;
				}
			}
			Hospital originHospital = hospitalMapper.findById(hospital.getId());
			if (StringUtils.isNotBlank(originHospital.getOpTele())) {
				User user = userMapper.findByPhoneAndRole(
						originHospital.getOpTele(),
						User.Role.HOSPITALADMIN.getId());
				if (user != null) {
					user.setPhone(opTele);
					userMapper.updateUser(user);
				}
			}
			hospitalMapper.updateBySelective(hospital);
		}
		response.setHospitals(hospitals);
		return response;
	}

	@Override
	public ResponseBody delHospitalById(long id) {
		HospitalResponse response = new HospitalResponse();
		hospitalMapper.deleteById(id);
		return response;
	}

	@Override
	public HospitalResponse findHospitalByOpTele(String opTele) {
		HospitalResponse response = new HospitalResponse();
		List<Hospital> hospitals = this.hospitalMapper.findByOpTele(opTele);
		response.setHospitals(hospitals);
		if (hospitals == null || hospitals.size() == 0) {
			response.setErrorCode(HospitalErrorCode.HOSPITAL_NOT_EXIST);
		}

		return response;
	}

	@Override
	public HospitalResponse findByStatus(int status) {
		HospitalResponse response = new HospitalResponse();
		List<Hospital> hospitals = this.hospitalMapper.findByStatus(status);
		response.setHospitals(hospitals);

		return response;
	}

}
