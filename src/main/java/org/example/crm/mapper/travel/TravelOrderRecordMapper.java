package org.example.crm.mapper.travel;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author hutianlin
 * 2024/11/15 09:40
 */
@Mapper
public interface TravelOrderRecordMapper {
    List<String> selectAllUsername();

    List<Long> selectItemIdByUsername(String username);
}
