package hwangjihun.coronamonitor.domain.members;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private String userId;
    private String password;
    @Column(name = "user_name")
    private String userName;
    private Integer age;
    @Column(name = "profile_image")
    private String profileImage;

    public Member() {}

    public Member(String userId, String password, String userName, Integer age) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.age = age;
    }
}
