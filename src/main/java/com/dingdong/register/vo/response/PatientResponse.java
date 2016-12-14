package com.dingdong.register.vo.response;

import java.util.List;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.model.Patient;

/**
 * 医生信息响应对象
 * 
 * @author chenliang
 * 
 */
public class PatientResponse extends ResponseBody {

    private List<Patient> patients;

    public List<Patient> getPatients() {
        return patients;
    }

    public PatientResponse setPatients(List<Patient> patients) {
        this.patients = patients;
        return this;
    }
}
