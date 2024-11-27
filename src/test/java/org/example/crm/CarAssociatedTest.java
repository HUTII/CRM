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
        for (long i = 1L; i <= 16L; i++) {
            CarInsurance carInsurance = associatedCarInsuranceService.getAssociatedCarInsurance(i);
            System.out.println("ID为" + i + "的汽车保险由关联算法选出的保险为");
            System.out.println(carInsurance);
            CarInsurance mostPopularCarInsurance = associatedCarInsuranceService.getMostPopularCarInsurance();
            System.out.println("ID为" + i + "的车险推荐的最受欢迎保险为");
            System.out.println(mostPopularCarInsurance);
        }

    }


//    @Test
//    public void getAllCarInsurances() {
//        List<CarInsurance> carInsuranceList = associatedCarInsuranceService.getAllCarInsurances();
//        System.out.println("以下为目前所有可以选择的车险：");
//        if (carInsuranceList != null && !carInsuranceList.isEmpty()) {
//            carInsuranceList.forEach(System.out::println);
//        } else {
//            System.out.println("未找到任何车险记录！");
//        }
//    }
}
