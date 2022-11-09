package hwangjihun.coronamonitor.domain;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 프로필 수정을 위한 Dto 객체.
 * Member에 MultipartFile 필드로 넣는 방법을 알면 Member로 대체하자.
 */
@Data
public class uploadProfileDto {

    private MultipartFile profileImage;
    private Long id;
    private String userId;
    private String password;
    private String userName;
    private Integer age;
}
