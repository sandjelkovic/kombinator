package com.sandjelkovic.kombinator.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

/**
 * @author sandjelkovic
 * @date 7.11.17.
 */
@Configuration
@EnableSwagger2
class SwaggerConfig {
	@Bean
	fun api(): Docket = Docket(DocumentationType.SWAGGER_2)
		.select()
		.apis(RequestHandlerSelectors.basePackage("com.sandjelkovic"))
		.paths(PathSelectors.any())
		.build()
}
