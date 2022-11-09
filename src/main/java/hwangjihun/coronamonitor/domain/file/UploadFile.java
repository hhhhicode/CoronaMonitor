package hwangjihun.coronamonitor.domain.file;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 고객이 업로드한 파일명으로 서버내부에 파일을 저장하면 안된다.
 * 파일명 충돌 위험.
 */
@Data
@RequiredArgsConstructor
public class UploadFile {

    /**
     * 고객이 업로드한 파일명
     */
    private final String uploadFileName;

    /**
     * 서버 내부에서 관리하는 파일명
     */
    private final String storeFileName;
}
