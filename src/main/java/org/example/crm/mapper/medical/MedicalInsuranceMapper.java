package org.example.crm.mapper.medical;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.crm.entity.medical.MedicalInsurance;

import java.util.List;

/**
 * @author
 * 2024/11/19
 */
@Mapper
public interface MedicalInsuranceMapper {
    /**
     * 查询所有医疗保险
     * @return 医疗保险列表
     */
    List<MedicalInsurance> selectAll();

    /**
     * 根据ID查询医疗保险
     * @param id 医疗保险ID
     * @return 医疗保险
     */
    MedicalInsurance selectById(@Param("id") Long id);
}
