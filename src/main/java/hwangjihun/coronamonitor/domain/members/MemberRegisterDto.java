package hwangjihun.coronamonitor.domain.members;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class MemberRegisterDto {

    @NotBlank
    @Length(min = 4, max = 12)
    private String userId;
    @NotBlank
    private String password;
    @NotBlank
    private String userName;
    @Range(min = 0, max = 9999)
    private Integer age;
}
