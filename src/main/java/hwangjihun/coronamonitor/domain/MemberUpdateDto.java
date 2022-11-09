package hwangjihun.coronamonitor.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MemberUpdateDto {

    private final String password;
    private final String userName;
    private final Integer age;
    private final String profileImage;
}
