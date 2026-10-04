package com.cth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CTH Enterprise Reporting System API")
                        .version("1.0.0")
                        .description("RESTful APIs for CTH Enterprise Reporting, User Maintenance, Dynamic Query Builder & Metrics")
                        .contact(new Contact().name("CTH Engineering Team").email("support@cth.com")));
    }
}
