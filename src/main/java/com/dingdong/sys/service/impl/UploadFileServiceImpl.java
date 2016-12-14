package com.dingdong.sys.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.dingdong.register.mapper.DoctorMapper;
import com.dingdong.register.mapper.HospitalMapper;
import com.dingdong.register.model.Doctor;
import com.dingdong.register.model.Hospital;
import com.dingdong.sys.service.UploadFileService;
import com.dingdong.sys.vo.response.UploadFileResponse;

/**
 * 
 * @author ChanLueng
 * 
 */
@Service
@Transactional
public class UploadFileServiceImpl implements UploadFileService {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(UploadFileServiceImpl.class);

	@Value("#{config['ddDoctorFilePath']}")
	private String ddDoctorFilePath;

	@Value("#{config['ddDoctorFileUrl']}")
	private String ddDoctorFileUrl;

	@Value("#{config['ddHospitalFilePath']}")
	private String ddHospitalFilePath;

	@Value("#{config['ddHospitalFileUrl']}")
	private String ddHospitalFileUrl;

	@Autowired
	private DoctorMapper doctorMapper;

	@Autowired
	private HospitalMapper hospitalMapper;

	@Override
	public UploadFileResponse addDoctorFile(MultipartFile uploadFile, Long id,
			HttpServletRequest request) {
		UploadFileResponse response = new UploadFileResponse();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 判断 request 是否有文件上传,即多部分请求
		if (multipartResolver.isMultipart(request)) {
			// 转换成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 取得request中的所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile(iter.next());
				if (file != null) {
					// 取得当前上传文件的文件名称
					String myFileName = file.getOriginalFilename();
					// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
					if (myFileName.trim() != "") {
						// 重命名上传后的文件名
						String ext = myFileName.substring(myFileName
								.lastIndexOf("."));

						String fileName = "doctor_" + id + ext;
						// 定义上传路径
						File dirFile = new File(ddDoctorFilePath);
						if (!dirFile.exists()) {
							dirFile.mkdirs();
						}
						File localFile = new File(ddDoctorFilePath, fileName);
						try {
							file.transferTo(localFile);
						} catch (IllegalStateException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}

						response.setFileUrl(ddDoctorFileUrl + fileName);
						Doctor doctor = doctorMapper.findById(id);
						if (doctor != null) {
							doctor.setHeadImgUrl(ddDoctorFileUrl + fileName);
							doctorMapper.updateBySelective(doctor);
						}
					}
				}
			}

		}
		return response;
	}

	@Override
	public UploadFileResponse addHospitalFile(MultipartFile uploadFile,
			Long id, HttpServletRequest request) {
		UploadFileResponse response = new UploadFileResponse();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 判断 request 是否有文件上传,即多部分请求
		if (multipartResolver.isMultipart(request)) {
			// 转换成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 取得request中的所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile(iter.next());
				if (file != null) {
					// 取得当前上传文件的文件名称
					String myFileName = file.getOriginalFilename();
					// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
					if (myFileName.trim() != "") {
						// 重命名上传后的文件名
						String ext = myFileName.substring(myFileName
								.lastIndexOf("."));

						String fileName = "hospital_" + id + ext;
						// 定义上传路径
						File dirFile = new File(ddHospitalFilePath);
						if (!dirFile.exists()) {
							dirFile.mkdirs();
						}
						File localFile = new File(ddHospitalFilePath, fileName);
						try {
							file.transferTo(localFile);
						} catch (IllegalStateException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}

						response.setFileUrl(ddHospitalFileUrl + fileName);
						Hospital hospital = hospitalMapper.findById(id);
						if (hospital != null) {
							hospital.setIconUrl(ddHospitalFileUrl + fileName);
							hospitalMapper.updateBySelective(hospital);
						}
					}
				}
			}

		}
		return response;
	}
}
