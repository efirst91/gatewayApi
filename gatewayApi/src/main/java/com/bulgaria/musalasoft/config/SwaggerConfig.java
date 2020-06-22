package com.bulgaria.musalasoft.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	/**
	 * 
	 * @return a swagger configuration bean
	 * 
	 */
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("Musala Soft - Gateways Service").apiInfo(apiInfo())
				.select().apis(RequestHandlerSelectors.basePackage("com.bulgaria.musalasoft"))
				.paths(PathSelectors.regex("/api/v1.*")).build()
				.tags(new Tag("Gateway Controller",
						"This Controller has a CRUD for Gateway and the peripheral devices associated it"),
						new Tag("Peripheral Device Controller", "This Controller has a CRUD for Peripheral Device"));
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Musala Soft Gateways Api Service")
				.description(" API for managing information about gateways and their associated devices.")
				.version("1.0-Beta")
				.contact(new Contact("Eduardo Hernández Anzardo", "http://www.musala.com/", "eduardo.ha1991@gmail.com")).build();
	}
}
