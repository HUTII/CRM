package org.example.crm.service.medical;

import org.example.crm.entity.medical.MedicalInsurance;

/**
 * 关联算法服务接口
 * 用于根据关联算法获取医疗保险推荐结果
 *
 * @author
 * 2024/11/19
 */
public interface AssociatedMedicalInsuranceService {

    /**
     * 根据关联算法获取的医疗保险
     *
     * @param id 医疗保险id
     * @return 医疗保险entity
     */
    MedicalInsurance getAssociatedMedicalInsurance(Long id);
}
