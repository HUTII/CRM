package org.example.crm.service.medical;

import org.example.crm.entity.medical.MedicalInsurance;

/**
 * 邻近算法服务接口
 * 用于推荐医疗保险
 */
public interface NearestMedicalInsuranceService {
    /**
     * 获取最临近的医疗保险
     * @param id 医疗保险id
     * @return 医疗保险实体
     */
    MedicalInsurance getNearestMedicalInsurance(Long id);

    /**
     * 获取最受欢迎的医疗保险
     * @return 医疗保险实体
     */
    MedicalInsurance getMostPopularMedicalInsurance();
}
