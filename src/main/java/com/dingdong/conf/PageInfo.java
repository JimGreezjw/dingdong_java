package com.dingdong.conf;

/**
 * 分页信息
 * 
 * @author chenliang
 * @version 2015年12月23日 下午10:19:42
 */
public class PageInfo {

	/**
	 * 分页-当前页码
	 */
	private int page = PageUtils.PAGE_NUM;
	/**
	 * 分页-每页大小
	 */
	private int size = PageUtils.PAGE_SIZE;
	/**
	 * 排序字段
	 */
	private String orderBy;
	/**
	 * 排序方式：desc，asc
	 */
	private String order = PageUtils.ORDER_DESC;

	private int offset;

	public PageInfo() {

	}

	public PageInfo(int page, int size, String orderBy, String order) {
		this.page = page;
		this.size = size;
		this.orderBy = orderBy;
		this.order = order;
		this.offset = (page - 1) * size;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
		this.offset = (page - 1) * size;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
		this.offset = (page - 1) * size;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public int getOffset() {
		return this.offset;
	}
}
