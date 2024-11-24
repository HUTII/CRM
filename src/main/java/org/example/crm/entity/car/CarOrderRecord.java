package org.example.crm.entity.car;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：eco
 * @date ：Created in 2024/11/24 10:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarOrderRecord {
    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 性别（男/女）
     */
    private String gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 联系方式（电话、邮箱）
     */
    private String contactInfo;

    /**
     * 地址
     */
    private String address;

    /**
     * 驾驶证颁发日期
     */
    private String licenseIssueDate;

    /**
     * 驾驶证有效期
     */
    private String licenseExpiryDate;

    /**
     * 客户类型（新客户、续保客户、高收入、低收入）
     */
    private String customerType;
}
