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
    private AccidentInsuranceMapper accidentInsuranceMapper; // 注入意外险数据的 Mapper，负责数据访问操作

    @Value("${accidentInsurance.maxDistance}")
    private double maxDistanceThreshold; // 配置项，表示最大允许距离阈值

    @Override
    public AccidentInsurance findNearestAccidentInsurance(Long id) {
        // 根据 ID 获取当前意外险数据
        AccidentInsurance currentInsurance = accidentInsuranceMapper.findById(id);
        if (currentInsurance == null) {
            // 如果当前意外险不存在，记录日志并返回 null
            logger.warn("未找到当前意外险数据，ID: {}", id);
            return null;
        }

        // 获取所有意外险数据
        List<AccidentInsurance> allInsurances = accidentInsuranceMapper.findAll();
        if (allInsurances.isEmpty()) {
            // 如果没有任何意外险数据，记录日志并返回 null
            logger.warn("未找到任何意外险数据");
            return null;
        }

        // 计算所有字段的最大差距，用于归一化距离计算
        Map<String, Integer> maxDistances = calculateMaxDistances(allInsurances);

        double closestDistance = Double.MAX_VALUE; // 初始化最近距离为最大值
        AccidentInsurance closestInsurance = null; // 初始化最近保险为 null

        // 遍历所有意外险数据，计算与当前意外险的距离
        for (AccidentInsurance insurance : allInsurances) {
            // 跳过当前保险自身
            if (insurance.getId().equals(id)) continue;

            // 计算当前保险与目标保险的归一化距离
            double distance = computeDistance(currentInsurance, insurance, maxDistances);

            // 如果找到更近的保险，更新最近距离和最近保险
            if (distance < closestDistance) {
                closestDistance = distance;
                closestInsurance = insurance;
            }
        }

        // 如果没有找到任何临近的保险，记录日志并返回 null
        if (closestInsurance == null) {
            logger.info("未找到临近的意外险");
            return null;
        }

        // 如果找到的最近保险的距离超过阈值，记录日志并返回 null
        if (closestDistance > maxDistanceThreshold) {
            logger.info("距离超过最大阈值：{}", maxDistanceThreshold);
            return null;
        }

        // 返回找到的最近保险
        return closestInsurance;
    }

    @Override
    public AccidentInsurance findMostPopularAccidentInsurance() {
        // 获取所有意外险数据
        List<AccidentInsurance> allInsurances = accidentInsuranceMapper.findAll();
        if (allInsurances.isEmpty()) {
            // 如果没有任何意外险数据，记录日志并返回 null
            logger.info("没有任何意外险数据");
            return null;
        }

        // 统计每个保险的出现次数
        Map<Long, Long> popularityMap = new HashMap<>();
        allInsurances.forEach(insurance ->
                // 使用 merge 方法累加保险 ID 的出现次数
                popularityMap.merge(insurance.getId().longValue(), 1L, Long::sum)
        );

        // 找出出现次数最多的保险 ID 并返回对应的保险对象
        return popularityMap.entrySet().stream()
                .max(Map.Entry.comparingByValue()) // 按出现次数降序排序
                .map(entry -> accidentInsuranceMapper.findById(entry.getKey())) // 根据 ID 获取保险对象
                .orElse(null); // 如果没有数据，返回 null
    }

    // 计算所有字段的最大差距，用于归一化距离计算
    private Map<String, Integer> calculateMaxDistances(List<AccidentInsurance> insurances) {
        // 定义字段名到获取字段值方法的映射
        Map<String, Function<AccidentInsurance, Integer>> fields = new LinkedHashMap<>();
        fields.put("price", AccidentInsurance::getPrice); // 价格字段
        fields.put("effectPeriod", AccidentInsurance::getEffectPeriod); // 有效期字段
        fields.put("accidentCoverage", AccidentInsurance::getAccidentCoverage); // 意外保障字段
        fields.put("disabilityCoverage", AccidentInsurance::getDisabilityCoverage); // 残疾保障字段
        fields.put("hospitalCoverage", AccidentInsurance::getHospitalCoverage); // 住院保障字段
        fields.put("outpatientCoverage", AccidentInsurance::getOutpatientCoverage); // 门诊保障字段
        fields.put("emergencyAssistance", AccidentInsurance::getEmergencyAssistance); // 紧急救援字段
        fields.put("personalLiability", AccidentInsurance::getPersonalLiability); // 个人责任字段

        Map<String, Integer> maxDistances = new HashMap<>();
        // 遍历每个字段，计算最大差距
        fields.forEach((fieldName, getter) -> {
            int max = insurances.stream().mapToInt(getter::apply).max().orElse(0); // 最大值
            int min = insurances.stream().mapToInt(getter::apply).min().orElse(0); // 最小值
            maxDistances.put(fieldName, max - min); // 差值存入 map
        });

        return maxDistances; // 返回字段最大差距的映射
    }

    // 计算两个保险之间的多维距离
    private double computeDistance(AccidentInsurance current, AccidentInsurance other, Map<String, Integer> maxDistances) {
        double sumSquaredDifferences = 0.0; // 初始化平方差和

        // 遍历每个字段，计算归一化的平方差
        for (Map.Entry<String, Integer> entry : maxDistances.entrySet()) {
            String fieldName = entry.getKey();
            int maxDistance = entry.getValue();

            // 如果字段的最大差距为 0，跳过该字段
            if (maxDistance != 0) {
                int currentValue = getFieldValue(current, fieldName); // 当前保险的字段值
                int otherValue = getFieldValue(other, fieldName); // 目标保险的字段值
                // 计算归一化的平方差
                sumSquaredDifferences += Math.pow((double) Math.abs(currentValue - otherValue) / maxDistance, 2);
            }
        }

        return Math.sqrt(sumSquaredDifferences); // 返回欧几里得距离
    }

    // 根据字段名获取保险的对应字段值
    private int getFieldValue(AccidentInsurance insurance, String fieldName) {
        switch (fieldName) {
            case "price": return insurance.getPrice(); // 获取价格
            case "effectPeriod": return insurance.getEffectPeriod(); // 获取有效期
            case "accidentCoverage": return insurance.getAccidentCoverage(); // 获取意外保障
            case "disabilityCoverage": return insurance.getDisabilityCoverage(); // 获取残疾保障
            case "hospitalCoverage": return insurance.getHospitalCoverage(); // 获取住院保障
            case "outpatientCoverage": return insurance.getOutpatientCoverage(); // 获取门诊保障
            case "emergencyAssistance": return insurance.getEmergencyAssistance(); // 获取紧急救援
            case "personalLiability": return insurance.getPersonalLiability(); // 获取个人责任
            default: throw new IllegalArgumentException("Unknown field: " + fieldName); // 如果字段未知，抛出异常
        }
    }
}
