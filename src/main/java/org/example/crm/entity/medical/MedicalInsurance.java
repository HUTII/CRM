package org.example.crm.entity.medical;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hutianlin
 * 2024/11/19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalInsurance {
    /**
     * 保险id
     */
    private Long id;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 保险计划名称
     */
    private String planName;

    /**
     * 价格
     */
    private Integer price;

    /**
     * 生效期
     */
    private Integer effectPeriod;

    /**
     * 住院覆盖金额
     */
    private Integer hospitalCoverage;

    /**
     * 门诊覆盖金额
     */
    private Integer outpatientCoverage;

    /**
     * 手术覆盖金额
     */
    private Integer surgeryCoverage;

    /**
     * 重大疾病赔付金额
     */
    private Integer criticalIllness;

    /**
     * 紧急援助赔付金额
     */
    private Integer emergencyAssistance;

    /**
     * 适用人群
     */
    private String applicablePopulation;
}
