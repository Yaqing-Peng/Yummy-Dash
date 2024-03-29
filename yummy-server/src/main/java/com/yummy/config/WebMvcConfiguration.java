package com.yummy.config;

import com.yummy.interceptor.JwtTokenAdminInterceptor;
import com.yummy.interceptor.JwtTokenUserInterceptor;
import com.yummy.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;
    @Autowired
    private JwtTokenUserInterceptor jwtTokenUserInterceptor;

    /**
     * register self-defined interceptor
     *
     * @param registry
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("Start registering self-defined interceptor...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");

        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/user/login")
                .excludePathPatterns("/user/shop/status");
    }

    /**
     * generate api doc by knife4j
     * @return
     */
    @Bean
    public Docket docket1() {
        log.info("start generating api doc...");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("Yummy Dash Proj Api Doc")
                .version("2.0")
                .description("Yummy Dash proj api doc")
                .build();

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("Admin APIs")
                .apiInfo(apiInfo)
                .select()
                //scan all methods and apis in controller
                .apis(RequestHandlerSelectors.basePackage("com.yummy.controller.admin"))
                .paths(PathSelectors.any())
                .build();

        return docket;
    }

    @Bean
    public Docket docket2() {
        log.info("start generating api doc...");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("Yummy Dash Proj Api Doc")
                .version("2.0")
                .description("Yummy Dash proj api doc")
                .build();

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("User APIs")
                .apiInfo(apiInfo)
                .select()
                //scan all methods and apis in controller
                .apis(RequestHandlerSelectors.basePackage("com.yummy.controller.user"))
                .paths(PathSelectors.any())
                .build();

        return docket;
    }

    /**
     * configure static resource mapping
     * @param registry
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("start configuring static resource mapping...");
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * extend spring mvc message converter
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("start extending spring mvc message converter");
        //new a message converter object
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //set object converter, converting java object to jason
        converter.setObjectMapper(new JacksonObjectMapper());
        //add my converter into spring converter and prioritize it
        converters.add(0, converter);


    }
}
