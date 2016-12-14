package com.dingdong.register.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dingdong.common.exception.LockFailureException;
import com.dingdong.common.vo.ResponseBody;
import com.dingdong.conf.PageInfo;
import com.dingdong.register.exception.DoctorErrorCode;
import com.dingdong.register.exception.FansErrorCode;
import com.dingdong.register.mapper.DoctorFanMapper;
import com.dingdong.register.mapper.DoctorMapper;
import com.dingdong.register.model.Doctor;
import com.dingdong.register.model.DoctorFan;
import com.dingdong.register.service.DoctorFanService;
import com.dingdong.register.vo.response.DoctorFanResponse;
import com.dingdong.register.vo.response.DoctorResponse;
import com.dingdong.sys.exception.SysErrorCode;
import com.dingdong.sys.mapper.UserMapper;
import com.dingdong.sys.model.User;
import com.dingdong.sys.vo.response.UserResponse;

/**
 * 
 * @author ChanLueng
 * 
 */
@Service
@Transactional
public class DoctorFanServiceImpl implements DoctorFanService {
	private static final Logger LOG = LoggerFactory
			.getLogger(DoctorFanServiceImpl.class);
	@Autowired
	private DoctorFanMapper doctorFanMapper;

	@Autowired
	private DoctorMapper doctorMapper;

	@Autowired
	private UserMapper userMapper;

	@Override
	public DoctorFanResponse findAllDoctorFans() {
		DoctorFanResponse response = new DoctorFanResponse();
		response.setDoctorFans(this.doctorFanMapper.findAllDoctorFans());
		return response;
	}

	@Override
	public DoctorFanResponse findByDoctorId(long doctorId, String filterText,
			PageInfo pageInfo) {
		DoctorFanResponse response = new DoctorFanResponse();
		response.setDoctorFans(this.doctorFanMapper.findByDoctorId(doctorId,
				filterText, pageInfo));

		return response;
	}

	@Override
	public DoctorResponse findByUserId(long userId) {
		DoctorResponse response = new DoctorResponse();
		List<DoctorFan> doctorFans = this.doctorFanMapper.findByUserId(userId);

		if (doctorFans != null && !doctorFans.isEmpty()) {
			List<Long> ids = new ArrayList<Long>();
			for (DoctorFan doctorFan : doctorFans) {
				ids.add(doctorFan.getDoctorId());
			}

			response.setDoctors(this.doctorMapper.findByIds(ids));
		}

		return response;
	}

	@Override
	public DoctorFanResponse findDoctorFanById(long id) {
		DoctorFanResponse response = new DoctorFanResponse();
		List<DoctorFan> doctorFans = new ArrayList<>();
		doctorFans.add(this.doctorFanMapper.findById(id));
		response.setDoctorFans(doctorFans);
		return response;
	}

	@Override
	public DoctorResponse addDoctorFan(long userId, long doctorId) {
		//DoctorFanResponse response = new DoctorFanResponse();
		DoctorResponse response = new DoctorResponse();
		DoctorFan doctorFan = this.doctorFanMapper.findByUserIdAndDoctorId(
				userId, doctorId);
		if (doctorFan != null) {
			LOG.info("doctorFan already Exists userId={},doctorId={}", userId,
					doctorId);
			response.setErrorCode(FansErrorCode.ALREADY_FOCUS_DOCTOR);
			return response;
		}
		// 不允许重复添加

		User user = this.userMapper.findById(userId);
		if (user == null) {
			response.setErrorCode(SysErrorCode.USER_ID_NOT_EXIST);
			return response;
		}

		Doctor doctor = this.doctorMapper.findById(doctorId);
		if (doctor == null) {
			response.setErrorCode(DoctorErrorCode.DOCTOR_ID_NOT_EXIST);
			return response;
		}

		doctorFan = new DoctorFan();
		doctorFan.setUserId(userId);
		doctorFan.setUserName(user.getName());
		doctorFan.setDoctorId(doctorId);
		doctorFan.setDoctorName(doctor.getName());

		// 粉丝数量存在并发问题
		int rows = this.doctorMapper.updateFansCount(doctorId, 1,
				(int) doctor.getFansCount());
		if (rows < 1) // 未锁定住
			throw new LockFailureException("无法锁定粉丝数量");

		LOG.info(" doctorFan successful userId={},doctorId={}", userId,
				doctorId);

		this.doctorFanMapper.addDoctorFan(doctorFan);
		List<Doctor> doctors = new ArrayList<Doctor>();

		doctors.add(doctor);
		response.setDoctors(doctors);

		return response;
	}

	@Override
	public ResponseBody deleteById(long id) {
		this.doctorFanMapper.deleteById(id);

		return null;
	}

	@Override
	public ResponseBody cancelDoctorFan(long userId, long doctorId) {
		DoctorFanResponse response = new DoctorFanResponse();

		DoctorFan doctorFan = this.doctorFanMapper.findByUserIdAndDoctorId(
				userId, doctorId);
		Doctor doctor = this.doctorMapper.findById(doctorId);
		if (doctorFan != null) {

			// 粉丝数量存在并发问题,粉丝数量减1
			int rows = this.doctorMapper.updateFansCount(doctorId, -1,
					(int) doctor.getFansCount());
			if (rows < 1)
				throw new LockFailureException("无法锁定粉丝数量");

			this.doctorFanMapper.deleteById(doctorFan.getId());

		}

		return response;
	}

	@Override
	public ResponseBody cancelDoctorFan(long id) {
		DoctorFanResponse response = new DoctorFanResponse();
		this.doctorFanMapper.deleteById(id);

		return response;
	}

	@Override
	public UserResponse findUsersByDoctor(long doctorId, String filterText,
			PageInfo pageInfo) {
		UserResponse response = new UserResponse();
		List<DoctorFan> doctorFans = this.doctorFanMapper.findByDoctorId(
				doctorId, filterText, pageInfo);

		if (doctorFans != null && !doctorFans.isEmpty()) {
			List<Long> ids = new ArrayList<Long>();
			for (DoctorFan doctorFan : doctorFans) {
				ids.add(doctorFan.getUserId());
			}

			response.setUsers(this.userMapper.findByIds(ids));
		}

		return response;
	}
}
