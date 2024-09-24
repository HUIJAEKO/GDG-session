package gdsc.practice.user.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String pwd;
    private String email;
    private String name;
    private Date birth;
    private String sns;
    private Date reg_date;
}
