package com.dingdong.register.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.dingdong.common.exception.LockFailureException;
import com.dingdong.common.vo.ResponseBody;
import com.dingdong.conf.PageInfo;
import com.dingdong.register.exception.RegisterErrorCode;
import com.dingdong.register.mapper.DoctorEvalMapper;
import com.dingdong.register.mapper.DoctorTagMapper;
import com.dingdong.register.mapper.RegisterMapper;
import com.dingdong.register.model.DoctorEval;
import com.dingdong.register.model.DoctorTag;
import com.dingdong.register.model.Register;
import com.dingdong.register.service.DoctorEvalService;
import com.dingdong.register.vo.response.DoctorEvalResponse;
import com.dingdong.sys.exception.SysErrorCode;
import com.dingdong.sys.mapper.SysTagMapper;
import com.dingdong.sys.mapper.UserMapper;
import com.dingdong.sys.model.SysTag;
import com.dingdong.sys.model.Transfer;
import com.dingdong.sys.model.User;
import com.dingdong.sys.service.TransferService;
import com.dingdong.sys.vo.util.DDCollectionUtils;

/**
 * 
 * @author ChanLueng
 * 
 */
@Service
@Transactional
public class DoctorEvalServiceImpl implements DoctorEvalService {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(DoctorEvalServiceImpl.class);
	@Autowired
	private DoctorEvalMapper doctorEvalMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private RegisterMapper registerMapper;

	@Autowired
	private DoctorTagMapper doctorTagMapper;

	@Autowired
	private SysTagMapper sysTagMapper;

	@Autowired
	private TransferService transferService;

	// 成功诊疗后对患者用户的奖励
	@Value("#{config['score.patientEvalScore']}")
	private int patientEvalScore;

	@Override
	public DoctorEvalResponse findByDoctorId(long doctorId, PageInfo pageInfo) {
		DoctorEvalResponse response = new DoctorEvalResponse();
		response.setDoctorEvals(this.doctorEvalMapper.findByDoctorId(doctorId,
				pageInfo));

		return response;
	}

	@Override
	public DoctorEvalResponse findByUserId(long userId) {
		DoctorEvalResponse response = new DoctorEvalResponse();
		List<DoctorEval> doctorEvals = this.doctorEvalMapper
				.findByUserId(userId);

		response.setDoctorEvals(doctorEvals);

		return response;
	}

	@Override
	public DoctorEvalResponse findById(long id) {
		DoctorEvalResponse response = new DoctorEvalResponse();
		List<DoctorEval> doctorEvals = new ArrayList<>();
		doctorEvals.add(this.doctorEvalMapper.findById(id));
		response.setDoctorEvals(doctorEvals);
		return response;
	}

	@Override
	public DoctorEvalResponse addDoctorEval(long userId, long registerId,
			int treatmentEffect, int serviceAttitude, String evalDesc) {
		DoctorEvalResponse response = new DoctorEvalResponse();

		DoctorEval doctorEval = new DoctorEval();
		doctorEval.setUserId(userId);

		User user = this.userMapper.findById(userId);
		if (user == null) {
			response.setErrorCode(SysErrorCode.USER_ID_NOT_EXIST);
			return response;
		}

		Register register = this.registerMapper.findById(registerId);

		if (null == register) {
			response.setErrorCode(RegisterErrorCode.REGISTER_ID_NOT_EXIST);
			return response;
		}

		doctorEval.setDoctorId(register.getDoctorId());

		doctorEval.setUserName(user.getName());

		doctorEval.setRegisterId(registerId);
		doctorEval.setTreatmentEffect(treatmentEffect);
		doctorEval.setServiceAttitude(serviceAttitude);
		doctorEval.setEvalDesc(evalDesc);
		doctorEval.setStatus(DoctorEval.Status.EFFECTIVE.getValue());

		this.doctorEvalMapper.addDoctorEval(doctorEval);

		// 更新挂号为已评价状态
		this.registerMapper.updateStatusById(registerId,
				Register.Status.EVALUATED.getValue());

		// 为用户评价添加积分
		this.transferService.executeTransferScore(register.getUserId(),
				this.patientEvalScore, Transfer.Reason.YFPJJF.getId());

		List<DoctorEval> doctorEvals = new ArrayList<DoctorEval>();
		doctorEvals.add(doctorEval);
		response.setDoctorEvals(doctorEvals);

		return response;
	}

	@Override
	public DoctorEvalResponse addDoctorEvalWithTag(long userId,
			long registerId, int treatmentEffect, int serviceAttitude,
			String evalDesc, String tags) {
		DoctorEvalResponse response = new DoctorEvalResponse();

		DoctorEval doctorEval = new DoctorEval();
		doctorEval.setUserId(userId);

		User user = this.userMapper.findById(userId);
		if (user == null) {
			response.setErrorCode(SysErrorCode.USER_ID_NOT_EXIST);
			return response;
		}

		Register register = this.registerMapper.findById(registerId);

		if (null == register) {
			response.setErrorCode(RegisterErrorCode.REGISTER_ID_NOT_EXIST);
			return response;
		}

		List<DoctorEval> doctorEvalList = this.doctorEvalMapper
				.findByRegisterId(registerId);
		if (CollectionUtils.isNotEmpty(doctorEvalList)) {
			response.setErrorCode(RegisterErrorCode.ALREADY_EVALUATED);
			return response;

		}

		doctorEval.setDoctorId(register.getDoctorId());

		doctorEval.setUserName(user.getName());

		doctorEval.setRegisterId(registerId);
		doctorEval.setTreatmentEffect(treatmentEffect);
		doctorEval.setServiceAttitude(serviceAttitude);
		doctorEval.setEvalDesc(evalDesc);
		doctorEval.setStatus(DoctorEval.Status.EFFECTIVE.getValue());

		// 对于tags的操作
		long doctorId = doctorEval.getDoctorId();
		if (!StringUtils.isEmpty(tags)) {
			tags = tags.trim();

			String[] tagIdStrArray = tags.split(",");
			if (!DDCollectionUtils.isEmpty(tagIdStrArray)) {
				doctorEval.setTags(tags);
				StringBuilder sb = new StringBuilder();

				for (String tagIdStr : tagIdStrArray) {
					long tagId = Long.parseLong(tagIdStr);
					SysTag sysTag = sysTagMapper.findById(tagId);

					if (sysTag == null) {
						continue;
					}

					sb.append(sysTag.getTagName());
					sb.append(",");

					DoctorTag existDoctorTag = doctorTagMapper
							.findByDoctorIdAndTagId(doctorId, tagId);
					int rows = 0;
					if (existDoctorTag == null) {
						// 新增
						existDoctorTag = new DoctorTag();
						existDoctorTag.setDoctorId(doctorId);
						existDoctorTag.setDoctorName(register.getDoctorName());
						existDoctorTag.setTagId(tagId);
						existDoctorTag.setTagName(sysTag.getTagName());
						existDoctorTag.setTimes(1);

						rows = doctorTagMapper.insertDoctorTag(existDoctorTag);
					} else {
						// 更新
						rows = doctorTagMapper.updateDoctorTag(doctorId, tagId);
					}

					if (rows < 1) {
						throw new LockFailureException("医生标签更新失败");
					}
				}

				if (sb.length() > 0) {
					sb.deleteCharAt(sb.length() - 1);
					doctorEval.setTagsDesc(sb.toString());
				}
			}
		}
		// end

		this.doctorEvalMapper.addDoctorEval(doctorEval);

		// 更新挂号为已评价状态
		this.registerMapper.updateStatusById(registerId,
				Register.Status.EVALUATED.getValue());

		// 为用户添加积分
		// 为用户评价添加积分
		this.transferService.executeTransferScore(register.getUserId(),
				this.patientEvalScore, Transfer.Reason.YFPJJF.getId());

		response.setEvalScore(this.patientEvalScore);
		List<DoctorEval> doctorEvals = new ArrayList<DoctorEval>();
		doctorEvals.add(doctorEval);
		response.setDoctorEvals(doctorEvals);

		return response;
	}

	@Override
	public ResponseBody deleteById(long id) {
		this.doctorEvalMapper.deleteById(id);

		return null;
	}

	@Override
	public DoctorEvalResponse findByRegisterId(long registerId) {
		DoctorEvalResponse response = new DoctorEvalResponse();

		response.setDoctorEvals(this.doctorEvalMapper
				.findByRegisterId(registerId));
		return response;
	}
}
