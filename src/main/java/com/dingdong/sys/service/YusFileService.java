package com.dingdong.sys.service;

import java.io.IOException;

import com.dingdong.sys.vo.response.YusFileResponse;

public interface YusFileService {

	public YusFileResponse findYusFileById(long id);

	public YusFileResponse addYusFileForWx(long userId, String wxServerId);

	/**
	 * 获取通过微信转换过的文件地址
	 * 
	 * @param yusFile
	 * @return
	 */
	String getTransYusFileUrl(String fileIds);

	public void fileTransferToLocal();

	public void downLoadFromUrl(String urlStr, String fileName, String savePath)
			throws IOException;

}
