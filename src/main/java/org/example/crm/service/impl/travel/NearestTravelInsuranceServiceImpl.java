package org.example.crm.service.impl.travel;

import jakarta.annotation.Resource;
import org.example.crm.entity.travel.TravelInsurance;
import org.example.crm.mapper.travel.TravelInsuranceMapper;
import org.example.crm.service.travel.NearestTravelInsuranceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;


/**
 * @author hutianlin
 * 2024/11/14 20:32
 */
@Service
public class NearestTravelInsuranceServiceImpl implements NearestTravelInsuranceService {

    private static final Logger log = LoggerFactory.getLogger(NearestTravelInsuranceServiceImpl.class);

    @Resource
    private TravelInsuranceMapper travelInsuranceMapper;

    @Value("${travelInsurance.maximumDistance}")
    private double maximumDistance;

    @Override
    public TravelInsurance getNearestTravelInsurance(Long id) {
        TravelInsurance currentTravelInsurance = travelInsuranceMapper.selectById(id);
        List<TravelInsurance> travelInsurances = travelInsuranceMapper.selectAll();
        int priceMaxDistance = calculateMaxDistance(travelInsurances, TravelInsurance::getPrice);
        int effectPeriodMaxDistance = calculateMaxDistance(travelInsurances, TravelInsurance::getEffectPeriod);
        int tripDelayMaxDistance = calculateMaxDistance(travelInsurances, TravelInsurance::getTripDelay);
        int accidentDeathMaxDistance = calculateMaxDistance(travelInsurances, TravelInsurance::getAccidentDeath);
        int medicalCoverageMaxDistance = calculateMaxDistance(travelInsurances, TravelInsurance::getMedicalCoverage);
        int emergencyAssistanceMaxDistance = calculateMaxDistance(travelInsurances, TravelInsurance::getEmergencyAssistance);
        int personalPropertyMaxDistance = calculateMaxDistance(travelInsurances, TravelInsurance::getPersonalProperty);
        int personalLiabilityMaxDistance = calculateMaxDistance(travelInsurances, TravelInsurance::getPersonalLiability);

        if (tripDelayMaxDistance == 0
                && accidentDeathMaxDistance == 0
                && medicalCoverageMaxDistance == 0
                && emergencyAssistanceMaxDistance == 0
                && personalPropertyMaxDistance == 0
                && personalLiabilityMaxDistance == 0) {
            log.info("旅游保险数据异常，出现null值");
            return null;
        }

        double minDistance = Double.MAX_VALUE;
        TravelInsurance nearestTravelInsurance = null;

        for (TravelInsurance travelInsurance : travelInsurances) {
            if (travelInsurance.getId().equals(id)) {
                continue;
            }
            double distance = Math.sqrt(Math.pow((double) Math.abs(currentTravelInsurance.getPrice() - travelInsurance.getPrice())/priceMaxDistance, 2)
                    + Math.pow((double) Math.abs(currentTravelInsurance.getEffectPeriod() - travelInsurance.getEffectPeriod())/effectPeriodMaxDistance, 2)
                    + Math.pow((double) Math.abs(currentTravelInsurance.getTripDelay() - travelInsurance.getTripDelay())/tripDelayMaxDistance, 2)
                    + Math.pow((double) Math.abs(currentTravelInsurance.getAccidentDeath() - travelInsurance.getAccidentDeath())/accidentDeathMaxDistance, 2)
                    + Math.pow((double) Math.abs(currentTravelInsurance.getMedicalCoverage() - travelInsurance.getMedicalCoverage())/medicalCoverageMaxDistance, 2)
                    + Math.pow((double) Math.abs(currentTravelInsurance.getEmergencyAssistance() - travelInsurance.getEmergencyAssistance())/emergencyAssistanceMaxDistance, 2)
                    + Math.pow((double) Math.abs(currentTravelInsurance.getPersonalProperty() - travelInsurance.getPersonalProperty())/personalPropertyMaxDistance, 2)
                    + Math.pow((double) Math.abs(currentTravelInsurance.getPersonalLiability() - travelInsurance.getPersonalLiability())/personalLiabilityMaxDistance, 2));

            if (distance < minDistance) {
                minDistance = distance;
                nearestTravelInsurance = travelInsurance;
            }
        }
        if (nearestTravelInsurance == null) {
            log.info("未找到最近的旅游保险");
        }

        if (minDistance > maximumDistance) {
            log.info("欧氏距离过远，返回null");
            return null;
        }
        return nearestTravelInsurance;
    }

    @Override
    public TravelInsurance getMostPopularTravelInsurance() {
        List<TravelInsurance> travelInsurances = travelInsuranceMapper.selectAll();
        Map<Long, Integer> map = new HashMap<>();

        for (TravelInsurance travelInsurance : travelInsurances) {
            map.merge(travelInsurance.getId(), 1, Integer::sum);
        }

        Map.Entry<Long, Integer> maxEntry = map.entrySet().stream().max(Map.Entry.comparingByValue()).orElse(null);
        if (maxEntry != null) {
            TravelInsurance mostPopularTravelInsurance = travelInsuranceMapper.selectById(maxEntry.getKey());
            if (mostPopularTravelInsurance == null) {
                log.info("未找到最受欢迎的旅游保险id对应的保险");
            }
            return mostPopularTravelInsurance;
        }
        log.info("未找到最受欢迎的旅游保险");
        return null;
    }

    // 计算最大距离
    private int calculateMaxDistance(List<TravelInsurance> travelInsurances, ToIntFunction<TravelInsurance> mapper) {
        return travelInsurances.stream().mapToInt(mapper).max().orElse(0)
                - travelInsurances.stream().mapToInt(mapper).min().orElse(0);
    }

}
