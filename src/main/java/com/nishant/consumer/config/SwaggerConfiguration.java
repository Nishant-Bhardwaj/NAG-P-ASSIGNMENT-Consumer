package com.nishant.consumer.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
		info = @Info(
				description = "Consumer Application",
				version = "v1",
				title = "Consumer Application"
				)
		)
@Configuration
public class SwaggerConfiguration {
	

}
