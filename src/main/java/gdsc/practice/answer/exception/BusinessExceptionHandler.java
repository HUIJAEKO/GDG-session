package gdsc.practice.answer.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Objects;

@Slf4j
@RestControllerAdvice(basePackages = {"gdsc.session.question","gdsc.session.answer"})
// 특정 패키지 예외 처리만 담당
public class BusinessExceptionHandler {

    // 1. 비즈니스 예외 처리
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> hasBusinessException(BusinessException e) {
        log.error("BusinessException: {}", e.getErrorCode().getMessage(), e);
        return ErrorResponse.createErrorResponseEntity(e.getErrorCode());
    }


    // 2. 유효성 검증 예외 처리
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        log.error("BindException: {}", e.getMessage(), e);
        FieldError fieldError = Objects.requireNonNull(e.getFieldError());
        return ErrorResponse.createErrorResponseEntity(
                ErrorCode.INVALID_INPUT_VALUE,
                String.format("%s (%s)",
                        fieldError.getDefaultMessage(),
                        fieldError.getField()));
    }


    // 3. 잘못된 요청 예외 처리
    @ExceptionHandler({
            // Spring MVC의 요청 파라미터 타입 변환 실패 예외 (URL 경로 변수 또는 요청 파라미터 타입 변환 실패 등)
            MethodArgumentTypeMismatchException.class,
            // Java의 기본 런타임 예외 (메소드에 전달된 인자 부적절, 비즈니스 로직 유효성 검사 실패 등)
            IllegalArgumentException.class,
            // Spring의 HTTP 메시지 변환 실패 예외 (JSON 파싱 또는 요청 본문의 객체 변환 실패 등)
            HttpMessageNotReadableException.class,
            // JSON 데이터를 Java 객체로 변환 시 타입 불일치
            InvalidFormatException.class,
            // Spring의 웹 요청 바인딩 관련 예외 (필수 요청 파라미터 누락, 헤더값 또는 쿠키 값 바인딩 실패 등)
            ServletRequestBindingException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestException(Exception e) {
        log.error("Exception : {}", e.getMessage(), e);
        return ErrorResponse.createErrorResponseEntity(ErrorCode.INVALID_INPUT_VALUE);
    }

    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(Exception e) {
        log.error("NotFoundException : {}", e.getMessage(), e);
        return ErrorResponse.createErrorResponseEntity(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.error("NoHandlerFoundException : {}", e.getMessage(), e);
        return ErrorResponse.createErrorResponseEntity(ErrorCode.NOT_FOUND_HANDLER_EXCEPTION);
    }


    // 4. 최상위 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Exception : {}", e.getMessage(), e);
        return ErrorResponse.createErrorResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
