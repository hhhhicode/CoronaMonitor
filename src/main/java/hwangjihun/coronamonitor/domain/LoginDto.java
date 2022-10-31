package hwangjihun.coronamonitor.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDto {

    @NotBlank
    @Length(min = 4, max = 12)
    private String loginId;

    @NotBlank
    private String password;
}
