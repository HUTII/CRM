package org.example.crm.service.user;

/**
 * @author hutianlin
 * 2024/11/10 16:24
 */
public interface UserService {

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    String login(String username, String password);
}
