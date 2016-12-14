//package com.dingdong.conf;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Component
//public class Settings {
//	private static Settings instance;
//
//	public static Settings getInstance() {
//		return instance;
//	}
//
//	public Settings() {
//		instance = this;
//	}
//
//	@Value("#{configProperties['test_userName']}")
//	private String userName;
//	@Value("#{configProperties['test_age']}")
//	private int age;
//
//	public String getUserName() {
//		return userName;
//	}
//
//	public void setUserName(String userName) {
//		this.userName = userName;
//	}
//
//	public int getAge() {
//		return age;
//	}
//
//	public void setAge(int age) {
//		this.age = age;
//	}
// }