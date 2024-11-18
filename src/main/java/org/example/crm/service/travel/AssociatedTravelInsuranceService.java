package org.example.crm.service.travel;

import org.example.crm.entity.travel.TravelInsurance;

/**
 * 关联算法服务接口
 * @author hutianlin
 * 2024/11/15 09:44
 */
public interface AssociatedTravelInsuranceService {
    /**
     * 根据关联算法获取的旅游保险
     * @param id 旅游保险id
     * @return 旅游保险entity
     */
    TravelInsurance getAssociatedTravelInsurance(Long id);
}
