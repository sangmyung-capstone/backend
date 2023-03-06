package capstone.bapool.config.interceptor;

import capstone.bapool.user.UserDao;
import capstone.bapool.utils.JwtUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;


@Slf4j
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;

    private final UserDao userDao;

    @Autowired
    public AuthenticationInterceptor(JwtUtils jwtUtils, UserDao userDao) {
        this.jwtUtils = jwtUtils;
        this.userDao = userDao;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!jwtUtils.validateRequest(request)) {
            Gson gson = new Gson();
            String exception = gson.toJson(new ResponseEntity<>(UNAUTHORIZED));
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().print(exception);
            return false;
        }
        return true;
    }
}
