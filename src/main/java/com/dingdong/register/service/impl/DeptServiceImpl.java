package com.dingdong.register.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dingdong.conf.PageInfo;
import com.dingdong.register.mapper.DeptMapper;
import com.dingdong.register.mapper.DoctorMapper;
import com.dingdong.register.model.Dept;
import com.dingdong.register.model.Doctor;
import com.dingdong.register.service.DeptService;
import com.dingdong.register.vo.response.DeptResponse;
import com.dingdong.register.vo.response.DoctorResponse;

/**
 * 
 * @author ChanLueng
 * 
 */
@Service
@Transactional
public class DeptServiceImpl implements DeptService {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(DeptServiceImpl.class);
	@Autowired
	private DeptMapper deptMapper;
	@Autowired
	private DoctorMapper doctorMapper;

	@Override
	public DeptResponse findAllDepts(String filterText, PageInfo pageInfo) {
		DeptResponse response = new DeptResponse();
		response.setDepts(this.deptMapper.findAllDepts(filterText, pageInfo));
		return response;
	}

	@Override
	public DeptResponse findDeptById(long id) {
		DeptResponse response = new DeptResponse();
		List<Dept> depts = new ArrayList<>();
		depts.add(this.deptMapper.findById(id));
		response.setDepts(depts);
		return response;
	}

	@Override
	public DeptResponse findDeptByParentId(long parentId) {
		DeptResponse response = new DeptResponse();
		List<Dept> depts = this.deptMapper.findByParentId(parentId);
		response.setDepts(depts);
		return response;
	}

	@Override
	public DeptResponse findTopDepts() {
		DeptResponse response = new DeptResponse();
		List<Dept> depts = this.deptMapper.findTopDepts();
		response.setDepts(depts);

		// 设置总数量
		if (depts != null) {
			response.setTotal((long) depts.size());
		}

		return response;
	}

	@Override
	public DeptResponse findRecursiveSubDepts(long parentId) {
		DeptResponse response = new DeptResponse();

		Dept dept = this.deptMapper.findById(parentId);
		if (dept == null) {
			return response;
		}

		String parentDeptOutline = dept.getDeptOutline();
		List<Dept> depts = this.deptMapper
				.findRecursiveSubDepts(parentDeptOutline);
		response.setDepts(depts);

		// 设置总数量
		if (depts != null) {
			response.setTotal((long) depts.size());
		}

		return response;
	}

	@Override
	public DoctorResponse findRecursiveDoctors(long id) {
		DoctorResponse response = new DoctorResponse();
		Dept dept = this.deptMapper.findById(id);
		if (dept == null) {
			return response;
		}

		String deptOutline = dept.getDeptOutline();
		List<Doctor> doctors = this.doctorMapper.findByDeptOutline(deptOutline);

		response.setDoctors(doctors);
		if (doctors != null) {
			response.setTotal((long) doctors.size());
		}

		return response;
	}
}
