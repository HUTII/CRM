package org.example.crm.service.impl.user;

import jakarta.annotation.Resource;
import org.example.crm.entity.user.User;
import org.example.crm.mapper.user.UserMapper;
import org.example.crm.service.user.UserService;
import org.example.crm.utils.JwtTokenUtil;
import org.springframework.stereotype.Service;

/**
 * @author hutianlin
 * 2024/11/10 16:24
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    @Override
    public String login(String username, String password) {
        String token = null;
        User user = userMapper.selectUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            token = jwtTokenUtil.generateToken(username);
            return token;
        }
        return token;
    }
}
