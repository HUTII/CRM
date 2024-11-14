package org.example.crm.controller;

import jakarta.annotation.Resource;
import org.example.crm.dto.common.CommonResult;
import org.example.crm.entity.travel.TravelInsurance;
import org.example.crm.service.travel.NearestTravelInsuranceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * @author hutianlin
 * 2024/11/07 15:34
 */
@RestController
@RequestMapping("/travelInsurance")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TravelInsuranceController {

    private static final Logger log = LoggerFactory.getLogger(TravelInsuranceController.class);
    @Resource
    private NearestTravelInsuranceService nearestTravelInsuranceService;

    @PostMapping("/getNearestTravelInsurance")
    public CommonResult<TravelInsurance> getNearestTravelInsurance(@RequestParam Long id) {
        TravelInsurance travelInsurance = nearestTravelInsuranceService.getNearestTravelInsurance(id);
        if (travelInsurance == null) {
            log.info("旅游保险数据异常，出现null值");
            return CommonResult.failed("旅游保险数据异常，出现null值");
        }
        return CommonResult.success(travelInsurance);
    }
}
