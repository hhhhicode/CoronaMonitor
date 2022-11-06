package hwangjihun.coronamonitor.domain.file;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UploadFile {

    private final String uploadFileName;
    private final String storeFileName;
}
