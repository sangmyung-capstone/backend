package capstone.bapool.config.interceptor;

import capstone.bapool.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final AuthenticationInterceptor authenticationInterceptor;

    @Autowired
    public InterceptorConfig(AuthenticationInterceptor authenticationInterceptor) {
        this.authenticationInterceptor = authenticationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                // / 로 시작하는 모든 URL 에 적용을 하겠다.
                .addPathPatterns("/**")
                // 이 URL 로 시작하는 코드는 적용을 하지 않겠다.
                .excludePathPatterns("/auth/**", "/swagger-ui/**","/v2/api-docs","/swagger-resources/**", "/test/**");
    }

}
