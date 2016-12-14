package com.dingdong.register.service.impl;

import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.dingdong.register.mapper.DoctorMapper;
import com.dingdong.register.mapper.PatientMapper;
import com.dingdong.register.mapper.RegisterMapper;
import com.dingdong.register.mapper.ScheduleMapper;
import com.dingdong.register.model.Doctor;
import com.dingdong.register.model.Patient;
import com.dingdong.register.model.Register;
import com.dingdong.register.model.Schedule;
import com.dingdong.sys.mapper.UserMapper;
import com.dingdong.sys.model.User;

/**
 * 挂号预约测试类
 * 
 * @author chenliang
 * @version 2015年12月18日 上午12:36:25
 */
@RunWith(MockitoJUnitRunner.class)
public class RegisterServiceImplTest {

	@Mock
	private DoctorMapper doctorMapper;

	@Mock
	private ScheduleMapper scheduleMapper;

	@Mock
	private PatientMapper patientMapper;

	@Mock
	private UserMapper userMapper;

	@Mock
	private RegisterMapper registerMapper;

	// 提前通知的天数
	public static int INFORM_DAYS_IN_ADVANCE = 7;

	// @InjectMocks
	// private RegisterServiceImpl registerService;

	@Test
	public void argumentMatcherTest() {

		List<String> list = Mockito.mock(List.class);
		Mockito.when(list.get(Mockito.anyInt())).thenReturn("hello", "world",
				"haha");

		String result = list.get(0) + list.get(1);

		Mockito.verify(list, Mockito.times(2)).get(Mockito.anyInt());

		Assert.assertEquals("helloworld", result);
		// String result = list.get(0) + list.get(2);
		//
		// Mockito.verify(list, Mockito.times(3)).get(Mockito.anyInt());
		//

	}

	@Test
	public void testMakeAppointment() {
		long userId = 1L;
		long patientId = 1L;
		long doctorId = 1L;
		long id = 12L;
		Schedule schedule = new Schedule();
		schedule.setDoctorId(doctorId);
		schedule.setHospitalId(100L);
		schedule.setHospitalName("慈济医院");
		schedule.setScheduleDate(new Date());
		schedule.setTimeSlot(1);
		schedule.setId(id);
		schedule.setIssueNum(10);
		schedule.setRegisteredNum(3);

		Doctor doctor = new Doctor();
		doctor.setId(doctorId);
		doctor.setName("sunjian");

		User user = new User();
		user.setId(userId);
		user.setName("john");
		user.setPhone("1");

		Patient patient = new Patient();
		patient.setId(patientId);
		patient.setName("john");

		Register register = new Register();
		register.setPatientId(patientId);
		register.setStatus(Register.Status.DRAFT.getValue());
		register.setScheduleId(id);

		Mockito.when(scheduleMapper.findById(id)).thenReturn(schedule);
		Mockito.when(patientMapper.findById(patientId)).thenReturn(patient);
		Mockito.when(userMapper.findById(userId)).thenReturn(user);
		Mockito.when(doctorMapper.findById(doctorId)).thenReturn(doctor);
		Mockito.when(registerMapper.add(register)).thenReturn(1);
		Mockito.when(scheduleMapper.acquireScheduleLock(id, 4, 3))
				.thenReturn(1);
		//
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日");
		// String strTreatTime = sdf.format(schedule.getScheduleDate())
		// + schedule.getTimeSlotDesc();
		// Mockito.when(
		// commonMessageService.sendOrderMessage(register,null,null,null);

		// ResponseBody response = registerService.makeAppointment(userId,
		// patientId, id);

		// Assert.assertTrue(response.getResponseStatus() == CommonErrorCode.OK
		// .getCode());
	}
	// public void testMakeAppointmentNullSchedule() {
	// long userId = 1L;
	// long patientId = 1L;
	// long scheduleId = 12L;
	// Schedule schedule = null;
	// Mockito.when(scheduleMapper.findById(scheduleId)).thenReturn(schedule);
	//
	// @SuppressWarnings("deprecation")
	// ResponseBody response = registerService.makeAppointment(userId,
	// patientId, scheduleId);
	//
	// // Assert.assertTrue(response.getResponseStatus() ==
	// // ScheduleErrorCode.SCHEDULE_NOT_FOUND
	// // .getCode());
	// }

	// public void testMakeAppointmentFailed() {
	// long userId = 1L;
	// long patientId = 1L;
	// long id = 12L;
	// Schedule schedule = new Schedule();
	// schedule.setDoctorId(1L);
	// schedule.setHospitalId(100L);
	// schedule.setId(id);
	// schedule.setIssueNum(10);
	// schedule.setRegisteredNum(3);
	//
	// Register register = new Register();
	// register.setPatientId(patientId);
	// register.setStatus(Register.Status.DRAFT.getValue());
	// register.setScheduleId(id);
	//
	// Mockito.when(scheduleMapper.findById(id)).thenReturn(schedule);
	// Mockito.when(registerMapper.add(register)).thenReturn(1);
	// Mockito.when(scheduleMapper.acquireScheduleLock(id, 4, 3))
	// .thenReturn(0);
	//
	// @SuppressWarnings("deprecation")
	// ResponseBody response = registerService.makeAppointment(userId,
	// patientId, id);
	//
	// Assert.assertTrue(response.getResponseStatus() ==
	// RegisterErrorCode.REGISTER_FAILED
	// .getCode());
	// }
}
