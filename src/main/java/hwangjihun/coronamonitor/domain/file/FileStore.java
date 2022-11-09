package hwangjihun.coronamonitor.domain.file;

import hwangjihun.coronamonitor.domain.constvalue.file.FileConst;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 파일 저장 관련 로직 처리
 */
@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    /**
     * 파일을 서버 저장 장소에 저장한다.
     * @param multipartFile
     * @return UploadFile(본래파일이름, 서버에저장되는파일이름)
     * @throws IOException
     */
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return new UploadFile(originalFilename, storeFileName);
    }

    public boolean deleteFile(String storeFileName) {
        if (storeFileName.equals(FileConst.DEFAULT_PROFILE_NAME)) return false;
        File file = new File(getFullPath(storeFileName));

        if (file.exists()) {
            return file.delete();
        }

        return false;
    }

    /**
     * 서버에 저장하는 파일명을 생성한다.
     * @param originalFilename
     * @return ex) 23525i2bi52v2.jpg
     */
    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();

        return uuid + "." + ext;
    }

    /**
     * 확장자를 추출한다.
     * @param originalFilename
     * @return . 다음의 확장자 명
     */
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
