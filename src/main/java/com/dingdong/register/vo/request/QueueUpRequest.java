//package com.dingdong.register.vo.request;
//
//import java.util.Date;
//
//import com.dingdong.common.vo.RequestBody;
//import com.fasterxml.jackson.annotation.JsonFormat;
//
///**
// * 客户排队请求
// * 
// * @author chenliang
// * @version 2015年12月12日 下午11:11:22
// */
//public class QueueUpRequest extends RequestBody {
//
//    /** 医生id */
//    private long doctorId;
//    /** 预约时间 */
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
//    private Date appointmentTime;
//    /** 医院id */
//    private long hospitalId;
//    
//    private String phoneNo;
//
//    public long getDoctorId() {
//        return doctorId;
//    }
//
//    public void setDoctorId(long doctorId) {
//        this.doctorId = doctorId;
//    }
//
//    public Date getAppointmentTime() {
//        return appointmentTime;
//    }
//
//    public void setAppointmentTime(Date appointmentTime) {
//        this.appointmentTime = appointmentTime;
//    }
//
//    public long getHospitalId() {
//        return hospitalId;
//    }
//
//    public void setHospitalId(long hospitalId) {
//        this.hospitalId = hospitalId;
//    }
//
//    public String getPhoneNo() {
//        return phoneNo;
//    }
//
//    public void setPhoneNo(String phoneNo) {
//        this.phoneNo = phoneNo;
//    }
// }
