package com.dingdong.sys.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dingdong.sys.mapper.YusFileMapper;
import com.dingdong.sys.model.YusFile;
import com.dingdong.sys.service.DDMZService;
import com.dingdong.sys.service.YusFileService;
import com.dingdong.sys.vo.response.YusFileResponse;

/**
 * 
 * @author ChanLueng
 * 
 */
@Service
@Transactional
public class YusFileServiceImpl implements YusFileService {
	private static final Logger LOG = LoggerFactory
			.getLogger(YusFileServiceImpl.class);

	@Autowired
	private YusFileMapper yusFileMapper;

	@Autowired
	private DDMZService wechatService;

	@Override
	public YusFileResponse findYusFileById(long id) {
		YusFileResponse response = new YusFileResponse();
		List<YusFile> yusFileList = new ArrayList<YusFile>();
		yusFileList.add(this.yusFileMapper.findById(id));
		response.setYusFiles(yusFileList);
		return response;
	}

	@Override
	public YusFileResponse addYusFileForWx(long userId, String wxServerId) {
		YusFileResponse response = new YusFileResponse();
		YusFile yusFile = new YusFile();
		yusFile.setCreateId(userId);
		yusFile.setFileUrl(wxServerId);
		yusFile.setName(wxServerId + ".jpg");
		this.yusFileMapper.addYusFile(yusFile);

		List<YusFile> yusFiles = new ArrayList<YusFile>();
		yusFiles.add(yusFile);

		response.setYusFiles(yusFiles);
		return response;
	}

	@Override
	public String getTransYusFileUrl(String fileIds) {
		if (fileIds == null)
			return null;

		try {
			String[] arrayFileId = fileIds.split(",");
			// 微信url只存了一个mediaid
			String mediaUrl = wechatService.getMediaPreUrl();
			List<String> urlList = new ArrayList<String>();

			for (int i = 0; i < arrayFileId.length; i++) {
				if (StringUtils.isBlank(arrayFileId[i]))
					continue;
				long fileId = Long.valueOf(arrayFileId[i]);
				YusFile yusFile = this.yusFileMapper.findById(fileId);
				if (yusFile != null) {
					String url = yusFile.getFileUrl();
					if (url != null && !url.startsWith("http://")) {
						urlList.add(mediaUrl + url);

					} else {
						urlList.add(url);
					}
				}
			}

			return StringUtils.join(urlList, ",");
		} catch (Exception e) {
			LOG.error("get file name error. \n" + e.getMessage());
			// e.printStackTrace();
		}
		return null;
	}

	@Override
	@Scheduled(cron = "0 48 * * * ?", zone = "GMT+08:00")
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	// 不使用事务，文件传输耗时长，并没有直接关联
	public void fileTransferToLocal() {
		LOG.info("*****************begin transfer file from wx");

		String savePathLocalRoot = "/resource/dingdong";

		File file = new File(savePathLocalRoot);
		// 如果文件夹不存在则创建
		if (!file.exists() && !file.isDirectory()) {
			LOG.warn("****************************,resource directory unfound！");
			return;
		}

		Calendar c = Calendar.getInstance();
		// 微信只保存3天的文件,保险起见，2天之内的
		c.add(Calendar.DATE, -2);

		List<YusFile> yusFiles = yusFileMapper.getUnTransferYusFiles(c
				.getTime());
		String mediaUrl = wechatService.getMediaPreUrl();
		String sep = File.separator;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + sep + "MM" + sep
				+ "dd");
		Date date = new Date();
		for (YusFile yusFile : yusFiles) {
			try {
				String url = yusFile.getFileUrl();
				if (url != null && !"".equals(url)
						&& !url.startsWith("http://")) {
					// 微信url只存了一个mediaid
					// yusFile.setFileUrl(mediaUrl + url);
					String savePath = "http://www.yushansoft.com/dingdong/userFiles/"
							+ yusFile.getCreateId()
							+ sep
							+ sdf.format(date)
							+ sep;

					String savePathLocal = savePathLocalRoot + "/userFiles/"
							+ yusFile.getCreateId() + sep + sdf.format(date)
							+ sep;
					this.downLoadFromUrl(mediaUrl + url, yusFile.getName(),
							savePathLocal);

					yusFile.setFileUrl(savePath + yusFile.getName());

					yusFile.setLocationType(1);

					yusFileMapper.updateYusFile(yusFile);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LOG.error(e.getMessage());

			}
		}
	}

	/**
	 * 从网络Url中下载文件
	 * 
	 * @param urlStr
	 * @param fileName
	 * @param savePath
	 * @throws IOException
	 */
	@Override
	public void downLoadFromUrl(String urlStr, String fileName, String savePath)
			throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置超时间为3秒
		conn.setConnectTimeout(3 * 1000);
		// 防止屏蔽程序抓取而返回403错误
		conn.setRequestProperty("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

		// 得到输入流
		InputStream inputStream = conn.getInputStream();
		// 获取自己数组
		byte[] getData = readInputStream(inputStream);

		// 文件保存位置
		File saveDir = new File(savePath);
		if (!saveDir.exists()) {
			saveDir.mkdirs();
		}
		File file = new File(savePath, fileName);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(getData);
		if (fos != null) {
			fos.close();
		}
		if (inputStream != null) {
			inputStream.close();
		}

	}

	/**
	 * 从输入流中获取字节数组
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}

	public static void main(String[] args) {
		YusFileServiceImpl f = new YusFileServiceImpl();
		try {
			f.downLoadFromUrl(
					"http://101.95.48.97:8005/res/upload/interface/apptutorials/manualstypeico/6f83ce8f-0da5-49b3-bac8-fd5fc67d2725.png",
					"百度.jpg", "d:\\ss" + File.separator + "yy");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
