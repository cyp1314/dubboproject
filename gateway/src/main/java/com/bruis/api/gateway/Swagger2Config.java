package com.bruis.api.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author LuoHaiYang
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    private static final String VERSION = "1.0";
    private static final String TITLE = "分布式电商网关服务接口文档";
    private static final String DESCRIPTION = "接口文档";
    private static final String BASEPACKAGE = "com.bruis.api.gateway.controller";
    private static final String SERVICE_URL = "http://localhost:8902";

    private static final String CLIENT_ID = "swagger";
    private static final String CLIENT_SECRET = "123456";
    private static final String GRANT_TYPE = "password";
    private static final String SCOPE = "test";


    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
//                .enable(true)
                .select()
                // 所有ApiOperation注解的方法，生成接口文档
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .apis(RequestHandlerSelectors.basePackage(BASEPACKAGE))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Collections.singletonList(securityScheme()))
                .securityContexts(Collections.singletonList(securityContext()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(TITLE)
                .description(DESCRIPTION)
                .termsOfServiceUrl(SERVICE_URL)
                .version(VERSION)
                .build();
    }

    /**
     * 设置安全策略
     * @return
     */
    private SecurityScheme securityScheme() {
        GrantType grantType = new ResourceOwnerPasswordCredentialsGrant("http://dubbo-swagger.cn");
        return new OAuthBuilder()
                .name("OAuth2")
                .grantTypes(Collections.singletonList(grantType))
                .scopes(Arrays.asList(scopes()))
                .build();
    }

    /**
     * 安全上下文
     * @return
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Collections.singletonList(new SecurityReference("OAuth2", scopes())))
                .forPaths(PathSelectors.any())
                .build();
    }

    private AuthorizationScope[] scopes() {
        return new AuthorizationScope[]{
                new AuthorizationScope("test", "")
        };
    }

}