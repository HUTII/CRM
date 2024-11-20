package org.example.crm.mapper.accident;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 意外险订单记录Mapper接口
 * 提供意外险订单记录相关数据库操作方法
 * @author
 * @date 2024/11/15
 */
@Mapper
public interface AccidentOrderRecordMapper {
    /**
     * 查询所有用户名称
     * @return 用户名列表
     */
    List<String> findAllUsernames();

    /**
     * 根据用户名查询购买的保险ID列表
     * @param username 用户名
     * @return 保险ID列表
     */
    List<Long> findInsuranceIdsByUsername(String username);
}
