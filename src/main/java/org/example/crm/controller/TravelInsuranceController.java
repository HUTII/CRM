package org.example.crm.controller;

import jakarta.annotation.Resource;
import org.example.crm.dto.common.CommonResult;
import org.example.crm.entity.travel.TravelInsurance;
import org.example.crm.mapper.travel.TravelInsuranceMapper;
import org.example.crm.service.travel.AssociatedTravelInsuranceService;
import org.example.crm.service.travel.NearestTravelInsuranceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hutianlin
 * 2024/11/07 15:34
 */
@RestController
@RequestMapping("/travelInsurance")
public class TravelInsuranceController {

    private static final Logger log = LoggerFactory.getLogger(TravelInsuranceController.class);

    @Resource
    private NearestTravelInsuranceService nearestTravelInsuranceService;

    @Resource
    private AssociatedTravelInsuranceService associatedTravelInsuranceService;

    @Resource
    private TravelInsuranceMapper travelInsuranceMapper;

    @PostMapping("/getNearestTravelInsurance")
    public CommonResult<TravelInsurance> getNearestTravelInsurance(@RequestParam Long id) {
        TravelInsurance travelInsurance = nearestTravelInsuranceService.getNearestTravelInsurance(id);
        if (travelInsurance == null) {
            TravelInsurance mostPopularTravelInsurance = nearestTravelInsuranceService.getMostPopularTravelInsurance();
            if (mostPopularTravelInsurance == null) {
                log.info("最受欢迎的旅游保险服务调用异常，出现null值");
                return CommonResult.failed("最受欢迎的旅游保险服务调用异常，出现null值");
            }
            return CommonResult.success(mostPopularTravelInsurance);
        }
        return CommonResult.success(travelInsurance);
    }

    @PostMapping("getAssociatedTravelInsurance")
    public CommonResult<TravelInsurance> getAssociatedTravelInsurance(@RequestParam Long id) {
        TravelInsurance travelInsurance = associatedTravelInsuranceService.getAssociatedTravelInsurance(id);
        if (travelInsurance == null) {
            TravelInsurance mostPopularTravelInsurance = nearestTravelInsuranceService.getMostPopularTravelInsurance();
            if (mostPopularTravelInsurance == null) {
                log.info("最受欢迎的旅游保险服务调用异常，出现null值");
                return CommonResult.failed("最受欢迎的旅游保险服务调用异常，出现null值");
            }
            return CommonResult.success(mostPopularTravelInsurance);
        }
        return CommonResult.success(travelInsurance);
    }

    @PostMapping("/getAllTravelInsurance")
    public CommonResult<List<TravelInsurance>> getAllTravelInsurance() {
        List<TravelInsurance> travelInsuranceList = travelInsuranceMapper.selectAll();
        if (travelInsuranceList == null) {
            log.info("查询所有旅游保险服务调用异常，出现null值");
            return CommonResult.failed("查询所有旅游保险服务调用异常，出现null值");
        }
        return CommonResult.success(travelInsuranceList);
    }

    @PostMapping("/getTravelInsuranceById")
    public CommonResult<TravelInsurance> getTravelInsuranceById(@RequestParam Long id) {
        TravelInsurance travelInsurance = travelInsuranceMapper.selectById(id);
        if (travelInsurance == null) {
            log.info("根据id查询旅游保险服务调用异常，出现null值");
            return CommonResult.failed("根据id查询旅游保险服务调用异常，出现null值");
        }
        return CommonResult.success(travelInsurance);
    }
}
