package gdsc.practice.user.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfo {
    private String id;
    private String username;
    private String email;
    private String password;
}
