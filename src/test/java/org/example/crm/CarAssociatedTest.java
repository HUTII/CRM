package org.example.crm;

import jakarta.annotation.Resource;
import org.example.crm.entity.car.CarInsurance;
import org.example.crm.service.car.AssociatedCarInsuranceService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ：eco
 * @date ：Created in 2024/11/24 11:16
 */
@SpringBootTest
public class CarAssociatedTest {

    @Resource
    private AssociatedCarInsuranceService associatedCarInsuranceService;

    @Test
    public void getAssociatedCarInsurance() {
        CarInsurance carInsurance = associatedCarInsuranceService.getAssociatedCarInsurance(5L);
        System.out.println(carInsurance);
    }
}
