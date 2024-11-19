package org.example.crm.entity.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hutianlin
 * 2024/11/10 14:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private String gender;

    /**
     * 年收入
     */
    private Integer annualIncome;

    /**
     * 年龄
     */
    private Integer age;
}
