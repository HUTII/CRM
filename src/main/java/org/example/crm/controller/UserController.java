package org.example.crm.controller;

import jakarta.annotation.Resource;
import org.example.crm.dto.common.CommonResult;
import org.example.crm.service.user.UserService;
import org.springframework.web.bind.annotation.*;

/**
 * @author hutianlin
 * 2024/11/10 16:05
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public CommonResult<String> login(@RequestParam String username, @RequestParam String password) {
        String token = userService.login(username, password);
        if (token == null) {
            return CommonResult.failed("用户名或密码错误");
        }
        return CommonResult.success(token);
    }

    @PostMapping("/register")
    public CommonResult<String> register(@RequestParam String username, @RequestParam String password) {
        if (userService.register(username, password)) {
            return CommonResult.success("注册成功");
        }
        return CommonResult.failed("用户名已存在");
    }

    @PostMapping("/validateToken")
    public CommonResult<Boolean> validateToken(@RequestParam String token) {
        if (userService.validateToken(token)) {
            return CommonResult.success(true);
        }
        return CommonResult.failed("token无效");
    }

    @PostMapping("/getUsername")
    public CommonResult<String> getUsername(@RequestParam String token) {
        String username = userService.getUsername(token);
        if (username == null) {
            return CommonResult.failed("token无效");
        }
        return CommonResult.success(username);
    }
}
