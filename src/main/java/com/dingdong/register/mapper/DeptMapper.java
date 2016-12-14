package com.dingdong.register.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.springframework.stereotype.Repository;

import com.dingdong.conf.PageInfo;
import com.dingdong.register.model.Dept;

/**
 * 
 * 
 */
@Repository
public interface DeptMapper {

	public List<Dept> findAllDepts(
			@Param(value = "filterText") String filterText,
			@Param(value = "page") PageInfo page);

	@Results(value = { @Result(id = true, property = "id", column = "id"),
			@Result() })
	public Dept findById(@Param(value = "id") long id);

	public Dept findByName(@Param(value = "name") String name);

	public List<Dept> findByParentId(@Param(value = "parentId") long parentId);

	public void deleteDept(@Param(value = "id") long id);

	public void addDept(@Param(value = "dept") Dept dept);

	/**
	 * 获取顶层的部门，大科室列表
	 * 
	 * @return
	 */
	public List<Dept> findTopDepts();

	/**
	 * 获取父科室全部层级的子科室
	 * 
	 * @param parentId
	 * @return
	 */
	public List<Dept> findRecursiveSubDepts(
			@Param(value = "parentDeptOutline") String parentDeptOutline);
}
