package org.example.crm.mapper.car;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author ：eco
 * @date ：Created in 2024/11/24 11:12
 */
@Mapper
public interface CarOrderRecordMapper {
    List<String> selectAllUsername();

    List<Long> selectItemIdByUsername(String username);
}
