package org.example.crm;

import jakarta.annotation.Resource;
import org.example.crm.entity.travel.TravelInsurance;
import org.example.crm.service.travel.AssociatedTravelInsuranceService;
import org.example.crm.service.travel.NearestTravelInsuranceService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author hutianlin
 * 2024/11/15 11:30
 */
@SpringBootTest
public class TravelAssociatedTest {

    @Resource
    private AssociatedTravelInsuranceService associatedTravelInsuranceService;

    @Resource
    private NearestTravelInsuranceService nearestTravelInsuranceService;

    @Test
    public void getAssociatedTravelInsurance() {
        TravelInsurance travelInsurance = associatedTravelInsuranceService.getAssociatedTravelInsurance(5L);
        System.out.println(travelInsurance);
        travelInsurance = nearestTravelInsuranceService.getNearestTravelInsurance(5L);
        System.out.println(travelInsurance);
    }
}
