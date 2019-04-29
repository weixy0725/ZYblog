package com.zhiyu.blog.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * swagger2配置类
 * 
 * @author xinyuan.wei
 * @date 2019年4月28日
 */
@Configuration
public class Swagger2Config {
	
	@Value("${swagger2.show}")
    private boolean swagger2Show;
	@Bean
	public Docket createRestApi() {
		
		ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();  
        tokenPar.name("Authorization").description("token").modelRef(new ModelRef("string")).parameterType("header")
        .defaultValue("") 
    	.required(false).build();
    	pars.add((Parameter) tokenPar.build());

		return new Docket(DocumentationType.SWAGGER_2)
				.enable(swagger2Show)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.zhiyu.blog.webapi"))
				.paths(PathSelectors.any())
				.build().globalOperationParameters(pars);
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("zy_blog api文档")
				.description("")
				.termsOfServiceUrl("")
				.version("1.0")
				.build();
	}

}
