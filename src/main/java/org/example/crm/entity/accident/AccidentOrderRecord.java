package org.example.crm.entity.accident;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AccidentOrderRecord {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class TravelOrderRecord {
        /**
         * 订单id
         */
        private Long id;

        /**
         * 用户id
         */
        private Integer userId;

        /**
         * 保险id
         */
        private Integer productId;

        /**
         * 购买者与保险对象的关系，0代表本人购买，1代表他人代替购买
         */
        private String relationship;
    }

}
