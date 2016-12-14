package com.dingdong.register.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dingdong.conf.DDPageInfo;
import com.dingdong.register.mapper.DeptMapper;
import com.dingdong.register.mapper.DoctorHospitalMapper;
import com.dingdong.register.mapper.DoctorMapper;
import com.dingdong.register.mapper.HospitalDeptMapper;
import com.dingdong.register.mapper.HospitalMapper;
import com.dingdong.register.model.Dept;
import com.dingdong.register.model.Doctor;
import com.dingdong.register.model.DoctorHospital;
import com.dingdong.register.model.Hospital;
import com.dingdong.register.model.HospitalDept;
import com.dingdong.register.service.HospitalDeptService;
import com.dingdong.register.vo.response.DeptResponse;
import com.dingdong.register.vo.response.HospitalDeptResponse;
import com.github.pagehelper.PageHelper;

/**
 * 
 * @author ChanLueng
 * 
 */
@Service
@Transactional
public class HospitalDeptServiceImpl implements HospitalDeptService {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(HospitalDeptServiceImpl.class);
	@Autowired
	private HospitalDeptMapper hospitalDeptMapper;

	@Autowired
	private HospitalMapper hospitalMapper;

	@Autowired
	private DeptMapper deptMapper;

	@Autowired
	private DoctorHospitalMapper doctorHospitalMapper;

	@Autowired
	private DoctorMapper doctorMapper;

	@Override
	public HospitalDeptResponse findByHospitalId(long hospitalId,
			DDPageInfo<HospitalDept> pageInfo) {
		HospitalDeptResponse response = new HospitalDeptResponse();
		PageHelper.startPage(pageInfo.getPage(), pageInfo.getSize(), true);
		// PageHelper.orderBy(pageInfo.getOrderBy());
		pageInfo.setPageInfo(hospitalDeptMapper.findByHospitalId(hospitalId));

		response.setHospitalDepts(pageInfo.getPageInfo().getResult());
		response.setPages(pageInfo.getPages());
		response.setTotal(pageInfo.getTotal());

		return response;
	}

	@Override
	public HospitalDeptResponse addHospitalDept(long hospitalId, String deptStr) {
		HospitalDeptResponse response = new HospitalDeptResponse();
		List<HospitalDept> list = new ArrayList<HospitalDept>();
		deptStr = deptStr.trim();
		String[] hosStrs = deptStr.split(",");

		for (String str : hosStrs) {
			Long deptId = Long.parseLong(str);

			HospitalDept hospitalDept = new HospitalDept();

			hospitalDept.setHospitalId(hospitalId);
			Hospital hospital = hospitalMapper.findById(hospitalId);
			hospitalDept.setHospitalName(hospital.getName());
			hospitalDept.setDeptId(deptId);
			Dept dept = deptMapper.findById(deptId);
			hospitalDept.setDeptName(dept.getName());

			list.add(hospitalDept);
		}
		hospitalDeptMapper.addHospitalDepts(list);

		response.setHospitalDepts(list);

		return response;
	}

	@Override
	public HospitalDeptResponse deleteById(long id) {
		HospitalDeptResponse responseBody = new HospitalDeptResponse();
		hospitalDeptMapper.deleteById(id);

		return responseBody;
	}

	@Override
	public DeptResponse getHospitalDeptRemain(long hospitalId, String filterText) {
		DeptResponse response = new DeptResponse();
		List<Dept> depts = deptMapper.findAllDepts(filterText, null);
		List<HospitalDept> hospitalDepts = hospitalDeptMapper
				.findByHospitalId(hospitalId);
		List<Long> hospitalDeptIds = new ArrayList<Long>();
		for (HospitalDept hospitalDept : hospitalDepts) {
			hospitalDeptIds.add(hospitalDept.getDeptId());
		}
		Iterator<Dept> itr = depts.iterator();
		while (itr.hasNext()) {
			Dept dept = itr.next();
			if (hospitalDeptIds.contains(dept.getId())) {
				itr.remove();
			}
		}
		response.setDepts(depts);
		return response;
	}

	@Override
	public HospitalDeptResponse findDeptTotalByHospitalId(long hospitalId) {
		HospitalDeptResponse response = new HospitalDeptResponse();

		List<HospitalDept> hospitalDepts = hospitalDeptMapper
				.findByHospitalId(hospitalId);
		for (HospitalDept hospitalDept : hospitalDepts) {
			List<DoctorHospital> doctorHospitals = doctorHospitalMapper
					.findByHospitalAndDept(hospitalDept.getHospitalId(),
							hospitalDept.getDeptId());
			if (doctorHospitals != null) {
				for (DoctorHospital doctorHospital : doctorHospitals) {
					long doctorId = doctorHospital.getDoctorId();
					if (doctorId > 0) {
						Doctor doctor = doctorMapper.findById(doctorId);
						if (doctor != null) {
							doctorHospital.setDoctor(doctor);
						}
					}
				}
				hospitalDept.setDoctorHospitals(doctorHospitals);
				hospitalDept.setTotal(doctorHospitals.size());
			}
		}

		response.setHospitalDepts(hospitalDepts);

		return response;
	}

	@Override
	public HospitalDeptResponse findById(long id) {
		HospitalDeptResponse response = new HospitalDeptResponse();
		List<HospitalDept> hospitalDepts = new ArrayList<>();
		hospitalDepts.add(this.hospitalDeptMapper.findById(id));
		response.setHospitalDepts(hospitalDepts);
		return response;
	}

	@Override
	public HospitalDeptResponse findByHospitalIdAndDeptId(long hospitalId,
			long deptId) {
		HospitalDeptResponse response = new HospitalDeptResponse();
		List<HospitalDept> hospitalDepts = new ArrayList<>();
		hospitalDepts.add(this.hospitalDeptMapper.findByHospitalIdAndDeptId(
				hospitalId, deptId));
		response.setHospitalDepts(hospitalDepts);
		return response;
	}
}
