package org.example.crm.entity.travel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hutianlin
 * 2024/11/14 20:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TravelOrderRecord {
    /**
     * 订单id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 保险id
     */
    private Long itemId;
}
