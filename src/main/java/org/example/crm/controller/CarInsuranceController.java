package org.example.crm.controller;

import jakarta.annotation.Resource;
import org.example.crm.dto.common.CommonResult;

import org.example.crm.entity.car.CarInsurance;
import org.example.crm.mapper.car.CarInsuranceMapper;
import org.example.crm.service.car.AssociatedCarInsuranceService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;//getmapping

import java.util.List;//list

/**
 * @author ：eco
 * @date ：Created in 2024/11/24 10:48
 */
@RestController
@RequestMapping("/carInsurance")
public class CarInsuranceController {
    private static final Logger log = LoggerFactory.getLogger(CarInsuranceController.class);

    @Resource
    private AssociatedCarInsuranceService associatedCarInsuranceService;

    @Resource
    private CarInsuranceMapper carInsuranceMapper;

    @PostMapping("/getAssociatedCarInsurance")
    public CommonResult<CarInsurance> getAssociatedCarInsurance(@RequestParam Long id) {
        CarInsurance carInsurance = associatedCarInsuranceService.getAssociatedCarInsurance(id);
        if (carInsurance == null) {
            CarInsurance mostPopularCarInsurance = associatedCarInsuranceService.getMostPopularCarInsurance();
            if(mostPopularCarInsurance == null){
                log.info("最受欢迎的汽车保险服务调用异常，出现null值");
                return CommonResult.failed("最受欢迎的汽车保险服务调用异常，出现null值");
            }
            return CommonResult.success(mostPopularCarInsurance);
        }
        return CommonResult.success(carInsurance);
    }

    /**
     * 获取所有的车险记录
     * @return CommonResult 包含所有车险的列表
     */
    @PostMapping("/getAllCarInsurances")
    public CommonResult<List<CarInsurance>> getAllCarInsurances() {
        List<CarInsurance> carInsuranceList = associatedCarInsuranceService.getAllCarInsurances();
        if (carInsuranceList == null || carInsuranceList.isEmpty()) {
            log.info("获取所有车险记录时出现问题，记录为空");
            return CommonResult.failed("获取所有车险记录失败或数据为空");
        }
        return CommonResult.success(carInsuranceList);
    }

    @PostMapping("/getCarInsuranceByCarId")
    public CommonResult<CarInsurance> getCarInsuranceByCarId(@RequestParam Long id) {
        CarInsurance carInsurance = carInsuranceMapper.selectById(id);
        if (carInsurance == null) {
            log.info("根据车辆id获取车险记录时出现问题，记录为空");
            return CommonResult.failed("根据车辆id获取车险记录失败或数据为空");
        }
        return CommonResult.success(carInsurance);
    }

}
