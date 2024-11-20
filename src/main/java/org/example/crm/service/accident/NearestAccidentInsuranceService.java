package org.example.crm.service.accident;

import org.example.crm.entity.accident.AccidentInsurance;

/**
 * 意外险临近算法服务接口
 * @author
 * @date 2024/11/14
 */
public interface NearestAccidentInsuranceService {

    /**
     * 获取最临近的意外险
     * @param id 意外险ID
     * @return 意外险实体
     */
    AccidentInsurance findNearestAccidentInsurance(Long id);

    /**
     * 获取最受欢迎的意外险
     * @return 意外险实体
     */
    AccidentInsurance findMostPopularAccidentInsurance();
}
