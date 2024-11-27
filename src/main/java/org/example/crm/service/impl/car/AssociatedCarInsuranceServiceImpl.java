package org.example.crm.service.impl.car;

import jakarta.annotation.Resource;
import org.example.crm.entity.car.CarInsurance;
import org.example.crm.mapper.car.CarInsuranceMapper;
import org.example.crm.mapper.car.CarOrderRecordMapper;
import org.example.crm.service.car.AssociatedCarInsuranceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
/**
 * @author ：eco
 * @date ：Created in 2024/11/24 11:02
 */
@Service
public class AssociatedCarInsuranceServiceImpl implements AssociatedCarInsuranceService {

    private static final Logger log = LoggerFactory.getLogger(org.example.crm.service.impl.car.AssociatedCarInsuranceServiceImpl.class);
    @Resource
    private CarOrderRecordMapper carOrderRecordMapper;

    @Resource
    private CarInsuranceMapper carInsuranceMapper;

    @Value("${travelOrderRecord.minimumSupport}")
    private double MINIMUM_SUPPORT;

    @Override
    public CarInsurance getAssociatedCarInsurance(Long id) {
        CarInsurance carInsurance = null;
        // 获取所有用户名
        List<String> usernameList = carOrderRecordMapper.selectAllUsername();
        List<Set<Long>> orderList = new ArrayList<>();
        for (String username : usernameList) {
            //  获取每个用户购买的保险id集合
            List<Long> list = carOrderRecordMapper.selectItemIdByUsername(username);
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

        //  遍历提升度，找到提升度最大的关联的汽车保险
        for (Map.Entry<Set<Long>, Double> entry : lift.entrySet()) {
            if (entry.getValue() < 1.0) {
                continue;
            }
            Set<Long> set = entry.getKey();
            if (set.contains(id)) {
                for (Long itemId : set) {
                    if (!itemId.equals(id) && lift.get(set) > maxLift) {
                        carInsurance = carInsuranceMapper.selectById(itemId);
                        maxLift = lift.get(set);
                        break;
                    }
                }
            }
        }

        if (carInsurance == null) {
            log.info("关联算法未找到关联的汽车保险");
            return null;
        }

        return carInsurance;
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


    //获取所有车险
    public AssociatedCarInsuranceServiceImpl(CarInsuranceMapper carInsuranceMapper) {
        this.carInsuranceMapper = carInsuranceMapper;
    }

    @Override
    public List<CarInsurance> getAllCarInsurances() {
        return carInsuranceMapper.selectAll();
    }

    @Override
    public CarInsurance getMostPopularCarInsurance() {
        List<CarInsurance> carInsurances = carInsuranceMapper.selectAll();
        Map<Long, Integer> map = new HashMap<>();

        for (CarInsurance carInsurance : carInsurances) {
            map.merge(carInsurance.getProductId(), 1, Integer::sum);
        }

        Map.Entry<Long, Integer> maxEntry = map.entrySet().stream().max(Map.Entry.comparingByValue()).orElse(null);
        if (maxEntry != null) {
            CarInsurance mostPopularCarInsurance = carInsuranceMapper.selectById(maxEntry.getKey());
            if (mostPopularCarInsurance == null) {
                log.info("未找到最受欢迎的汽车保险id对应的保险");
            }
            return mostPopularCarInsurance;
        }
        log.info("未找到最受欢迎的汽车保险");
        return null;
    }
}
