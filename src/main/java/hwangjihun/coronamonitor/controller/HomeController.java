package hwangjihun.coronamonitor.controller;

import hwangjihun.coronamonitor.domain.corona.CoronaData;
import hwangjihun.coronamonitor.domain.corona.CoronaTableDto;
import hwangjihun.coronamonitor.domain.members.Member;
import hwangjihun.coronamonitor.domain.constvalue.members.SessionConst;
import hwangjihun.coronamonitor.service.CoronaApiService;
import hwangjihun.coronamonitor.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
//TODO /error 접근시 Member 객체 전달하는 방법 찾기.
    @ModelAttribute("member")
    public Member member(@SessionAttribute(name = SessionConst.LOGIN_ID, required = false) Long loginId) {
        if (loginId != null) {
            Optional<Member> optionalMember = memberService.findById(loginId);
            if (!optionalMember.isEmpty()) {
                return optionalMember.get();
            }
        }

        return new Member();
    }

    private final CoronaApiService coronaApiService;
    private final MemberService memberService;

    @RequestMapping("/")
    public String welcome() {

        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model) {

        // Area Chart
        Map<String, Integer> areaChartMap = coronaApiService.getAreaChart();
        List<String> areaLabels = new ArrayList<>();
        List<Integer> areaValues = new ArrayList<>();
        for (String areaLabel : areaChartMap.keySet()) {
            areaLabels.add(areaLabel);
            areaValues.add(areaChartMap.get(areaLabel));
        }
        model.addAttribute("areaLabels", areaLabels);
        model.addAttribute("areaValues", areaValues);

        //Pie Chart
        List<Integer> pieChartValues = coronaApiService.getPieChart();
        model.addAttribute("pieChartValueList", pieChartValues);

        // Cards
        List<Float> cardsValueList = coronaApiService.getCardsValue();
        model.addAttribute("cardsValueList", cardsValueList);

        return "/home";
    }

    @GetMapping ("/charts")
    public String charts(Model model) {

        // Area Chart
        Map<String, Integer> areaChartMap = coronaApiService.getAreaChart();
        List<String> areaLabels = new ArrayList<>();
        List<Integer> areaValues = new ArrayList<>();
        for (String areaLabel : areaChartMap.keySet()) {
            areaLabels.add(areaLabel);
            areaValues.add(areaChartMap.get(areaLabel));
        }
        model.addAttribute("areaLabels", areaLabels);
        model.addAttribute("areaValues", areaValues);

        //Bar Chart
        Map<String, Integer> barChartValuesMap = coronaApiService.getBarChart();
        List<String> preBarLabels = new ArrayList<>();
        List<Integer> preBarValues = new ArrayList<>();
        List<String> barLabels = new ArrayList<>();
        List<Integer> barValues = new ArrayList<>();
        for (String barLabel : barChartValuesMap.keySet()) {
            preBarLabels.add(barLabel);
            preBarValues.add(barChartValuesMap.get(barLabel));
        }
        for (int i = 1; i < preBarLabels.size(); i++) {
            barLabels.add(preBarLabels.get(i));
            barValues.add(preBarValues.get(i) - preBarValues.get(i - 1));
        }
        model.addAttribute("barLabels", barLabels);
        model.addAttribute("barValues", barValues);

        //Pie Chart
        List<Integer> pieChartValues = coronaApiService.getPieChart();
        model.addAttribute("pieChartValueList", pieChartValues);

        return "/charts";
    }

    @GetMapping("/tables")
    public String tables(Model model) throws IOException {

        List<CoronaTableDto> coronaTableDtoList = getCoronaTableDtoList();
        model.addAttribute("rowList", coronaTableDtoList);

        return "/tables";
    }

    private List<CoronaTableDto> getCoronaTableDtoList() {
        List<CoronaData> coronaDataList = coronaApiService.findAll();
        List<CoronaTableDto> coronaTableDtoList = new ArrayList<>();
        for (CoronaData coronaData : coronaDataList) {
            CoronaTableDto coronaTableDto = new CoronaTableDto();
            coronaTableDto.setStatusDt(coronaData.getStatusDt());
            coronaTableDto.setAccDefRate(coronaData.getAccDefRate());
            coronaTableDto.setAccExamCnt(coronaData.getAccExamCnt());
            coronaTableDto.setAccExamCompCnt(coronaData.getAccExamCompCnt());
            coronaTableDto.setHPntCnt(coronaData.getHPntCnt());
            coronaTableDto.setGPntCnt(coronaData.getGPntCnt());
            coronaTableDto.setCareCnt(coronaData.getCareCnt());
            coronaTableDto.setResutlNegCnt(coronaData.getResutlNegCnt());
            coronaTableDtoList.add(coronaTableDto);
        }
        return coronaTableDtoList;
    }

   // @PostConstruct
    public void init() throws IOException {

        int count = coronaApiService.deleteAll();
        log.info("delete 영향을 받은 row count = {}", count);

        List<CoronaData> coronaDataList = coronaApiService.findCoronaApi(400);

        for (CoronaData coronaData : coronaDataList) {
            coronaApiService.save(coronaData);
        }
        log.info("초기화 완료");
    }
}
