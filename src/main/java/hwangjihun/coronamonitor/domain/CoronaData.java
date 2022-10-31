package hwangjihun.coronamonitor.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * resultCode       결과코드
 * resultMsg        결과메세지
 * numOfRows        한 페이지 결과 수
 * pageNo           페이지 수
 * totalCount       데이터 총 개수
 * accDefRate       누적확진율
 * accExamCnt       누적검사수
 * accExamCompCnt   누적검사완료수
 * careCnt          치료중환자수
 * dPntCnt          격리해제수
 * gPntCnt          사망자수
 * hPntCnt          확진자수
 * resutlNegCnt     결과음성수
 * statusDt         기준일자
 * statusTime       기준시간
 * uPntCnt          검사중수
 */
@Data
@Entity
public class CoronaData {

    @Id
    private String statusDt = "";
    private String resultCode;
    private String resultMsg;
    private Integer numOfRows;
    private Integer pageNo;
    private Integer totalCount;
    private Double accDefRate;
    private Integer accExamCnt;
    private Integer accExamCompCnt;
    private Integer careCnt;
    private Integer dPntCnt;
    private Integer gPntCnt;
    private Integer hPntCnt;
    private Integer resutlNegCnt;
    private String statusTime;
    private Integer uPntCnt;
}
