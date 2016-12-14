package com.dingdong.sys.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.dingdong.sys.vo.response.UploadFileResponse;

public interface UploadFileService {

	public UploadFileResponse addDoctorFile(MultipartFile uploadFile, Long id,
			HttpServletRequest request);

	public UploadFileResponse addHospitalFile(MultipartFile uploadFile,
			Long id, HttpServletRequest request);

}
