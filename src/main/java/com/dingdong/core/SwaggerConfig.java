package com.dingdong.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@ComponentScan(basePackages = "com.dingdong")
@EnableWebMvc
@EnableSwagger2
@PropertySource("classpath:application-swagger.properties")
@Import(SwaggerUiConfiguration.class)
public class SwaggerConfig {

	@Bean
	ApiInfo apiInfo() {
		return new ApiInfo(
				"dingdong Front-end api docs",
				"dingdong Front-end api docs",
				"0.0.0",
				"http://182.92.174.224:8686/dingdong",
				"chenliang",
				"MIT",
				"http://opensource.org/licenses/MIT");
	}
	
	@Bean
	public Docket customImplemention() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(this.apiInfo());
	}
}
