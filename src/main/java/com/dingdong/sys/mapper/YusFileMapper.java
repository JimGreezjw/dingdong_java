package com.dingdong.sys.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dingdong.sys.model.YusFile;

/**
 * 
 * @author ChanLueng
 * 
 */
@Repository
public interface YusFileMapper {

	public YusFile findById(@Param(value = "id") long id);

	/**
	 * 按照用户ids查询
	 * 
	 * @param ids
	 * @return
	 */
	public List<YusFile> findByIds(@Param(value = "ids") List<Long> ids);

	/**
	 * 获取还在微信服务器中的文件
	 * 
	 * @param beforeTime
	 * @return
	 */
	public List<YusFile> getUnTransferYusFiles(
			@Param(value = "beforeTime") Date beforeTime);

	/**
	 * 添加文件
	 * 
	 * @param yusFile
	 */
	public void addYusFile(@Param(value = "yusFile") YusFile yusFile);

	/**
	 * 更新文件
	 * 
	 * @param yusFile
	 */
	public void updateYusFile(@Param(value = "yusFile") YusFile yusFile);

	/**
	 * 更新文件
	 * 
	 * @param yusFiles
	 */
	public void updateYusFiles(@Param(value = "yusFiles") List<YusFile> yusFiles);

	/**
	 * 
	 * @param id
	 */
	public void deleteById(@Param(value = "id") long id);

}
