package com.cogniflow.fraud.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CogniFlow Fraud Detection API")
                        .version("1.0.0")
                        .description("API para detección de fraudes en tiempo real usando Kafka y Spring Boot.")
                        .contact(new Contact()
                                .name("Emanuel Fabbro")
                                .email("fabbroemanuel7@gmail.com")));
    }
}
