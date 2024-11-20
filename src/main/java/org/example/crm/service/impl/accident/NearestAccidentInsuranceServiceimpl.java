package org.example.crm.service.impl.accident;

import jakarta.annotation.Resource;
import org.example.crm.entity.accident.AccidentInsurance;
import org.example.crm.mapper.accident.AccidentInsuranceMapper;
import org.example.crm.service.accident.NearestAccidentInsuranceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class NearestAccidentInsuranceServiceimpl implements NearestAccidentInsuranceService {
    private static final Logger logger = LoggerFactory.getLogger(NearestAccidentInsuranceServiceimpl.class);

    @Resource
    private AccidentInsuranceMapper accidentInsuranceMapper;

    @Value("${accidentInsurance.maxDistance}")
    private double maxDistanceThreshold;

    @Override
    public AccidentInsurance findNearestAccidentInsurance(Long id) {
        // 获取当前意外险
        AccidentInsurance currentInsurance = accidentInsuranceMapper.findById(id);
        if (currentInsurance == null) {
            logger.warn("未找到当前意外险数据，ID: {}", id);
            return null;
        }

        // 获取所有意外险数据
        List<AccidentInsurance> allInsurances = accidentInsuranceMapper.findAll();
        if (allInsurances.isEmpty()) {
            logger.warn("未找到任何意外险数据");
            return null;
        }

        // 计算各字段的最大距离
        Map<String, Integer> maxDistances = calculateMaxDistances(allInsurances);

        double closestDistance = Double.MAX_VALUE;
        AccidentInsurance closestInsurance = null;

        // 计算与其他保险的距离
        for (AccidentInsurance insurance : allInsurances) {
            if (insurance.getId().equals(id)) continue;

            double distance = computeDistance(currentInsurance, insurance, maxDistances);

            if (distance < closestDistance) {
                closestDistance = distance;
                closestInsurance = insurance;
            }
        }

        if (closestInsurance == null) {
            logger.info("未找到临近的意外险");
            return null;
        }

        if (closestDistance > maxDistanceThreshold) {
            logger.info("距离超过最大阈值：{}", maxDistanceThreshold);
            return null;
        }

        return closestInsurance;
    }

    @Override
    public AccidentInsurance findMostPopularAccidentInsurance() {
        List<AccidentInsurance> allInsurances = accidentInsuranceMapper.findAll();
        if (allInsurances.isEmpty()) {
            logger.info("没有任何意外险数据");
            return null;
        }

        // 修改 popularityMap 的键和值类型为 Long
        Map<Long, Long> popularityMap = new HashMap<>();
        allInsurances.forEach(insurance ->
                popularityMap.merge(insurance.getId().longValue(), 1L, Long::sum)
        );

        // 使用 Long 类型的 key 和 mapper 方法
        return popularityMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> accidentInsuranceMapper.findById(entry.getKey()))
                .orElse(null);
    }

    private Map<String, Integer> calculateMaxDistances(List<AccidentInsurance> insurances) {
        Map<String, Function<AccidentInsurance, Integer>> fields = new LinkedHashMap<>();
        fields.put("price", AccidentInsurance::getPrice);
        fields.put("effectPeriod", AccidentInsurance::getEffectPeriod);
        fields.put("accidentCoverage", AccidentInsurance::getAccidentCoverage);
        fields.put("disabilityCoverage", AccidentInsurance::getDisabilityCoverage);
        fields.put("hospitalCoverage", AccidentInsurance::getHospitalCoverage);
        fields.put("outpatientCoverage", AccidentInsurance::getOutpatientCoverage);
        fields.put("emergencyAssistance", AccidentInsurance::getEmergencyAssistance);
        fields.put("personalLiability", AccidentInsurance::getPersonalLiability);

        Map<String, Integer> maxDistances = new HashMap<>();
        fields.forEach((fieldName, getter) -> {
            int max = insurances.stream().mapToInt(getter::apply).max().orElse(0);
            int min = insurances.stream().mapToInt(getter::apply).min().orElse(0);
            maxDistances.put(fieldName, max - min);
        });

        return maxDistances;
    }

    private double computeDistance(AccidentInsurance current, AccidentInsurance other, Map<String, Integer> maxDistances) {
        double sumSquaredDifferences = 0.0;

        for (Map.Entry<String, Integer> entry : maxDistances.entrySet()) {
            String fieldName = entry.getKey();
            int maxDistance = entry.getValue();

            if (maxDistance != 0) {
                int currentValue = getFieldValue(current, fieldName);
                int otherValue = getFieldValue(other, fieldName);
                sumSquaredDifferences += Math.pow((double) Math.abs(currentValue - otherValue) / maxDistance, 2);
            }
        }

        return Math.sqrt(sumSquaredDifferences);
    }

    private int getFieldValue(AccidentInsurance insurance, String fieldName) {
        switch (fieldName) {
            case "price": return insurance.getPrice();
            case "effectPeriod": return insurance.getEffectPeriod();
            case "accidentCoverage": return insurance.getAccidentCoverage();
            case "disabilityCoverage": return insurance.getDisabilityCoverage();
            case "hospitalCoverage": return insurance.getHospitalCoverage();
            case "outpatientCoverage": return insurance.getOutpatientCoverage();
            case "emergencyAssistance": return insurance.getEmergencyAssistance();
            case "personalLiability": return insurance.getPersonalLiability();
            default: throw new IllegalArgumentException("Unknown field: " + fieldName);
        }
    }
}
