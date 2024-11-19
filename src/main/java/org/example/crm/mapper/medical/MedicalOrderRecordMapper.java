package org.example.crm.mapper.medical;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author
 * 2024/11/19
 */
@Mapper
public interface MedicalOrderRecordMapper {
    /**
     * 查询所有用户ID
     * @return 用户ID列表
     */
    List<Long> selectAllUserIds();


    List<Long> selectPlanIdByUserId(@Param("userId") Long userId);
}
