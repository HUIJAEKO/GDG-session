package gdsc.practice.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class LoginRequest {

    @NotEmpty(message = "이메일은 필수항목니다.")
    @Email(message = "올바른 이메일 주소를 입력해주세요.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수항목니다.")
    private String password;
}
