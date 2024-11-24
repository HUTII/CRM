package org.example.crm.mapper.car;

import org.apache.ibatis.annotations.Mapper;
import org.example.crm.entity.car.CarInsurance;

import java.util.List;
/**
 * @author ：eco
 * @date ：Created in 2024/11/24 11:12
 */
@Mapper
public interface CarInsuranceMapper {
    /**
     * 查询所有汽车保险
     * @return 汽车保险列表
     */
    List<CarInsurance> selectAll();

    /**
     * 根据id查询汽车保险
     * @param id 汽车保险id
     * @return 汽车保险
     */
    CarInsurance selectById(Long id);
}
