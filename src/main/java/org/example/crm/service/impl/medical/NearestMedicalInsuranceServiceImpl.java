package org.example.crm.service.impl.medical;

import jakarta.annotation.Resource;
import org.example.crm.entity.medical.MedicalInsurance;
import org.example.crm.mapper.medical.MedicalInsuranceMapper;
import org.example.crm.service.medical.NearestMedicalInsuranceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;

/**
 * 基于邻近算法的医疗保险推荐服务实现
 */
@Service
public class NearestMedicalInsuranceServiceImpl implements NearestMedicalInsuranceService {

    private static final Logger log = LoggerFactory.getLogger(NearestMedicalInsuranceServiceImpl.class);

    @Resource
    private MedicalInsuranceMapper medicalInsuranceMapper;

    @Value("1.2")
    private double maximumDistance;

    @Override
    public MedicalInsurance getNearestMedicalInsurance(Long id) {
        MedicalInsurance currentInsurance = medicalInsuranceMapper.selectById(id);
        List<MedicalInsurance> medicalInsurances = medicalInsuranceMapper.selectAll();

        if (currentInsurance == null || medicalInsurances.isEmpty()) {
            log.info("当前保险或保险列表为空");
            return null;
        }

        // 计算各指标的最大距离
        int priceMaxDistance = calculateMaxDistance(medicalInsurances, MedicalInsurance::getPrice);
        int effectPeriodMaxDistance = calculateMaxDistance(medicalInsurances, MedicalInsurance::getEffectPeriod);
        int hospitalCoverageMaxDistance = calculateMaxDistance(medicalInsurances, MedicalInsurance::getHospitalCoverage);
        int outpatientCoverageMaxDistance = calculateMaxDistance(medicalInsurances, MedicalInsurance::getOutpatientCoverage);
        int surgeryCoverageMaxDistance = calculateMaxDistance(medicalInsurances, MedicalInsurance::getSurgeryCoverage);
        int criticalIllnessMaxDistance = calculateMaxDistance(medicalInsurances, MedicalInsurance::getCriticalIllness);
        int emergencyAssistanceMaxDistance = calculateMaxDistance(medicalInsurances, MedicalInsurance::getEmergencyAssistance);

        if (priceMaxDistance == 0 && effectPeriodMaxDistance == 0
                && hospitalCoverageMaxDistance == 0 && outpatientCoverageMaxDistance == 0
                && surgeryCoverageMaxDistance == 0 && criticalIllnessMaxDistance == 0
                && emergencyAssistanceMaxDistance == 0) {
            log.info("保险数据异常，所有字段的最大距离为0");
            return null;
        }

        double minDistance = Double.MAX_VALUE;
        MedicalInsurance nearestInsurance = null;

        for (MedicalInsurance insurance : medicalInsurances) {
            if (insurance.getId().equals(id)) {
                continue;
            }

            // 计算欧氏距离
            double distance = Math.sqrt(
                    Math.pow((double) Math.abs(currentInsurance.getPrice() - insurance.getPrice()) / priceMaxDistance, 2)
                            + Math.pow((double) Math.abs(currentInsurance.getEffectPeriod() - insurance.getEffectPeriod()) / effectPeriodMaxDistance, 2)
                            + Math.pow((double) Math.abs(currentInsurance.getHospitalCoverage() - insurance.getHospitalCoverage()) / hospitalCoverageMaxDistance, 2)
                            + Math.pow((double) Math.abs(currentInsurance.getOutpatientCoverage() - insurance.getOutpatientCoverage()) / outpatientCoverageMaxDistance, 2)
                            + Math.pow((double) Math.abs(currentInsurance.getSurgeryCoverage() - insurance.getSurgeryCoverage()) / surgeryCoverageMaxDistance, 2)
                            + Math.pow((double) Math.abs(currentInsurance.getCriticalIllness() - insurance.getCriticalIllness()) / criticalIllnessMaxDistance, 2)
                            + Math.pow((double) Math.abs(currentInsurance.getEmergencyAssistance() - insurance.getEmergencyAssistance()) / emergencyAssistanceMaxDistance, 2)
            );

            if (distance < minDistance) {
                minDistance = distance;
                nearestInsurance = insurance;
            }
        }

        if (nearestInsurance == null || minDistance > maximumDistance) {
            log.info("未找到满足条件的最近医疗保险，或距离超出最大允许范围");
            return null;
        }

        return nearestInsurance;
    }

    @Override
    public MedicalInsurance getMostPopularMedicalInsurance() {
        List<MedicalInsurance> insurances = medicalInsuranceMapper.selectAll();
        Map<Long, Integer> purchaseCounts = new HashMap<>();

        for (MedicalInsurance insurance : insurances) {
            purchaseCounts.merge(insurance.getId(), 1, Integer::sum);
        }

        Map.Entry<Long, Integer> maxEntry = purchaseCounts.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        if (maxEntry != null) {
            MedicalInsurance mostPopularInsurance = medicalInsuranceMapper.selectById(maxEntry.getKey());
            if (mostPopularInsurance == null) {
                log.info("未找到最受欢迎的医疗保险ID对应的保险");
            }
            return mostPopularInsurance;
        }
        log.info("未找到最受欢迎的医疗保险");
        return null;
    }

    // 计算字段的最大距离
    private int calculateMaxDistance(List<MedicalInsurance> medicalInsurances, ToIntFunction<MedicalInsurance> mapper) {
        return medicalInsurances.stream().mapToInt(mapper).max().orElse(0)
                - medicalInsurances.stream().mapToInt(mapper).min().orElse(0);
    }
}
