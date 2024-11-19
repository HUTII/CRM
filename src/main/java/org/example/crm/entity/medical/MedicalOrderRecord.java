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
public class MedicalOrderRecord {
    /**
     * 记录id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 保险计划id
     */
    private Long planId;

    /**
     * 与购买者的关系
     */
    private String relationshipWithBuyer;
}
