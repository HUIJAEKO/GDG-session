package gdsc.practice.answer.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /**
     * common. code prefix: common-
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "common-1", "서버 에러가 발생했습니다"),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "common-2", "입력값이 올바르지 않습니다."),
    NOT_FOUND_RESOURCE_EXCEPTION(HttpStatus.NOT_FOUND, "common-3", "존재하지 않는 데이터입니다."),
    DUPLICATED_RESOURCE_EXCEPTION(HttpStatus.CONFLICT, "common-4", "이미 존재하는 데이터입니다."),
    PARSE_JSON_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "common-5", "JSON 파싱 에러가 발생했습니다."),
    NOT_FOUND_HANDLER_EXCEPTION(HttpStatus.NOT_FOUND, "common-8", "지원하지 않는 Api 요청 입니다."),
    NOT_FOUND_USER_EXCEPTION(HttpStatus.NOT_FOUND, "common-9", "존재하지 않는 사용자입니다."),
    INVALID_ACCESS_EXCEPTION(HttpStatus.FORBIDDEN, "common-10", "잘못된 접근입니다."),

    /**
     * auth. code prefix: auth-
     */
    UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, "auth-1", "인증되지 않은 사용자입니다."),

    /**
     * Question 관련 에러 코드. prefix: question-
     */
    NOT_EXIST_QUESTION_EXCEPTION(HttpStatus.NOT_FOUND, "question-1", "질문이 존재하지 않습니다."),
    QUESTION_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "question-2", "질문에 대한 권한이 없습니다."),
    INVALID_QUESTION_CONTENT(HttpStatus.BAD_REQUEST, "question-3", "질문 내용이 올바르지 않습니다."),
    INVALID_QUESTION_SUBJECT(HttpStatus.BAD_REQUEST, "question-4", "질문 제목이 올바르지 않습니다."),
    QUESTION_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "question-5", "이미 삭제된 질문입니다."),

    /**
     * Answer 관련 에러 코드. prefix: answer-
     */
    NOT_EXIST_ANSWER_EXCEPTION(HttpStatus.NOT_FOUND, "answer-1", "답변이 존재하지 않습니다."),
    ANSWER_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "answer-2", "답변에 대한 권한이 없습니다."),
    INVALID_ANSWER_CONTENT(HttpStatus.BAD_REQUEST, "answer-3", "답변 내용이 올바르지 않습니다."),
    ANSWER_CONTENT_TOO_LONG(HttpStatus.BAD_REQUEST, "answer-4", "답변 내용이 너무 깁니다."),
    ANSWER_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "answer-5", "이미 삭제된 답변입니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
