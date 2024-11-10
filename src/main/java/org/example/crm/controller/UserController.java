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
@CrossOrigin(origins = "*", allowedHeaders = "*")
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
}
