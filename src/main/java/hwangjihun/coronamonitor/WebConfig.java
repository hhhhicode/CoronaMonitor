package hwangjihun.coronamonitor;

import hwangjihun.coronamonitor.web.interceptor.LogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "*.ico", "/error", "*.svg", "*.css", "*.woff2", "*.js", "*.min.js", "*.bundle.min.js", "*.easing.min.js"
                        , "**/css/**", "**/img/**", "**/js/**", "**/scss/**", "**/vendor/**", "/members/vendor/**", "/members/js/**");
    }
}
