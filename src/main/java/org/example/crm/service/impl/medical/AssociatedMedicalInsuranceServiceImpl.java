package org.example.crm.service.impl.medical;

import jakarta.annotation.Resource;
import org.example.crm.entity.medical.MedicalInsurance;
import org.example.crm.mapper.medical.MedicalInsuranceMapper;
import org.example.crm.mapper.medical.MedicalOrderRecordMapper;
import org.example.crm.service.medical.AssociatedMedicalInsuranceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 医疗保险关联算法服务实现类
 * 用于通过购买记录分析保险之间的关联
 * @author
 * 2024/11/19
 */
@Service
public class AssociatedMedicalInsuranceServiceImpl implements AssociatedMedicalInsuranceService {

    private static final Logger log = LoggerFactory.getLogger(AssociatedMedicalInsuranceServiceImpl.class);

    @Resource
    private MedicalOrderRecordMapper medicalOrderRecordMapper;

    @Resource
    private MedicalInsuranceMapper medicalInsuranceMapper;

    @Value("${medicalOrderRecord.minimumSupport}")
    private double MINIMUM_SUPPORT;

    @Override
    public MedicalInsurance getAssociatedMedicalInsurance(Long id) {
        MedicalInsurance medicalInsurance = null;

        // 获取所有用户的购买计划 ID 列表
        List<Long> userIds = medicalOrderRecordMapper.selectAllUserIds();
        List<Set<Long>> purchaseHistory = new ArrayList<>();

        for (Long userId : userIds) {
            // 获取每个用户购买的保险计划 ID 集合
            List<Long> planIds = medicalOrderRecordMapper.selectPlanIdByUserId(userId);
            if (!planIds.isEmpty()) {
                purchaseHistory.add(new HashSet<>(planIds));
            }
        }

        // 获取单项集合和双项集合
        Map<Set<Long>, Double> oneTermSetMap = getOneTermSetMap(purchaseHistory);
        Map<Set<Long>, Double> twoTermSetMap = getTwoTermSetMap(purchaseHistory, oneTermSetMap);
        Map<Set<Long>, Double> liftMap = calculateLift(oneTermSetMap, twoTermSetMap);

        // 找到与目标 ID 关联度最高的保险
        double maxLift = 0.0;
        for (Map.Entry<Set<Long>, Double> entry : liftMap.entrySet()) {
            Set<Long> set = entry.getKey();
            double lift = entry.getValue();
            if (set.contains(id) && lift > maxLift) {
                for (Long associatedId : set) {
                    if (!associatedId.equals(id)) {
                        medicalInsurance = medicalInsuranceMapper.selectById(associatedId);
                        maxLift = lift;
                        break;
                    }
                }
            }
        }

        if (medicalInsurance == null) {
            log.info("未找到与 ID 为 {} 的保险相关的医疗保险", id);
        }

        return medicalInsurance;
    }

    private Map<Set<Long>, Double> getOneTermSetMap(List<Set<Long>> purchaseHistory) {
        Map<Set<Long>, Double> oneTermSetMap = new HashMap<>();
        Map<Long, Integer> countMap = new HashMap<>();
        int totalTransactions = purchaseHistory.size();

        for (Set<Long> transaction : purchaseHistory) {
            for (Long itemId : transaction) {
                countMap.merge(itemId, 1, Integer::sum);
            }
        }

        for (Map.Entry<Long, Integer> entry : countMap.entrySet()) {
            double support = (double) entry.getValue() / totalTransactions;
            if (support >= MINIMUM_SUPPORT) {
                Set<Long> itemSet = Collections.singleton(entry.getKey());
                oneTermSetMap.put(itemSet, support);
            }
        }

        return oneTermSetMap;
    }

    private Map<Set<Long>, Double> getTwoTermSetMap(List<Set<Long>> purchaseHistory, Map<Set<Long>, Double> oneTermSetMap) {
        Map<Set<Long>, Double> twoTermSetMap = new HashMap<>();
        Map<Set<Long>, Integer> countMap = new HashMap<>();
        int totalTransactions = purchaseHistory.size();

        for (Set<Long> transaction : purchaseHistory) {
            for (Set<Long> set1 : oneTermSetMap.keySet()) {
                for (Set<Long> set2 : oneTermSetMap.keySet()) {
                    if (!set1.equals(set2)) {
                        Set<Long> combinedSet = new HashSet<>(set1);
                        combinedSet.addAll(set2);
                        if (transaction.containsAll(combinedSet)) {
                            countMap.merge(combinedSet, 1, Integer::sum);
                        }
                    }
                }
            }
        }

        for (Map.Entry<Set<Long>, Integer> entry : countMap.entrySet()) {
            double support = (double) entry.getValue() / totalTransactions;
            if (support >= MINIMUM_SUPPORT) {
                twoTermSetMap.put(entry.getKey(), support);
            }
        }

        return twoTermSetMap;
    }

    private Map<Set<Long>, Double> calculateLift(Map<Set<Long>, Double> oneTermSetMap, Map<Set<Long>, Double> twoTermSetMap) {
        Map<Set<Long>, Double> liftMap = new HashMap<>();

        for (Map.Entry<Set<Long>, Double> entry : twoTermSetMap.entrySet()) {
            Set<Long> set = entry.getKey();
            double support = entry.getValue();
            double product = 1.0;
            for (Long itemId : set) {
                product *= oneTermSetMap.get(Collections.singleton(itemId));
            }
            double lift = support / product;
            liftMap.put(set, lift);
        }

        return liftMap;
    }
}
