package org.example.crm.controller;

import jakarta.annotation.Resource;
import org.example.crm.dto.common.CommonResult;
import org.example.crm.entity.accident.AccidentInsurance;
import org.example.crm.service.accident.NearestAccidentInsuranceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * 意外险控制器
 * 提供意外险相关服务的接口
 * @author
 * @date 2024/11/07
 */
@RestController
@RequestMapping("/accidentInsurance")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccidentInsuranceController {

    private static final Logger log = LoggerFactory.getLogger(AccidentInsuranceController.class);

    @Resource
    private NearestAccidentInsuranceService nearestAccidentInsuranceService;

    /**
     * 获取最临近的意外保险
     * @param id 意外保险ID
     * @return 最临近的意外保险
     */
    @PostMapping("/getNearestAccidentInsurance")
    public CommonResult<AccidentInsurance> getNearestAccidentInsurance(@RequestParam Long id) {
        AccidentInsurance accidentInsurance = nearestAccidentInsuranceService.findNearestAccidentInsurance(id);
        if (accidentInsurance == null) {
            AccidentInsurance mostPopularAccidentInsurance = nearestAccidentInsuranceService.findMostPopularAccidentInsurance();
            if (mostPopularAccidentInsurance == null) {
                log.info("最受欢迎的意外保险服务调用异常，出现null值");
                return CommonResult.failed("最受欢迎的意外保险服务调用异常，出现null值");
            }
            return CommonResult.success(mostPopularAccidentInsurance);
        }
        return CommonResult.success(accidentInsurance);
    }

    /**
     * 获取关联的意外保险
     * @param id 意外保险ID
     * @return 关联的意外保险
     */

}
