package com.dingdong.conf;

import java.util.List;

import com.github.pagehelper.Page;

/**
 * 分页信息
 * 
 * @author daiweiming
 * @version 2015年2月2日 下午10:19:42
 * @param <T>
 */
public class DDPageInfo<T> {

	private Page<T> pageInfo = new Page<T>();

	public DDPageInfo() {

	}

	public DDPageInfo(Integer page, Integer size, String orderBy, String order) {

		if (page == null) {
			page = PageUtils.PAGE_NUM;
		}
		if (size == null) {
			size = PageUtils.PAGE_SIZE;
		}
		pageInfo.setPageNum(page);
		pageInfo.setPageSize(size);
		pageInfo.setOrderBy(orderBy + " " + order);
		/*
		 * this.page = page; this.size = size; this.orderBy = orderBy;
		 * this.order = order; this.offset = (page - 1) * size;
		 */
	}

	public int getPage() {
		return pageInfo.getPageNum();
	}

	public void setPage(int page) {
		pageInfo.setPageNum(page);
	}

	public int getSize() {
		return pageInfo.getPageSize();
	}

	public void setSize(int size) {
		pageInfo.setPageSize(size);
	}

	public String getOrderBy() {
		return pageInfo.getOrderBy();
	}

	public void setOrderBy(String orderBy, String order) {
		pageInfo.setOrderBy(orderBy + " " + order);
	}

	public int getPages() {
		return pageInfo.getPages();
	}

	public long getTotal() {
		return pageInfo.getTotal();
	}

	public Page<T> getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(List<T> list) {
		this.pageInfo = (Page<T>) list;
	}
}
