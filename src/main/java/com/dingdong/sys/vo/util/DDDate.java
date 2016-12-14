package com.dingdong.sys.vo.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Date的通用类
 * 
 * @author niukai
 * 
 */
public class DDDate {

	private Date date;

	public DDDate() {
		date = new Date();
	}

	public DDDate(Date date) {
		this.date = date;
	}

	@SuppressWarnings("deprecation")
	public DDDate(String strDate) {
		this.date = new Date(strDate);
	}

	/**
	 * 获取当前日期前几天
	 * 
	 * @param day
	 * @return
	 */
	public DDDate before(int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -day);

		return new DDDate(calendar.getTime());
	}

	/**
	 * 获取当前日期后几天
	 * 
	 * @param day
	 * @return
	 */
	public DDDate after(int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, day);

		return new DDDate(calendar.getTime());
	}

	/**
	 * 获取日期
	 * 
	 * @return
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * 格式化日期输出
	 * 
	 * @return
	 */
	public String toStdDate() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String str = df.format(date);
		return str;
	}

	/**
	 * 中国通用的日期格式
	 * 
	 * @return
	 */
	public String toChinaDate() {
		DateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
		String str = df.format(date);
		return str;
	}

	/**
	 * 格式化时间输出
	 * 
	 * @return
	 */
	public String toStdDateTime() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = df.format(date);
		return str;
	}

	public static void main(String[] args) {
		DDDate now = new DDDate();
		System.out.println(now.before(1).getDate());
		System.out.println(now.after(1).getDate());

		System.out.println(now.toStdDate());
		System.out.println(now.toStdDateTime());
	}
}
