package org.september.colorful.qrcode.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WeighWebMvcConfigurerAdapter implements WebMvcConfigurer, BeanPostProcessor {

	@Autowired
	private MvInterceptor mvInterceptor;

	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //配置静态资源处理
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/resources/", "classpath:/static/",
                "classpath:/public/");
    }

	@Override
    public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(mvInterceptor);
    }
}
