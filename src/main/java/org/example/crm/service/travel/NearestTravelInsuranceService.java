package org.example.crm.service.travel;

import org.example.crm.entity.travel.TravelInsurance;

/**
 * @author hutianlin
 * 2024/11/14 20:28
 */
public interface NearestTravelInsuranceService {
    /**
     * 获取最临近的旅游保险
     * @param id 旅游保险id
     * @return 旅游保险entity
     */
    TravelInsurance getNearestTravelInsurance(Long id);

    /**
     * 获取最受欢迎的旅游保险
     * @return 旅游保险entity
     */
    TravelInsurance getMostPopularTravelInsurance();
}
