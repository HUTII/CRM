package org.example.crm.entity.car;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarInsurance {
    /**
     * 车险id
     */
    private Integer productId;

    /**
     * 保险名称
     */
    private String productName;

    /**
     * 保险类型（交强险、商业险、附加险等）
     */
    private String productType;

    /**
     * 保障范围
     */
    private String coverage;

    /**
     * 赔付限额
     */
    private Float compensationLimit;

    /**
     * 价格
     */
    private Float price;

    /**
     * 产品描述
     */
    private String description;

    /**
     * 是否可选（true 可选 / false 强制）
     */
    private Boolean optional;

    /**
     * 保险公司信息
     */
    private String insuranceCompany;

    /**
     * 保险的好处
     */
    private String benefits;
}
