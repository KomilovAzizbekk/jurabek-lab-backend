package uz.mediasolutions.jurabeklabbackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Open API for Jurabek Lab Backend")
                        .version("1.0")
                        .description("Jurabek Lab API Description"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .addServersItem(new io.swagger.v3.oas.models.servers.Server()
                        .url("https://jurabek-lab.medias.uz/api"));
    }


    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("user-side")
                .packagesToScan("uz.mediasolutions.jurabeklabbackend.controller.user")
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("admin-side")
                .packagesToScan("uz.mediasolutions.jurabeklabbackend.controller.admin")
                .build();
    }

    @Bean
    public GroupedOpenApi commonApi() {
        return GroupedOpenApi.builder()
                .group("common")
                .packagesToScan("uz.mediasolutions.jurabeklabbackend.controller.common")
                .build();
    }
}
