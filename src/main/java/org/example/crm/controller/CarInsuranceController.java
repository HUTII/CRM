package org.example.crm.controller;

import jakarta.annotation.Resource;
import org.example.crm.dto.common.CommonResult;

import org.example.crm.entity.car.CarInsurance;
import org.example.crm.service.car.AssociatedCarInsuranceService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/getAssociatedCarInsurance")
    public CommonResult<CarInsurance> getAssociatedCarInsurance(@RequestParam Long id) {
        CarInsurance carInsurance = associatedCarInsuranceService.getAssociatedCarInsurance(id);
        if (carInsurance == null) {
            log.info("最受欢迎的汽车保险服务调用异常，出现null值");
            return CommonResult.failed("最受欢迎的汽车保险服务调用异常，出现null值");

        }
        return CommonResult.success(carInsurance);
    }
}
