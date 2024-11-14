package gdsc.practice.answer.exception;

import org.springframework.http.ResponseEntity;

public record ErrorResponse(String code, String message) {
    // 기본 에러 응답 생성
    public static ResponseEntity<ErrorResponse> createErrorResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.from(errorCode));
    }

    // 상세 메시지 포함 응답 생성
    public static ResponseEntity<ErrorResponse> createErrorResponseEntity(ErrorCode errorCode, String message) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.of(errorCode, message));
    }

    // ErrorCode의 기본 정보로 응답 생성
    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(
                errorCode.getCode(),
                errorCode.getMessage()
        );
    }

    // 상세 메시지 포함 ErrorResponse 생성
    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return new ErrorResponse(
                errorCode.getCode(),
                errorCode.getMessage() + "-detail message: " + message
        );
    }
}
