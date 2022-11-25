package hwangjihun.coronamonitor.service;

import hwangjihun.coronamonitor.domain.corona.CoronaData;
import hwangjihun.coronamonitor.domain.corona.CoronaTableDto;
import hwangjihun.coronamonitor.repository.CoronaOpenApiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class CoronaApiService {

    @Autowired
    private CoronaOpenApiRepository coronaOpenApiRepository;

    public List<CoronaData> findCoronaApi(int rangeNum) throws IOException {
        return coronaOpenApiRepository.getApiData(rangeNum);
    }

    @Transactional
    public CoronaData save(CoronaData coronaData) {
        return coronaOpenApiRepository.save(coronaData);
    }

    @Transactional
    public void update(String statusDt, CoronaTableDto coronaTableDto) {
        coronaOpenApiRepository.update(statusDt, coronaTableDto);
    }

    public Optional<CoronaData> findById(String statusDt) {
        return coronaOpenApiRepository.findById(statusDt);
    }

    public List<CoronaData> findAll() {
        return coronaOpenApiRepository.findAll();
    }

    public List<Float> getCardsValue() {
        ArrayList<Float> returnValue = new ArrayList<>();
        Optional<CoronaData> a = coronaOpenApiRepository.findById(coronaOpenApiRepository.getEditedDate(1));
        Optional<CoronaData> b = coronaOpenApiRepository.findById(coronaOpenApiRepository.getEditedDate(2));
        CoronaData aCoronaData = a.orElse(null);
        CoronaData bCoronaData = b.orElse(null);
        if (bCoronaData != null && aCoronaData != null) {
            returnValue.add(Float.valueOf(aCoronaData.getHPntCnt() - bCoronaData.getHPntCnt()));
            returnValue.add(Float.valueOf(aCoronaData.getGPntCnt() - bCoronaData.getGPntCnt()));
            returnValue.add(Float.valueOf(Float.valueOf(aCoronaData.getGPntCnt()) / Float.valueOf(aCoronaData.getHPntCnt()) * 100));
        } else {
            returnValue.add(Float.MIN_VALUE);
            returnValue.add(Float.MIN_VALUE);
            returnValue.add(Float.MIN_VALUE);
        }

        return returnValue;
    }

    public Map<String, Integer> getAreaChart() {
        Map<String, Integer> returnValuesMap = new LinkedHashMap<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar today = Calendar.getInstance();

        for (int i = 12; i >= 1; i--) {
            today.setTime(new Date());
            today.add(Calendar.MONTH, i * -1);
            today.set(Calendar.DATE, today.getActualMaximum(Calendar.DAY_OF_MONTH));

            String lastDay = simpleDateFormat.format(today.getTime());
            returnValuesMap.put(lastDay, 0);

            CoronaData coronaData = coronaOpenApiRepository.findById(lastDay).orElse(null);
            if (coronaData != null) {
                returnValuesMap.replace(lastDay, coronaData.getHPntCnt());
            }
        }

        return returnValuesMap;
    }

    public List<Integer> getPieChart() {

        int allPopulation = 51466658;
        int numberOfNonConfirmedCases = 0;
        int numberOfNonDeathConfirmedCases = 0;
        int numberOfDeathConfirmedCases = 0;
        List<Integer> returnValues = new ArrayList<>();
        List<CoronaData> recentCoronaDataList = coronaOpenApiRepository.getRecentCoronaData();
        CoronaData recentCoronaData = null;
        if (recentCoronaDataList.size() != 0) {
            recentCoronaData = recentCoronaDataList.get(0);
        }
        if (recentCoronaData != null) {
            numberOfNonConfirmedCases = allPopulation - recentCoronaData.getHPntCnt();
            numberOfNonDeathConfirmedCases = recentCoronaData.getHPntCnt() - recentCoronaData.getGPntCnt();
            numberOfDeathConfirmedCases = recentCoronaData.getGPntCnt();
        }
        returnValues.add(numberOfNonConfirmedCases);
        returnValues.add(numberOfNonDeathConfirmedCases);
        returnValues.add(numberOfDeathConfirmedCases);

        return returnValues;
    }

    public Map<String, Integer> getBarChart() {

        Map<String, Integer> returnValuesMap = new LinkedHashMap<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar today = Calendar.getInstance();

        //  TODO 중복 해결
        for (int i = 13; i >= 1; i--) {
            today.setTime(new Date());
            today.add(Calendar.MONTH, i * -1);
            today.set(Calendar.DATE, today.getActualMaximum(Calendar.DAY_OF_MONTH));

            String lastDay = simpleDateFormat.format(today.getTime());
            returnValuesMap.put(lastDay, 0);

            CoronaData coronaData = coronaOpenApiRepository.findById(lastDay).orElse(null);
            if (coronaData != null) {
                returnValuesMap.replace(lastDay, coronaData.getGPntCnt());
            }
        }

        return returnValuesMap;
    }

    @Transactional
    public int deleteAll() {

        return coronaOpenApiRepository.deleteAll();
    }
}
