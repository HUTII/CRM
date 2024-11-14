package org.example.crm.entity.travel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hutianlin
 * 2024/11/14 20:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TravelInsurance {
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
     * 生效时间
     */
    private Integer effectPeriod;

    /**
     * 延误赔付金额
     */
    private Integer tripDelay;

    /**
     * 意外死亡赔付金额
     */
    private Integer accidentDeath;

    /**
     * 医疗费用赔付金额
     */
    private Integer medicalCoverage;

    /**
     * 紧急援助赔付金额
     */
    private Integer emergencyAssistance;

    /**
     * 个人财产赔付金额
     */
    private Integer personalProperty;

    /**
     * 个人责任赔付金额
     */
    private Integer personalLiability;
}
