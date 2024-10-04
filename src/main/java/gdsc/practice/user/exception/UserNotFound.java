package gdsc.practice.user.exception;

import org.springframework.http.HttpStatus;

public class UserNotFound extends UserException {
    private static final String MESSAGE = "사용자를 찾을 수 없습니다.";

    public UserNotFound() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}