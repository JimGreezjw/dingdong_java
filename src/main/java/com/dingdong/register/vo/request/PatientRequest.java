package com.dingdong.register.vo.request;

import java.util.List;

import com.dingdong.common.vo.RequestBody;
import com.dingdong.register.model.Patient;

public class PatientRequest extends RequestBody {

    private List<Patient> patients;

    public List<Patient> getPatients() {
        return patients;
    }

    public PatientRequest setPatients(List<Patient> patients) {
        this.patients = patients;
        return this;
    }
}
