package org.example.crm.service.user;

/**
 * 用户服务接口
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

    /**
     * 用户注册
     *
     * @param username 用户名
     * @param password 密码
     * @return 是否注册成功
     */
    boolean register(String username, String password);

    /**
     * 验证token
     *
     * @param token token
     * @return 是否有效
     */
    boolean validateToken(String token);

    /**
     * 获取用户名
     *
     * @param token token
     * @return 用户名
     */
    String getUsername(String token);
}
