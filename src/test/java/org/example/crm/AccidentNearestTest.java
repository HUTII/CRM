package org.example.crm;

import jakarta.annotation.Resource;
import org.example.crm.entity.accident.AccidentInsurance;
import org.example.crm.service.accident.NearestAccidentInsuranceService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 意外险关联测试类
 * 测试获取临近的意外险功能
 */
@SpringBootTest
public class AccidentNearestTest {

    @Resource
    private NearestAccidentInsuranceService nearestAccidentInsuranceService;

    @Test
    public void findNearestAccidentInsuranceTest() {
        AccidentInsurance accidentInsurance = nearestAccidentInsuranceService.findNearestAccidentInsurance(6L);
        System.out.println(accidentInsurance);
    }
}
