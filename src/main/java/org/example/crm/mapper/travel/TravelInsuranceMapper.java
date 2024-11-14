package org.example.crm.mapper.travel;

import org.apache.ibatis.annotations.Mapper;
import org.example.crm.entity.travel.TravelInsurance;

import java.util.List;

/**
 * @author hutianlin
 * 2024/11/14 20:25
 */
@Mapper
public interface TravelInsuranceMapper {
    /**
     * 查询所有旅游保险
     * @return 旅游保险列表
     */
    List<TravelInsurance> selectAll();

    /**
     * 根据id查询旅游保险
     * @param id 旅游保险id
     * @return 旅游保险
     */
    TravelInsurance selectById(Long id);
}
