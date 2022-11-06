package hwangjihun.coronamonitor.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@RequiredArgsConstructor
public class ProfileDto {

    private final MultipartFile multipartFile;
    private final String password;
    private final String userName;
    private final Integer age;
}
