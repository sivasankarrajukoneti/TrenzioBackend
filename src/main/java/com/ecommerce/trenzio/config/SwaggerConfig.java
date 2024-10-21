package com.ecommerce.trenzio.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI trenzioOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Trenzio E-Commerce API")
                        .description("API documentation for Trenzio E-Commerce system")
                        .version("1.0.0"));
    }
}
