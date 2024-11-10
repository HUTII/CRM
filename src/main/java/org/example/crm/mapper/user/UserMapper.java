package org.example.crm.mapper.user;

import org.apache.ibatis.annotations.Mapper;
import org.example.crm.entity.user.User;

/**
 * @author hutianlin
 * 2024/11/10 14:16
 */
@Mapper
public interface UserMapper {
    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    User selectUserByUsername(String username);
}
