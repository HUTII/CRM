package org.example.crm.service.impl.travel;

import jakarta.annotation.Resource;
import org.example.crm.entity.travel.TravelInsurance;
import org.example.crm.mapper.travel.TravelInsuranceMapper;
import org.example.crm.mapper.travel.TravelOrderRecordMapper;
import org.example.crm.service.travel.AssociatedTravelInsuranceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 关联算法服务接口实现类
 * @author hutianlin
 * 2024/11/15 09:46
 */
@Service
public class AssociatedTravelInsuranceServiceImpl implements AssociatedTravelInsuranceService {

    private static final Logger log = LoggerFactory.getLogger(AssociatedTravelInsuranceServiceImpl.class);
    @Resource
    private TravelOrderRecordMapper travelOrderRecordMapper;

    @Resource
    private TravelInsuranceMapper travelInsuranceMapper;

    @Value("${travelOrderRecord.minimumSupport}")
    private double MINIMUM_SUPPORT;

    @Override
    public TravelInsurance getAssociatedTravelInsurance(Long id) {
        TravelInsurance travelInsurance = null;
        // 获取所有用户名
        List<String> usernameList = travelOrderRecordMapper.selectAllUsername();
        List<Set<Long>> orderList = new ArrayList<>();
        for (String username : usernameList) {
            //  获取每个用户购买的保险id集合
            List<Long> list = travelOrderRecordMapper.selectItemIdByUsername(username);
            //  如果该集合非空则加入List
            if (!list.isEmpty()) {
                Set<Long> set = new HashSet<>(list);
                orderList.add(set);
            }
        }

        //  获取单项集合和双项集合
        Map<Set<Long>, Double> oneTermSetMap = getOneTermSetMap(orderList);
        Map<Set<Long>, Double> twoTermSetMap = getTwoTermSetMap(orderList, oneTermSetMap);
        Map<Set<Long>, Double> lift = new HashMap<>();

        //  遍历双项集合，计算提升度
        for (Map.Entry<Set<Long>, Double> entry : twoTermSetMap.entrySet()) {
            Set<Long> set = entry.getKey();
            double support = entry.getValue();
            double production = 1.0;
            for (Long itemId : set) {
                production *= oneTermSetMap.get(Collections.singleton(itemId));
            }
            double liftValue = support / production;
            lift.put(set, liftValue);
        }

        double maxLift = 0.0;

        //  遍历提升度，找到提升度最大的关联的旅游保险
        for (Map.Entry<Set<Long>, Double> entry : lift.entrySet()) {
            if (entry.getValue() < 1.0) {
                continue;
            }
            Set<Long> set = entry.getKey();
            if (set.contains(id)) {
                for (Long itemId : set) {
                    if (!itemId.equals(id) && lift.get(set) > maxLift) {
                        travelInsurance = travelInsuranceMapper.selectById(itemId);
                        maxLift = lift.get(set);
                        break;
                    }
                }
            }
        }

        if (travelInsurance == null) {
            log.info("关联算法未找到关联的旅游保险");
            return null;
        }

        return travelInsurance;
    }

    private Map<Set<Long>, Double> getOneTermSetMap(List<Set<Long>> orderList) {
        Map<Set<Long>, Double> oneTermSetMap = new HashMap<>();
        Map<Long, Integer> countMap = new HashMap<>();
        int size = orderList.size();

        for (Set<Long> set : orderList) {
            for (Long itemId : set) {
                countMap.merge(itemId, 1, Integer::sum);
            }
        }

        for (Map.Entry<Long, Integer> entry : countMap.entrySet()) {
            double support = (double) entry.getValue() / size;
            if (support >= MINIMUM_SUPPORT) {
                Set<Long> oneTermSet = new HashSet<>();
                oneTermSet.add(entry.getKey());
                oneTermSetMap.put(oneTermSet, support);
            }
        }

        return oneTermSetMap;
    }

    private Map<Set<Long>, Double> getTwoTermSetMap(List<Set<Long>> orderList, Map<Set<Long>, Double> oneTermSetMap) {
        Map<Set<Long>, Double> twoTermSetMap = new HashMap<>();
        Map<Set<Long>, Integer> countMap = new HashMap<>();
        int size = orderList.size();

        for (Set<Long> set1 : oneTermSetMap.keySet()) {
            for (Set<Long> set2 : oneTermSetMap.keySet()) {
                if (!set1.equals(set2)) {
                    Set<Long> set = new HashSet<>();
                    set.addAll(set1);
                    set.addAll(set2);
                    for (Set<Long> order : orderList) {
                        if (order.containsAll(set)) {
                            countMap.merge(set, 1, Integer::sum);
                        }
                    }
                }
            }
        }

        countMap.replaceAll((k, v) -> v / 2);

        for (Map.Entry<Set<Long>, Integer> entry : countMap.entrySet()) {
            double support = (double) entry.getValue() / size;
            if (support >= MINIMUM_SUPPORT) {
                twoTermSetMap.put(entry.getKey(), support);
            }
        }

        return twoTermSetMap;
    }
}
