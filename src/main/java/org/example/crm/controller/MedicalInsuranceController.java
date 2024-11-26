package org.example.crm.controller;

import jakarta.annotation.Resource;
import org.example.crm.dto.common.CommonResult;
import org.example.crm.entity.medical.MedicalInsurance;
import org.example.crm.mapper.medical.MedicalInsuranceMapper;
import org.example.crm.service.medical.AssociatedMedicalInsuranceService;
import org.example.crm.service.medical.NearestMedicalInsuranceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 医疗保险控制器
 * 处理与医疗保险相关的API请求
 *
 * 作者:
 * 日期: 2024/11/26 16:00
 */
@RestController
@RequestMapping("/medicalInsurance")
public class MedicalInsuranceController {

    private static final Logger log = LoggerFactory.getLogger(MedicalInsuranceController.class);

    @Resource
    private NearestMedicalInsuranceService nearestMedicalInsuranceService;

    @Resource
    private AssociatedMedicalInsuranceService associatedMedicalInsuranceService;

    @Resource
    private MedicalInsuranceMapper medicalInsuranceMapper;

    @PostMapping("/getNearestMedicalInsurance")
    public CommonResult<MedicalInsurance> getNearestMedicalInsurance(@RequestParam Long id) {
        MedicalInsurance medicalInsurance = nearestMedicalInsuranceService.getNearestMedicalInsurance(id);
        if (medicalInsurance == null) {
            MedicalInsurance mostPopularMedicalInsurance = nearestMedicalInsuranceService.getMostPopularMedicalInsurance();
            if (mostPopularMedicalInsurance == null) {
                log.info("最受欢迎的医疗保险服务调用异常，出现null值");
                return CommonResult.failed("最受欢迎的医疗保险服务调用异常，出现null值");
            }
            return CommonResult.success(mostPopularMedicalInsurance);
        }
        return CommonResult.success(medicalInsurance);
    }

    @PostMapping("/getAssociatedMedicalInsurance")
    public CommonResult<MedicalInsurance> getAssociatedMedicalInsurance(@RequestParam Long id) {
        MedicalInsurance medicalInsurance = associatedMedicalInsuranceService.getAssociatedMedicalInsurance(id);
        if (medicalInsurance == null) {
            MedicalInsurance mostPopularMedicalInsurance = nearestMedicalInsuranceService.getMostPopularMedicalInsurance();
            if (mostPopularMedicalInsurance == null) {
                log.info("最受欢迎的医疗保险服务调用异常，出现null值");
                return CommonResult.failed("最受欢迎的医疗保险服务调用异常，出现null值");
            }
            return CommonResult.success(mostPopularMedicalInsurance);
        }
        return CommonResult.success(medicalInsurance);
    }

    @PostMapping("/getAllMedicalInsurance")
    public CommonResult<List<MedicalInsurance>> getAllMedicalInsurance() {
        List<MedicalInsurance> medicalInsuranceList = medicalInsuranceMapper.selectAll();
        if (medicalInsuranceList == null) {
            log.info("查询所有医疗保险服务调用异常，出现null值");
            return CommonResult.failed("查询所有医疗保险服务调用异常，出现null值");
        }
        return CommonResult.success(medicalInsuranceList);
    }

    @PostMapping("/getMedicalInsuranceById")
    public CommonResult<MedicalInsurance> getMedicalInsuranceById(@RequestParam Long id) {
        MedicalInsurance medicalInsurance = medicalInsuranceMapper.selectById(id);
        if (medicalInsurance == null) {
            log.info("根据id查询医疗保险服务调用异常，出现null值");
            return CommonResult.failed("根据id查询医疗保险服务调用异常，出现null值");
        }
        return CommonResult.success(medicalInsurance);
    }
}
