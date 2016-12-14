package com.dingdong.register.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.conf.DDPageInfo;
import com.dingdong.register.exception.DoctorErrorCode;
import com.dingdong.register.mapper.DeptMapper;
import com.dingdong.register.mapper.DoctorHospitalMapper;
import com.dingdong.register.mapper.DoctorMapper;
import com.dingdong.register.mapper.HospitalMapper;
import com.dingdong.register.mapper.RegisterMapper;
import com.dingdong.register.mapper.ScheduleMapper;
import com.dingdong.register.model.Dept;
import com.dingdong.register.model.Doctor;
import com.dingdong.register.model.DoctorHospital;
import com.dingdong.register.model.Hospital;
import com.dingdong.register.model.Register;
import com.dingdong.register.model.Schedule;
import com.dingdong.register.service.DoctorHospitalService;
import com.dingdong.register.vo.request.DoctorHospitalRequest;
import com.dingdong.register.vo.response.DoctorHospitalResponse;
import com.github.pagehelper.PageHelper;

/**
 * 
 * @author ChanLueng
 * 
 */
@Service
@Transactional
public class DoctorHospitalServiceImpl implements DoctorHospitalService {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(DoctorHospitalServiceImpl.class);
	@Autowired
	private DoctorHospitalMapper doctorHospitalMapper;

	@Autowired
	private HospitalMapper hospitalMapper;

	@Autowired
	private ScheduleMapper scheduleMapper;

	@Autowired
	private RegisterMapper registerMapper;

	@Autowired
	private DoctorMapper doctorMapper;

	@Autowired
	private DeptMapper deptMapper;

	// @Override
	// public DoctorHospitalResponse findAllDoctorHospitals() {
	// DoctorHospitalResponse response = new DoctorHospitalResponse();
	// response.setDoctorHospitals(this.doctorHospitalMapper
	// .findAllDoctorHospitals());
	// return response;
	// }

	@Override
	public DoctorHospitalResponse findByDoctorId(long doctorId) {
		DoctorHospitalResponse response = new DoctorHospitalResponse();
		List<DoctorHospital> doctorHospitalList = this.doctorHospitalMapper
				.findByDoctorId(doctorId);
		response.setDoctorHospitals(doctorHospitalList);

		return response;
	}

	@Override
	public DoctorHospitalResponse findDoctorHospitalById(long id) {
		DoctorHospitalResponse response = new DoctorHospitalResponse();
		List<DoctorHospital> doctorHospitals = new ArrayList<>();
		doctorHospitals.add(this.doctorHospitalMapper.findById(id));
		response.setDoctorHospitals(doctorHospitals);
		return response;
	}

	private void fillRegisterInfo(long userId,
			List<DoctorHospital> doctorHospitals) {
		if (CollectionUtils.isEmpty(doctorHospitals))
			return;
		for (DoctorHospital doctorHospital : doctorHospitals) {
			fillRegisterInfo(userId, doctorHospital);
		}
	}

	private void fillRegisterInfo(long userId, DoctorHospital doctorHospital) {

		List<Register> registerList = registerMapper
				.findUnFinishedRegistersByUserIdDoctorIdHospitalId(userId,
						doctorHospital.getDoctorId(),
						doctorHospital.getHospitalId());

		if (CollectionUtils.isNotEmpty(registerList)) {

			Register register = registerList.get(registerList.size() - 1);
			if (Register.Status.QUEUE.getValue() == register.getStatus()) {
				doctorHospital
						.setRegisterStatus(DoctorHospital.RegisterStatus.QUEUED
								.getValue());// 标识已排队
				return;
			}

			else {
				doctorHospital
						.setRegisterStatus(DoctorHospital.RegisterStatus.REGISTERED// 已挂号
								.getValue());
				return;
			}
		}

		int availableCount = 0;
		int status = Schedule.Status.EFFECTIVE.getValue();
		Date beginDate = new Date();
		Date endDate = null;
		List<Schedule> scheduleList = this.scheduleMapper
				.findByDoctorIdHospitalIdBeginDateEndDateStatus(
						doctorHospital.getDoctorId(),
						doctorHospital.getHospitalId(), beginDate, endDate,
						status);
		if (CollectionUtils.isNotEmpty(scheduleList)) {
			for (Schedule schedule : scheduleList) {
				if (schedule.getIssueNum() > schedule.getRegisteredNum())
					availableCount += (schedule.getIssueNum() - schedule
							.getRegisteredNum());

			}
			doctorHospital.setAvailableCount(availableCount);
			doctorHospital
					.setRegisterStatus(DoctorHospital.RegisterStatus.CANREGISTER// 可挂号
							.getValue());// 标识已排队
			return;
		}

		// 缺省为未排队
		doctorHospital.setRegisterStatus(DoctorHospital.RegisterStatus.UNQUEUE// 可排队
				.getValue());
		return;

	}

	@Override
	public DoctorHospitalResponse addDoctorHospital(
			DoctorHospitalRequest request) {
		DoctorHospitalResponse response = new DoctorHospitalResponse();
		DoctorHospital doctorHospital = new DoctorHospital();

		Doctor doctor = doctorMapper.findById(request.getDoctorId());
		if (doctor == null) {
			response.setErrorCode(DoctorErrorCode.DOCTOR_ID_NOT_EXIST);
			return response;
		}

		doctorHospital.setDoctorId(request.getDoctorId());
		doctorHospital.setDoctorName(doctor.getName());

		Hospital hospital = hospitalMapper.findById(request.getHospitalId());
		if (hospital == null) {
			response.setErrorCode(DoctorErrorCode.HOSTPITAL_ID_NOT_EXIST);
			return response;
		}

		doctorHospital.setHospitalId(request.getHospitalId());
		doctorHospital.setHospitalName(hospital.getName());

		DoctorHospital dh = this.doctorHospitalMapper
				.findByDoctorIdAndHospitalId(doctorHospital.getDoctorId(),
						doctorHospital.getHospitalId());
		if (dh != null) {
			response.setErrorCode(DoctorErrorCode.DOCTOR_HOSPITAL_ALREADY_EXIST);
			return response;
		}

		if (request.getDeptId() != 0) {
			Dept dept = deptMapper.findById(request.getDeptId());
			doctorHospital.setDeptId(request.getDeptId());
			if (dept != null) {
				doctorHospital.setDeptName(dept.getName());
			}
		}
		doctorHospital.setMainFlag(request.getMainFlag());
		doctorHospital.setMinQueue(request.getMinQueue());
		doctorHospital.setRegisterFee(request.getRegisterFee());
		doctorHospital.setDeposit(request.getDeposit());
		this.doctorHospitalMapper.addDoctorHospital(doctorHospital);

		List<DoctorHospital> list = new ArrayList<DoctorHospital>();
		list.add(doctorHospital);
		response.setDoctorHospitals(list);

		return response;
	}

	@Override
	public ResponseBody updateDoctorHospital(long id,
			DoctorHospitalRequest request) {
		DoctorHospitalResponse response = new DoctorHospitalResponse();
		DoctorHospital doctorHospital = new DoctorHospital();

		doctorHospital.setId(id);

		// doctorHospital.setDoctorId(request.getDoctorId());
		// doctorHospital.setHospitalId(request.getHospitalId());
		// doctorHospital.setHospitalName(request.getHospitalName());
		doctorHospital.setDeptId(request.getDeptId());
		doctorHospital.setDeptName(request.getDeptName());
		doctorHospital.setMainFlag(request.getMainFlag());
		doctorHospital.setMinQueue(request.getMinQueue());
		doctorHospital.setRegisterFee(request.getRegisterFee());

		this.doctorHospitalMapper.updateDoctorHospital(doctorHospital);

		List<DoctorHospital> list = new ArrayList<DoctorHospital>();
		list.add(doctorHospital);
		response.setDoctorHospitals(list);
		return response;
	}

	@Override
	public ResponseBody updateDoctorHospital(long id, Integer minQueue,
			Integer registerFee, Integer deposit, Long deptId, String deptName,
			String contactName, String contactTele, Integer status) {
		ResponseBody response = new ResponseBody();

		DoctorHospital doctorHospital = this.doctorHospitalMapper.findById(id);
		if (doctorHospital == null) {
			response.setErrorCode(DoctorErrorCode.DOCTOR_HOSPITAL_NOT_EXIST);
			return response;
		}
		if (minQueue != null)
			doctorHospital.setMinQueue(minQueue);
		if (registerFee != null)
			doctorHospital.setRegisterFee(registerFee);
		if (deposit != null)
			doctorHospital.setDeposit(deposit);
		if (deptId != null)
			doctorHospital.setDeptId(deptId);
		if (deptName != null)
			doctorHospital.setDeptName(deptName);
		if (contactName != null)
			doctorHospital.setContactName(contactName);
		if (contactTele != null)
			doctorHospital.setContactTele(contactTele);
		if (status != null)
			doctorHospital.setStatus(status);

		this.doctorHospitalMapper.updateDoctorHospital(doctorHospital);

		return response;
	}

	@Override
	public ResponseBody deleteById(long id) {
		ResponseBody response = new ResponseBody();

		DoctorHospital doctorHospital = this.doctorHospitalMapper.findById(id);
		if (doctorHospital == null) {
			response.setErrorCode(DoctorErrorCode.DOCTOR_HOSPITAL_NOT_EXIST);
			return response;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date beginDate = calendar.getTime();

		Date endDate = null;

		List<Schedule> schedules = this.scheduleMapper
				.findByDoctorIdHospitalIdBeginDateEndDateStatus(
						doctorHospital.getDoctorId(),
						doctorHospital.getHospitalId(), beginDate, endDate,
						Schedule.Status.EFFECTIVE.getValue());
		if (CollectionUtils.isNotEmpty(schedules)) {
			response.setErrorCode(DoctorErrorCode.DOCTOR_HOSTPITAL_CANNOT_DELETE);
			return response;
		}
		this.doctorHospitalMapper.deleteById(id);

		return response;
	}

	@Override
	public ResponseBody updateStatus(long id, int status) {
		// TODO Auto-generated method stub
		ResponseBody responseBody = new ResponseBody();
		this.doctorHospitalMapper.updateStatus(id, status);
		return responseBody;
	}

	@Override
	public DoctorHospitalResponse findByDoctorIdStatus(long doctorId,
			Integer status) {
		DoctorHospitalResponse response = new DoctorHospitalResponse();

		List<DoctorHospital> doctorHospitalList = this.doctorHospitalMapper
				.findByDoctorIdStatus(doctorId, status);

		response.setDoctorHospitals(doctorHospitalList);

		return response;
	}

	@Override
	public DoctorHospitalResponse findByDoctorIdWithRegisterInfo(long doctorId,
			long userId) {
		DoctorHospitalResponse response = new DoctorHospitalResponse();
		int status = DoctorHospital.Status.SIGNED.getValue();
		List<DoctorHospital> doctorHospitalList = this.doctorHospitalMapper
				.findByDoctorIdStatus(doctorId, status);

		fillRegisterInfo(userId, doctorHospitalList);

		response.setDoctorHospitals(doctorHospitalList);

		return response;
	}

	@Override
	public DoctorHospitalResponse findByHospitalId(long hospitalId) {
		DoctorHospitalResponse response = new DoctorHospitalResponse();
		List<DoctorHospital> doctorHospitals = this.doctorHospitalMapper
				.findByHospitalId(hospitalId, null);
		for (DoctorHospital doctorHospital : doctorHospitals) {
			long doctorId = doctorHospital.getDoctorId();
			if (doctorId > 0) {
				Doctor doctor = doctorMapper.findById(doctorId);
				if (doctor != null) {
					doctorHospital.setDoctor(doctor);
				}
			}
		}
		response.setDoctorHospitals(doctorHospitals);

		return response;
	}

	@Override
	public DoctorHospitalResponse findByHospitalAndDept(long hospitalId,
			long deptId, DDPageInfo<DoctorHospital> pageInfo) {
		DoctorHospitalResponse response = new DoctorHospitalResponse();

		PageHelper.startPage(pageInfo.getPage(), pageInfo.getSize(), true);
		PageHelper.orderBy(pageInfo.getOrderBy());

		pageInfo.setPageInfo(doctorHospitalMapper.findByHospitalAndDept(
				hospitalId, deptId));

		response.setDoctorHospitals(pageInfo.getPageInfo().getResult());
		response.setPages(pageInfo.getPages());
		response.setTotal(pageInfo.getTotal());
		return response;
	}

	@Override
	public DoctorHospitalResponse findByHospitalAndDeptAndDoctor(
			long hospitalId, long deptId, long doctorId) {
		DoctorHospitalResponse response = new DoctorHospitalResponse();
		List<DoctorHospital> doctorHospitals = new ArrayList<DoctorHospital>();
		DoctorHospital doctorHospital = this.doctorHospitalMapper
				.findByHospitalAndDeptAndDoctor(hospitalId, deptId, doctorId);
		Doctor doctor = doctorMapper.findById(doctorId);
		doctorHospital.setDoctor(doctor);
		doctorHospitals.add(doctorHospital);
		response.setDoctorHospitals(doctorHospitals);
		return response;
	}

	@Override
	public ResponseBody findByHospitalWithPage(long hospitalId,
			String filterText, DDPageInfo<DoctorHospital> pageInfo) {
		DoctorHospitalResponse response = new DoctorHospitalResponse();

		PageHelper.startPage(pageInfo.getPage(), pageInfo.getSize(), true);
		List<DoctorHospital> doctorHospitals = doctorHospitalMapper
				.findByHospitalId(hospitalId, filterText);
		for (DoctorHospital doctorHospital : doctorHospitals) {
			long doctorId = doctorHospital.getDoctorId();
			Doctor doctor = doctorMapper.findById(doctorId);
			if (doctor != null) {
				doctorHospital.setDoctor(doctor);
			}
		}
		pageInfo.setPageInfo(doctorHospitals);

		response.setDoctorHospitals(pageInfo.getPageInfo().getResult());
		response.setPages(pageInfo.getPages());
		response.setTotal(pageInfo.getTotal());
		return response;
	}
}
