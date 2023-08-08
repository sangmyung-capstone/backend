package capstone.bapool.config.interceptor;

import capstone.bapool.config.error.ErrorResponse;
import capstone.bapool.config.error.StatusEnum;
import capstone.bapool.user.UserRepository;
import capstone.bapool.utils.JwtUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;


@Slf4j
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;

    private final UserRepository userRepository;

    @Autowired
    public AuthenticationInterceptor(JwtUtils jwtUtils, UserRepository userRepository) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //요청의 userid가 있는지 확인, 토큰이 있는지 확인
        if (!jwtUtils.validateRequest(request)) {
            Gson gson = new Gson();
            String exception = gson.toJson(ResponseEntity.badRequest()
                    .body(ErrorResponse.builder()
                    .code(StatusEnum.EXPIRATION_TOKEN.getCode())
                    .message(StatusEnum.EXPIRATION_TOKEN.getMessage())
                    .build()));
            makeResponse(response, exception);
            return false;
        }
        //요청의 userid표시
        Long userId = jwtUtils.resolveRequest(request);
        request.setAttribute("userId", userId);
        log.info("토큰으로 나온 userid={}", userId);

        //요청에 담긴 PathVariable중 userid가져오기
        Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long requestUserId = Long.parseLong((String)pathVariables.get("user-id"));
        log.info("url의 사용자id={}",requestUserId);
        if(userId!=requestUserId){
            Gson gson = new Gson();
            log.info("토큰의 id와 url의 id가 일치하지 않습니다.");
            String exception = gson.toJson(ResponseEntity.badRequest()
                    .body(ErrorResponse.builder()
                            .code(StatusEnum.INVALID_TOKEN.getCode())
                            .message(StatusEnum.INVALID_TOKEN.getMessage())
                            .build()));
            makeResponse(response, exception);
            return false;
        }


        return true;
    }

    private void makeResponse(HttpServletResponse response, String exception) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(exception);
    }
}
