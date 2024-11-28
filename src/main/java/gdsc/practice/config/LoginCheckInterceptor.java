package gdsc.practice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import gdsc.practice.user.dto.ErrorResponse;
import gdsc.practice.user.dto.UserInfo;
import gdsc.practice.utils.SessionConst;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

import static org.apache.logging.log4j.util.StringBuilders.equalsIgnoreCase;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(HttpMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())){
            return true;
        }
        HttpSession serverSession = request.getSession(false);

        // 서버에 세션이 없으면 세션이 만료된 상태로 간주 -> 에러 반환
        if (serverSession == null) {
            sendErrorResponse(response, SessionError.NO_SESSION.getMessage());
            return false;
        }

        // 클라이언트가 보낸 세션 ID 가져오기 (쿠키에서 가져옴)
        String clientSessionId = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("JSESSIONID".equals(cookie.getName())) {
                    clientSessionId = cookie.getValue();
                    break;
                }
            }
        }

        // 클라이언트가 보낸 세션 ID가 없는지? 서버에서 설정한 유저 객체가 있는지? 확인
        if (clientSessionId == null || serverSession.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            sendErrorResponse(response, SessionError.NO_SESSION.getMessage());
            return false;
        }

        // 서버 세션의 ID와 클라이언트 세션 ID 비교
        String serverSessionId = serverSession.getId();
        if (!clientSessionId.equals(serverSessionId)) {
            sendErrorResponse(response, SessionError.SESSION_NOT_MATCH.getMessage());
            return false;
        }

        // 질문 수정,삭제 기능 구현시에 질문 작성자와 요청 보낸 사용자가 같은지 판단하기 위해 컨트롤러로 유저 아이디를 넘겨줌.
//        UserInfo user = (UserInfo) serverSession.getAttribute(SessionConst.LOGIN_MEMBER);
//        Long id = (user.getId());
//        request.setAttribute(SessionConst.REQUEST_USER_ID,id);
        return true;
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message(message)
                .build();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(json);
    }

    @Getter
    private enum SessionError {
        NO_SESSION("세션이 존재하지 않습니다"),
        SESSION_NOT_MATCH("세션이 일치하지 않습니다");

        final String message;
        SessionError(String message) {
            this.message = message;
        }
    }
}