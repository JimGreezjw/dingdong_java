package com.dingdong.register.vo.response;

import java.util.List;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.model.PatientCond;

/**
 * 医生信息响应对象
 * 
 * 
 */
public class PatientCondResponse extends ResponseBody {

    private List<PatientCond> patientConds;

    public List<PatientCond> getPatientConds() {
        return patientConds;
    }

    public PatientCondResponse setPatientConds(List<PatientCond> patientConds) {
        this.patientConds = patientConds;
        return this;
    }
}
