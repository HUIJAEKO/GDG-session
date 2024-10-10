package gdsc.practice.user.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfo {
    private Long id;
    private String username;
    private String email;
    private String password;
}
