package org.example.crm.controller;

import jakarta.annotation.Resource;
import org.example.crm.dto.common.CommonResult;
import org.example.crm.entity.medical.MedicalInsurance;
import org.example.crm.service.medical.AssociatedMedicalInsuranceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * 医疗保险相关接口控制器
 * 仅包含获取关联医疗保险的方法
 *
 * @author
 * @date 2024/11/21
 */

@RestController
@RequestMapping("/medicalInsurance")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MedicalInsuranceController {

    private static final Logger log = LoggerFactory.getLogger(MedicalInsuranceController.class);

    @Resource
    private AssociatedMedicalInsuranceService associatedMedicalInsuranceService;

    /**
     * 获取与指定医疗保险相关联的医疗保险信息
     *
     * @param id 医疗保险的ID
     * @return 关联的医疗保险或错误信息
     */
    @PostMapping("/getAssociatedMedicalInsurance")
    public CommonResult<MedicalInsurance> getAssociatedMedicalInsurance(@RequestParam Long id) {
        // 调用服务获取关联医疗保险
        MedicalInsurance medicalInsurance = associatedMedicalInsuranceService.getAssociatedMedicalInsurance(id);
        if (medicalInsurance == null) {
            log.info("未找到与ID={}相关联的医疗保险", id);
            return CommonResult.failed("未找到与指定ID相关联的医疗保险");
        }
        return CommonResult.success(medicalInsurance);
    }
}
