package org.example.crm.mapper.accident;

import org.apache.ibatis.annotations.Mapper;
import org.example.crm.entity.accident.AccidentInsurance;

import java.util.List;

/**
 * 意外险Mapper接口
 * 提供意外险相关数据库操作方法
 * @author
 * @date 2024/11/14
 */
@Mapper
public interface AccidentInsuranceMapper {
    /**
     * 查询所有意外保险
     * @return 意外保险列表
     */
    List<AccidentInsurance> findAll();

    /**
     * 根据id查询意外保险
     * @param id 意外保险ID
     * @return 意外保险
     */
    AccidentInsurance findById(Long id);
}
