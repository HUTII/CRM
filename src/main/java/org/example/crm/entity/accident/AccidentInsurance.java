package org.example.crm.entity.accident;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccidentInsurance {
    /**
     * 保险id
     */
    private Long id;

    /**
     * 保险名称
     */
    private String name;

    /**
     * 保险价格
     */
    private Integer price;

    /**
     * 生效时长
     */
    private Integer effectPeriod;

    /**
     * 意外事故保障
     */
    private Integer accidentCoverage ;

    /**
     * 残疾保障
     */
    private Integer disabilityCoverage;

    /**
     * 住院保障
     */
    private Integer hospitalCoverage;

    /**
     * 门诊保障
     */
    private Integer outpatientCoverage;

    /**
     * 紧急救援
     */
    private Integer emergencyAssistance;

    /**
     * 个人责任
     */
    private Integer personalLiability;
}
