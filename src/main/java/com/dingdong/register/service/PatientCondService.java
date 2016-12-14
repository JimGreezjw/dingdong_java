package com.dingdong.register.service;

import com.dingdong.register.vo.request.PatientCondRequest;
import com.dingdong.register.vo.response.PatientCondResponse;

public interface PatientCondService {

    public PatientCondResponse findAllPatientConds();

    public PatientCondResponse findPatientCondByRegisterId(long registerId);
    
    public PatientCondResponse addPatientCond(PatientCondRequest request);
}
