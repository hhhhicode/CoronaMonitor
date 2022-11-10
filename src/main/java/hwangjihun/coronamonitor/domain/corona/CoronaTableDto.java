package hwangjihun.coronamonitor.domain.corona;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * statusDt         기준일자
 * accDefRate       누적확진율
 * accExamCnt       누적검사수
 * accExamCompCnt   누적검사완료수
 * careCnt          치료중환자수
 * gPntCnt          사망자수
 * hPntCnt          확진자수
 * resutlNegCnt     결과음성수
 */
@Getter @Setter
public class CoronaTableDto {

    private String statusDt;
    private Double accDefRate;
    private Integer accExamCnt;
    private Integer accExamCompCnt;
    private Integer careCnt;
    private Integer gPntCnt;
    private Integer hPntCnt;
    private Integer resutlNegCnt;
}
