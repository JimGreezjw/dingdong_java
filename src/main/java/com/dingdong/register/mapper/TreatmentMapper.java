package com.dingdong.register.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dingdong.register.model.Treatment;

/**
 * 
 * 
 */
@Repository
public interface TreatmentMapper {

	public List<Treatment> findAllTreatments();

	public List<Treatment> findTreatmentById(@Param(value = "id") long id);

	public void deleteTreatment(@Param(value = "id") long id);

}
