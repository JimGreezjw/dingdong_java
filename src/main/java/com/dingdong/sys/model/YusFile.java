package com.dingdong.sys.model;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.dingdong.common.model.IdEntity;

/**
 * YsFile entity. @author MyEclipse Persistence Tools
 */
@Alias("yusFile")
public class YusFile extends IdEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3162641973552648266L;

	// 写对应的pojo类型，比如part,routing
	private String name = "";

	private int status = Status.EFFECTIVE.getId();

	// 文件扩展名类型
	private String fileType = "";

	public int getLocationType() {
		return locationType;
	}

	public void setLocationType(int locationType) {
		this.locationType = locationType;
	}

	private long fileSize;
	private String fileUrl = "";

	private long createId = 0;
	private Date createTime = new Date();

	// 0表示从微信，1表示从yushansoft的文件服务器
	private int locationType = 0;

	public static enum Status {
		DRAFT(0, "草稿"), EFFECTIVE(1, "生效"), DELETED(1, "删除");
		private int id;
		private String desc;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		Status(int id, String desc) {
			this.id = id;
			this.desc = desc;
		}

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public long getCreateId() {
		return createId;
	}

	public void setCreateId(long createId) {
		this.createId = createId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
