package hwangjihun.coronamonitor.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class CoronaApiServiceTest {

    @Test
    @DisplayName("AreaChart Test")
    void getAreaChart() {
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<Integer> months = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 1; i < calendar.get(Calendar.MONTH) + 1; i++) {
            months.add(i);
            labels.add(MessageFormat.format("{0} 월", i));
        }

        assertThat(labels.size()).isEqualTo(9);
    }

    @Test
    @DisplayName("월말 구하기 테스트")
    void maximumDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        assertThat(calendar.getActualMaximum(Calendar.DAY_OF_MONTH)).isEqualTo(30);
    }

    @Test
    @DisplayName("월말 구하기 테스트2")
    void maximumDay2() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        for (int i = 1; i <= 12; i++) {
            today.add(Calendar.MONTH, -1);
            today.set(Calendar.DATE, today.getActualMaximum(Calendar.DAY_OF_MONTH));
            log.info("today = {}", simpleDateFormat.format(today.getTime()));
        }
    }

    @Test
    @DisplayName("월말 구하기 테스트2")
    void maximumDay3() {
        ArrayList<String> labels = new ArrayList<>();
        List<String> lastDaysList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        for (int i = 1; i <= 12; i++) {
            today.add(Calendar.MONTH, -1);
            today.set(Calendar.DATE, today.getActualMaximum(Calendar.DAY_OF_MONTH));

            log.info("labels = {}", MessageFormat.format("{0} 월",  today.get(Calendar.MONTH) + 1));
            log.info("dates = {}", simpleDateFormat.format(today.getTime()));
        }
    }
}