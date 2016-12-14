package com.dingdong.register.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.conf.DDPageInfo;
import com.dingdong.register.exception.DoctorErrorCode;
import com.dingdong.register.mapper.DoctorHospitalMapper;
import com.dingdong.register.mapper.DoctorMapper;
import com.dingdong.register.model.Doctor;
import com.dingdong.register.model.DoctorHospital;
import com.dingdong.register.service.DoctorHospitalService;
import com.dingdong.register.service.DoctorService;
import com.dingdong.register.vo.request.DoctorRequest;
import com.dingdong.register.vo.response.DoctorResponse;
import com.dingdong.sys.model.User;
import com.dingdong.sys.service.DDMZService;
import com.dingdong.sys.service.YusFileService;
import com.github.pagehelper.PageHelper;

/**
 * 
 * @author ChanLueng
 * 
 */
@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {
	private static final Logger LOG = LoggerFactory
			.getLogger(DoctorServiceImpl.class);
	@Autowired
	private DoctorMapper doctorMapper;

	@Autowired
	private DoctorHospitalMapper doctorHospitalMapper;

	@Autowired
	private DoctorHospitalService doctorHospitalService;

	@Autowired
	private DDMZService ddmzService;

	@Autowired
	private YusFileService yusFileService;

	@Value("#{config['ddDoctorFilePath']}")
	private String ddDoctorFilePath;

	private static final String DOCTOR_IMG_URL = "http://www.yushansoft.com/dingdong/images/doctors/";

	@Override
	public DoctorResponse findAllDoctors(String filterText,
			DDPageInfo<Doctor> pageInfo) {
		DoctorResponse response = new DoctorResponse();

		PageHelper.startPage(pageInfo.getPage(), pageInfo.getSize(), true);
		PageHelper.orderBy(pageInfo.getOrderBy());

		pageInfo.setPageInfo(doctorMapper.findAllDoctors(filterText));

		response.setDoctors(pageInfo.getPageInfo().getResult());
		response.setPages(pageInfo.getPages());
		response.setTotal(pageInfo.getTotal());
		return response;
	}

	public DoctorResponse getNewJoinDoctors(int requireNum) {
		DoctorResponse response = new DoctorResponse();
		List<Doctor> doctors = doctorMapper.getNewJoinDoctors(requireNum);

		long size = doctors == null ? 0 : doctors.size();
		response.setTotal(size);
		response.setDoctors(doctors);
		return response;
	}

	@Override
	public DoctorResponse findDoctorById(long id) {
		DoctorResponse response = new DoctorResponse();
		List<Doctor> doctors = new ArrayList<>();
		doctors.add(this.doctorMapper.findById(id));
		response.setDoctors(doctors);
		return response;
	}

	@Override
	public DoctorResponse addDoctor(DoctorRequest request) {
		DoctorResponse response = new DoctorResponse();
		List<Doctor> doctors = request.getDoctors();
		for (Doctor doctor : doctors) {
			List<Doctor> existsDoctorList = this.doctorMapper
					.findByNameHospitalDept(doctor.getName(),
							doctor.getHospitalName(), doctor.getDeptName());
			if (CollectionUtils.isNotEmpty(existsDoctorList)) {
				response.setErrorCode(DoctorErrorCode.DOCTOR_ALREADY_EXISTS);
				// 临时处理
				response.setDoctors(existsDoctorList);
				return response;
			}
			if (doctor.getIntroduction().length() > 1000) {
				response.setErrorCode(DoctorErrorCode.DOCTOR_INTRODUCE_EXCEED);
				return response;
			}

			doctorMapper.addDoctor(doctor);

			DoctorHospital doctorHospital = new DoctorHospital();
			doctorHospital.setDoctorId(doctor.getId());
			doctorHospital.setDoctorName(doctor.getName());
			doctorHospital.setHospitalId(doctor.getHospitalId());
			doctorHospital.setHospitalName(doctor.getHospitalName());
			doctorHospital.setDeptId(doctor.getDeptId());
			doctorHospital.setDeptName(doctor.getDeptName());
			doctorHospital.setMainFlag(1);// 设置主医院为1
			doctorHospital.setMinQueue(0);
			doctorHospital.setRegisterFee(100);
			doctorHospital.setDeposit(0);
			this.doctorHospitalMapper.addDoctorHospital(doctorHospital);

			String fileName = "doctor_qr_" + doctor.getId() + ".jpg";
			try {
				String qrCodeUrl = ddmzService.getPersistQrUrl(doctor.getId());
				yusFileService.downLoadFromUrl(qrCodeUrl, fileName,
						ddDoctorFilePath);
			} catch (Exception e) {
				LOG.error(e.getMessage());
			}
			doctor.setQrImgUrl(DOCTOR_IMG_URL + fileName);

			doctorMapper.updateBySelective(doctor);
		}
		response.setDoctors(doctors);

		return response;
	}

	@Override
	public ResponseBody updateDoctor(long id, String name, Integer gender,
			String hospitalName, String level, String specialty,
			String introduction) {
		ResponseBody response = new ResponseBody();
		if (introduction != null && introduction.length() > 1000) {
			response.setErrorCode(DoctorErrorCode.DOCTOR_INTRODUCE_EXCEED);
			return response;
		}

		Doctor doctor = this.doctorMapper.findById(id);
		if (doctor == null) {
			return response.setErrorCode(DoctorErrorCode.DOCTOR_ID_NOT_EXIST);
		}

		if (name != null)
			doctor.setName(name);
		if (gender != null)
			doctor.setGender(gender);
		if (hospitalName != null)
			doctor.setHospitalName(hospitalName);
		if (level != null)
			doctor.setLevel(level);
		if (specialty != null)
			doctor.setSpecialty(specialty);
		if (introduction != null)
			doctor.setIntroduction(introduction);

		this.doctorMapper.updateDoctor(doctor);
		return response;
	}

	@Override
	public ResponseBody submitSignDoctor(long id, Long userId, String officeTele) {
		ResponseBody response = new ResponseBody();

		Doctor doctor = this.doctorMapper.findById(id);
		if (doctor == null) {
			return response.setErrorCode(DoctorErrorCode.DOCTOR_ID_NOT_EXIST);
		}

		List<Doctor> doctors = this.doctorMapper.findByUserId(userId);
		if (CollectionUtils.isNotEmpty(doctors)) {
			doctor = doctors.get(0);
			if (doctor.getStatus() == Doctor.Status.CREATED.getValue()) {
				return response
						.setErrorCode(DoctorErrorCode.DOCTOR_SUBMIT_VALID);
			} else if (doctor.getStatus() == Doctor.Status.SIGNED.getValue()) {
				return response
						.setErrorCode(DoctorErrorCode.DOCTOR_USER_ALREADY_SIGNED);
			}

		}

		if (userId != null && userId > 0) {
			doctor.setUserId(userId);
			if (StringUtils.isNotBlank(officeTele)) {
				doctor.setOfficeTele(officeTele);
			}
			this.doctorMapper.updateDoctor(doctor);
		}
		return response;
	}

	@Override
	public ResponseBody signDoctor(long id) {
		ResponseBody response = new ResponseBody();

		Doctor doctor = this.doctorMapper.findById(id);
		if (doctor == null) {
			return response.setErrorCode(DoctorErrorCode.DOCTOR_ID_NOT_EXIST);
		}
		if (doctor.getUserId() > 0) {
			doctor.setStatus(User.Status.ACTIVATED.getValue());
			this.doctorMapper.updateDoctor(doctor);
		}

		return response;
	}

	@Override
	public ResponseBody cancelSignDoctor(long id) {
		ResponseBody response = new ResponseBody();

		Doctor doctor = this.doctorMapper.findById(id);
		if (doctor == null) {
			return response.setErrorCode(DoctorErrorCode.DOCTOR_ID_NOT_EXIST);
		}

		doctor.setUserId(0);
		doctor.setStatus(Doctor.Status.CREATED.getValue());
		this.doctorMapper.updateDoctor(doctor);

		return response;
	}

	@Override
	public DoctorResponse findUnsignedDoctors(String name) {
		DoctorResponse response = new DoctorResponse();
		response.setDoctors(doctorMapper.findUnsignedDoctors(name));
		return response;
	}

	@Override
	public DoctorResponse findByStatus(int status) {
		DoctorResponse response = new DoctorResponse();
		response.setDoctors(doctorMapper.findByStatus(status));
		return response;
	}

	@Override
	public DoctorResponse findUnsignedDoctorsApply(String name,
			DDPageInfo<Doctor> pageInfo) {
		DoctorResponse response = new DoctorResponse();

		PageHelper.startPage(pageInfo.getPage(), pageInfo.getSize(), true);

		pageInfo.setPageInfo(doctorMapper.findUnsignedDoctorsApply(name));
		response.setDoctors(pageInfo.getPageInfo().getResult());
		response.setPages(pageInfo.getPages());
		response.setTotal(pageInfo.getTotal());
		return response;
	}

	@Override
	public DoctorResponse updateDoctorBySelective(DoctorRequest request) {
		DoctorResponse response = new DoctorResponse();
		List<Doctor> doctors = request.getDoctors();
		for (Doctor doctor : doctors) {
			if (doctor.getIntroduction().length() > 1000) {
				response.setErrorCode(DoctorErrorCode.DOCTOR_INTRODUCE_EXCEED);
				return response;
			}

			doctorMapper.updateBySelective(doctor);
		}
		response.setDoctors(doctors);
		return response;
	}

	@Override
	public ResponseBody delDoctorById(long id) {
		DoctorResponse response = new DoctorResponse();
		doctorMapper.deleteById(id);
		return response;
	}

}
