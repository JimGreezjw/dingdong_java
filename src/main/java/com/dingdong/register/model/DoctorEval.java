package com.dingdong.register.model;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.dingdong.common.model.IdEntity;

@Alias("doctorEval")
public class DoctorEval extends IdEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6690479663546097254L;

	private int status = Status.EFFECTIVE.getValue();
	private long userId;// 粉丝用户编号
	private String userName = "";// 粉丝用户编号
	private long doctorId;
	private Date createTime = new Date();
	// 挂号Id
	private long registerId = 0;

	/**
	 * 评价内容
	 */
	private String evalDesc = "";

	private int treatmentEffect;

	/**
	 * 标准标签
	 */
	private String tags = "";
	private String tagsDesc = "";

	public long getRegisterId() {
		return registerId;
	}

	public void setRegisterId(long registerId) {
		this.registerId = registerId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEvalDesc() {
		return evalDesc;
	}

	public void setEvalDesc(String evalDesc) {
		this.evalDesc = evalDesc;
	}

	public int getTreatmentEffect() {
		return treatmentEffect;
	}

	public void setTreatmentEffect(int treatmentEffect) {
		this.treatmentEffect = treatmentEffect;
	}

	public int getServiceAttitude() {
		return serviceAttitude;
	}

	public void setServiceAttitude(int serviceAttitude) {
		this.serviceAttitude = serviceAttitude;
	}

	private int serviceAttitude;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(long doctorId) {
		this.doctorId = doctorId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getTagsDesc() {
		return tagsDesc;
	}

	public void setTagsDesc(String tagsDesc) {
		this.tagsDesc = tagsDesc;
	}

	public static enum Status {
		CREATED(0, "创建"), EFFECTIVE(1, "已生效"), CANCEL(2, "已取消");

		private int value;
		private String desc;

		Status(int value, String desc) {
			this.value = value;
			this.desc = desc;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}

}
