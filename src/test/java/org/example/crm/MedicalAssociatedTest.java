package org.example.crm;

import jakarta.annotation.Resource;
import org.example.crm.entity.medical.MedicalInsurance;
import org.example.crm.service.medical.AssociatedMedicalInsuranceService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 测试医疗保险关联算法服务
 * @author
 * 2024/11/19
 */
@SpringBootTest
public class MedicalAssociatedTest {

    @Resource
    private AssociatedMedicalInsuranceService associatedMedicalInsuranceService;

    @Test
    public void getAssociatedMedicalInsurance() {
        // 使用测试ID 5L 进行测试
        MedicalInsurance medicalInsurance = associatedMedicalInsuranceService.getAssociatedMedicalInsurance(5L);
        System.out.println(medicalInsurance);
    }
}
