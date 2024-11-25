package org.example.crm;

import jakarta.annotation.Resource;
import org.example.crm.entity.car.CarInsurance;
import org.example.crm.service.car.AssociatedCarInsuranceService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
        System.out.println("关联算法选出的保险为：");
        System.out.println(carInsurance);
    }

    @Test
    public void getAllCarInsurances() {
        List<CarInsurance> carInsuranceList = associatedCarInsuranceService.getAllCarInsurances();
        System.out.println("以下为目前所有可以选择的车险：");
        if (carInsuranceList != null && !carInsuranceList.isEmpty()) {
            carInsuranceList.forEach(System.out::println);
        } else {
            System.out.println("未找到任何车险记录！");
        }
    }
}
