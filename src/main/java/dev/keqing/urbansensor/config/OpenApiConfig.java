package dev.keqing.urbansensor.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private final GeneralConfig generalConfig = GeneralConfig.INSTANCE;


    @Bean
    public OpenAPI springShopOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("UrbanSensor")
                        .description("Prototipo")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url(""))
                        .contact(new Contact().name("keqing.dev"))
                )
                .addServersItem(new Server().url("http://localhost:8080"))
                .addServersItem(new Server().url(generalConfig.getUrl()))
                .components(new Components()
                        .addSecuritySchemes("bearer", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"))
                );

    }

}
