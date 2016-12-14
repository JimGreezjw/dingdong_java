package com.dingdong.sys.model;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.dingdong.common.model.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

@Alias("msg")
public class Msg extends IdEntity {

	private static final long serialVersionUID = 481377697449749388L;
	private long fromUserId;

	private long toUserId;

	private String fromUserName;

	private String fromUserHeadImgUrl;

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String toUserName) {
		this.fromUserName = toUserName;
	}

	public String getFromUserHeadImgUrl() {
		return fromUserHeadImgUrl;
	}

	public void setFromUserHeadImgUrl(String toUserHeadImgUrl) {
		this.fromUserHeadImgUrl = toUserHeadImgUrl;
	}

	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+08:00")
	private Date createTime = new Date();
	// 0：账户充值纪录；1：账户挂号扣费纪录；3：积分赠与纪录
	private int type = Type.TXT.getId();
	// 0--草稿， 1—成功
	private int status = Status.DRAFT.getId();

	// 消息内容
	private String content = "";

	public long getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(long fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getToUserId() {
		return toUserId;
	}

	public void setToUserId(long toUserId) {
		this.toUserId = toUserId;
	}

	public static enum Type {
		TXT(0, "文本"), IMAGE(1, "图像");
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

		Type(int id, String desc) {
			this.id = id;
			this.desc = desc;
		}

	}

	public static enum Status {

		DRAFT(0, "草稿"), SEND(1, "已发送"), RECEIVE(2, "已接收"), READ(3, "已阅读");
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
}
