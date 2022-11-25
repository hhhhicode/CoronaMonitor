package hwangjihun.coronamonitor.scheduler;

import hwangjihun.coronamonitor.domain.corona.CoronaData;
import hwangjihun.coronamonitor.repository.CoronaOpenApiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class Scheduler {

    @Autowired
    private CoronaOpenApiRepository coronaOpenApiRepository;

    @Scheduled(cron = "0 30 5 * * ?")
    @Transactional
    public void CoronaApiDbScheduler(int rangeNum) throws IOException {

        int count = coronaOpenApiRepository.deleteAll();
        log.info("영향을 받은 row count = {}", count);

        List<CoronaData> coronaDataList = coronaOpenApiRepository.getApiData(rangeNum);
        for (CoronaData coronaData : coronaDataList) {
            coronaOpenApiRepository.save(coronaData);
        }
    }
}
