package hwangjihun.coronamonitor.controller;

import hwangjihun.coronamonitor.domain.constvalue.file.FileConst;
import hwangjihun.coronamonitor.domain.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.net.MalformedURLException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class FileController {

    private final FileStore fileStore;

    /**
     * UrlResource를 사용하여, 해당 경로에 있는 파일에 접근해서 스트림으로 반환한다.
     * View에서 URI 호출 -> Controller 접근 -> 파일 스트림 반환
     * @param filename
     * @return 파일의 스트림을 반환한다.
     * @throws MalformedURLException
     */
    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        File file = new File(fileStore.getFullPath(filename));
        if (file.exists()) {
            return new UrlResource("file:" + fileStore.getFullPath(filename));
        }

        return new UrlResource("file:" + fileStore.getFullPath(FileConst.DEFAULT_PROFILE_NAME));
    }
}
