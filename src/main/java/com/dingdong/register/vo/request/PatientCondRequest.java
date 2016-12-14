package com.dingdong.register.vo.request;

import java.util.List;

import com.dingdong.common.vo.RequestBody;
import com.dingdong.register.model.PatientCond;

public class PatientCondRequest extends RequestBody {

	private List<PatientCond> patientConds;

    public List<PatientCond> getPatientConds() {
        return patientConds;
    }

    public PatientCondRequest setPatientConds(List<PatientCond> patientConds) {
        this.patientConds = patientConds;
        return this;
    }
}
