package hwangjihun.coronamonitor.repository;

import hwangjihun.coronamonitor.domain.CoronaData;
import hwangjihun.coronamonitor.domain.CoronaTableDto;
import lombok.extern.slf4j.Slf4j;
import org.json.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Repository
public class CoronaOpenApiRepository {

    private final EntityManager entityManager;

    public CoronaOpenApiRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<CoronaData> getApiData(int rangeNum) throws IOException {
        String serviceKey = "=53GQb6AbcGed0qwUzQ1VVXELv8756xxVPk%2BCH1Si74iVgWGT8a2uskfasxAl8B5HuHWGqrXQOl2NbPGMSA19gg%3D%3D";

        List<String> dateList = getDateList(rangeNum);

        List<CoronaData> coronaDataList = new ArrayList<>();

        for (int i = 0; i < dateList.size(); i++) {
            StringBuilder urlBuilder = getStringBuilder(serviceKey, dateList.get(i));
            HttpURLConnection conn = getHttpURLConnection(urlBuilder);
            BufferedReader rd = getBufferedReader(conn);
            StringBuilder sb = getStringBuilder(rd);

            rd.close();
            conn.disconnect();

            JSONObject jsonObject = XML.toJSONObject(sb.toString());
            CoronaData coronaData = getCoronaData(jsonObject);
            if (coronaData.getStatusDt().equals("")) continue;
            coronaDataList.add(coronaData);
        }

        return coronaDataList;
    }

    private List<String> getDateList(int rangeNum) {
        List<String> dateList = new ArrayList<>();


        for (int i = 1; i < rangeNum + 1; i++) {
            String editedDate = getEditedDate(i);
            dateList.add(editedDate);
        }

        return dateList;
    }

    // today - i == return
    public String getEditedDate(int i) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, i * -1);

        String editedDate = simpleDateFormat.format(calendar.getTime());
        return editedDate;
    }

    private static CoronaData getCoronaData(JSONObject jsonObject) {
        CoronaData coronaData = new CoronaData();

        JSONObject response = jsonObject.getJSONObject("response");
        JSONObject body = response.getJSONObject("body");
        if (!body.isEmpty()) {
            try {
                JSONObject items = body.getJSONObject("items");
                Object test = items.get("item");
                if (test instanceof JSONObject) {
                    JSONObject item = items.getJSONObject("item");
                    setCoronaData(coronaData, item);
                } else if (test instanceof JSONArray) {
                    JSONArray itemArray = items.getJSONArray("item");
                    JSONObject item = itemArray.getJSONObject(0);
                    setCoronaData(coronaData, item);
                }
            } catch (JSONException e) {
                return coronaData;
            }
        }
        return coronaData;
    }

    private static void setCoronaData(CoronaData coronaData, JSONObject item) {
        coronaData.setAccDefRate(item.getDouble("accDefRate"));
        coronaData.setDPntCnt(item.getInt("dPntCnt"));
        coronaData.setGPntCnt(item.getInt("gPntCnt"));
        coronaData.setHPntCnt(item.getInt("hPntCnt"));
        coronaData.setAccExamCnt(item.getInt("accExamCnt"));
        coronaData.setStatusTime(item.getString("statusTime"));
        coronaData.setUPntCnt(item.getInt("uPntCnt"));
        coronaData.setResutlNegCnt(item.getInt("resutlNegCnt"));
        coronaData.setStatusDt(item.get("statusDt").toString());
        coronaData.setAccExamCompCnt(item.getInt("accExamCompCnt"));
        coronaData.setCareCnt(item.getInt("careCnt"));
    }

    private static StringBuilder getStringBuilder(BufferedReader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        return sb;
    }

    private static BufferedReader getBufferedReader(HttpURLConnection conn) throws IOException {
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        return rd;
    }

    private static HttpURLConnection getHttpURLConnection(StringBuilder urlBuilder) throws IOException {
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-type", "application/json");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        return conn;
    }

    private static StringBuilder getStringBuilder(String serviceKey, String date) throws UnsupportedEncodingException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1352000/ODMS_COVID_02/callCovid02Api"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("500", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("apiType","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*결과형식(xml/json)*/
        urlBuilder.append("&" + URLEncoder.encode("status_dt","UTF-8") + "=" + URLEncoder.encode(date, "UTF-8")); /*기준일자*/
        return urlBuilder;
    }

    public CoronaData save(CoronaData coronaData) {

        entityManager.persist(coronaData);
        return coronaData;
    }

    public void update(String statusDt, CoronaTableDto coronaTableDto) {
        CoronaTableDto findCoronaData = entityManager.find(CoronaTableDto.class, statusDt);
        findCoronaData.setAccDefRate(coronaTableDto.getAccDefRate());
        findCoronaData.setAccExamCnt(coronaTableDto.getAccExamCnt());
        findCoronaData.setAccExamCompCnt(coronaTableDto.getAccExamCompCnt());
        findCoronaData.setResutlNegCnt(coronaTableDto.getResutlNegCnt());
        // JPA가 변화를 감지하고 자동으로 update 해준다.
    }

    public Optional<CoronaData> findById(String statusDt) {
        CoronaData findCoronaData = entityManager.find(CoronaData.class, statusDt);
        return Optional.ofNullable(findCoronaData);
    }

    public List<CoronaData> findAll() {
        String jpql = "select c from CoronaData c";

        TypedQuery<CoronaData> query = entityManager.createQuery(jpql, CoronaData.class);

        return query.getResultList();
    }

    public int deleteAll() {
        String jpql = "delete from CoronaData";

        return entityManager.createQuery(jpql)
                .executeUpdate();
    }

    public List<CoronaData> getRecentCoronaData() {

        String jpql = "select c from CoronaData c order by c.statusDt desc";

        TypedQuery<CoronaData> query = entityManager.createQuery(jpql, CoronaData.class);
        return query.setMaxResults(1).getResultList();
    }
}
