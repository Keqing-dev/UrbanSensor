package dev.keqing.urbansensor.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Autowired
    private GeneralConfig generalConfig;

    @Bean
    public OpenAPI springShopOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("UrbanSensor")
                        .description("<div id='section/Common-Status-Codes' data-section-id='section/Common-Status-Codes' class='sc-eCApnc'>\n" +
                                "    <h1 class='chOOHy'><a class='sc-crzoAE DykGo' href='#section/Common-Status-Codes' aria-label='section/Common-Status-Codes'></a>Common Status Codes</h1>\n" +
                                "    <table>\n" +
                                "        <thead>\n" +
                                "        <tr>\n" +
                                "            <th>Status Code</th>\n" +
                                "            <th>Description</th>\n" +
                                "        </tr>\n" +
                                "        </thead>\n" +
                                "        <tbody>\n" +
                                "        <tr>\n" +
                                "            <td>400 Bad Request</td>\n" +
                                "            <td>Parámetros o formato inválido, campos faltantes</td>\n" +
                                "        </tr>\n" +
                                "        <tr>\n" +
                                "            <td>403 Forbidden</td>\n" +
                                "            <td>No autorizado</td>\n" +
                                "        </tr>\n" +
                                "        <tr>\n" +
                                "            <td>404 Not Found</td>\n" +
                                "            <td></td>\n" +
                                "        </tr>\n" +
                                "        <tr>\n" +
                                "            <td>500 Internal Server Error</td>\n" +
                                "            <td></td>\n" +
                                "        </tr>\n" +
                                "        </tbody>\n" +
                                "    </table>\n" +
                                "</div>")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url(""))
                        .contact(new Contact().name("keqing.dev"))
                )
                .addServersItem(new Server().url("http://localhost:8080"))
                .addServersItem(new Server().url(generalConfig.getDomainName()))
                .components(new Components()
                        .addSecuritySchemes("bearer", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"))
                );

    }

}
