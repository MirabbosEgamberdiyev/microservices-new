package com.programmingtechie.productservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.SpringDocAnnotationsUtils;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class SwaggerConfig {


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                                .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization"))
//                                .type(SecurityScheme.Type.HTTP)
//                                .scheme("bearer")
//                                .bearerFormat("JWT")
//                                .in(SecurityScheme.In.HEADER)
//                                .name("Authorization"))
                )
                .info(new Info().title("FB-IABS-API for SQB").description(
                        "This is a sample Spring Boot RESTful service using springdoc-openapi and OpenAPI 3.").version("v1.0.0"))
                .openapi("3.0.2")

                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));
    }

    @Bean
    SpringDocConfigProperties springDocConfigProperties() {
        return new SpringDocConfigProperties();
    }

    @Bean
    public OpenApiCustomiser customerGlobalHeaderOpenApiCustomiser() {
        return openApi -> {
            Schema<?> schema = SpringDocAnnotationsUtils.extractSchema(
                    openApi.getComponents(),
                    ResultAsync.class,
                    null,
                    null,
                    null
            );
            MediaType mediaType = new MediaType().schema(schema);
            Content content = new Content().addMediaType(APPLICATION_JSON_VALUE, mediaType);

            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
                ApiResponses apiResponses = operation.getResponses();
                apiResponses.addApiResponse("400", new ApiResponse()
                        .description("Some of the required fields are not filled or invalid request body case.").content(content));
                apiResponses.addApiResponse("401", new ApiResponse()
                        .description("Authorization error case.").content(content));
                apiResponses.addApiResponse("403", new ApiResponse()
                        .description("Forbidden.").content(content));
                apiResponses.addApiResponse("404", new ApiResponse()
                        .description("Resource Not Found.").content(content));
                apiResponses.addApiResponse("500", new ApiResponse()
                        .description("Internal Error").content(content));
            }));
        };
    }

}
