package capstone.bapool.config.interceptor;

import capstone.bapool.user.UserRepository;
import capstone.bapool.utils.JwtUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

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
        if (!jwtUtils.validateRequest(request)) {
            Gson gson = new Gson();
            String exception = gson.toJson(new ResponseEntity<>(UNAUTHORIZED));
            makeResponse(response, exception);
            return false;
        }
        Long userId = jwtUtils.resolveRequest(request);
        request.setAttribute("userId", userId);
        log.info("userid={}", userId);
        return true;
    }

    private void makeResponse(HttpServletResponse response, String exception) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(exception);
    }
}
