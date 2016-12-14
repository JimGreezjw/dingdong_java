package com.dingdong.sys.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dingdong.sys.model.SysTag;

/**
 * 系统标签
 * 
 * @author niukai
 * 
 */
@Repository
public interface SysTagMapper {

	public SysTag findById(@Param(value = "id") long id);

	public List<SysTag> findAllTags();

	public int addTag(@Param(value = "sysTag") SysTag sysTag);

	public int deleteById(@Param(value = "id") long id);
}
