package org.example.crm;

import jakarta.annotation.Resource;
import org.example.crm.entity.medical.MedicalInsurance;
import org.example.crm.service.medical.NearestMedicalInsuranceService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 医疗保险关联测试类
 * 测试获取临近的医疗保险及最受欢迎的医疗保险功能
 */
@SpringBootTest
public class MedicalNearestTest {

    @Resource
    private NearestMedicalInsuranceService nearestMedicalInsuranceService;

    /**
     * 测试获取距离最近的医疗保险
     */
    @Test
    public void findNearestMedicalInsuranceTest() {
        MedicalInsurance nearestInsurance = nearestMedicalInsuranceService.getNearestMedicalInsurance(5L);
        System.out.println("最近的医疗保险：" + nearestInsurance);
    }

    /**
     * 测试获取最受欢迎的医疗保险
     */
    @Test
    public void findMostPopularMedicalInsuranceTest() {
        MedicalInsurance mostPopularInsurance = nearestMedicalInsuranceService.getMostPopularMedicalInsurance();
        System.out.println("最受欢迎的医疗保险：" + mostPopularInsurance);
    }
}
