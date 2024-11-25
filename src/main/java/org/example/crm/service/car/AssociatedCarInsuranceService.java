package org.example.crm.service.car;

import org.example.crm.entity.car.CarInsurance;
import java.util.List;//接收汽车保险数据
/**
 * @author     ：eco
 * @date       ：Created in 2024/11/24 10:57
 */
public interface AssociatedCarInsuranceService {
    /**
     * 根据关联算法获取的汽车保险
     *
     * @param id 汽车保险id
     * @return 汽车保险entity
     */
    CarInsurance getAssociatedCarInsurance(Long id);

    /**
     * 获取所有的车险记录
     * @return CommonResult 包含所有车险的列表
     */
    List<CarInsurance> getAllCarInsurances(); // 新增方法
}
