package org.unilab.uniplan.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.http.HttpStatus;

@Configuration
@OpenAPIDefinition(info = @Info(title = "UniPlan - API", version = "1.0",
    description = "API for managing university schedules, resources, and academic programs, including students, lecturers, courses, disciplines, and rooms."
))
public class SwaggerConfig {

    @Bean
    public OpenApiCustomizer globalResponsesCustomizer() {
        return openApi -> openApi.getPaths().values().forEach(pathItem ->
              pathItem.readOperationsMap().forEach((httpMethod, operation) -> {
                  var responses = operation.getResponses();

                  // for all
                  responses.addApiResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), new ApiResponse().description(HttpStatus.BAD_REQUEST.getReasonPhrase()));
                  responses.addApiResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), new ApiResponse().description(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));

                  // custom for each method
                  switch (httpMethod) {
                      case POST -> responses.addApiResponse(String.valueOf(HttpStatus.CREATED.value()), new ApiResponse().description(HttpStatus.CREATED.getReasonPhrase()));
                      case PUT -> {
                          responses.addApiResponse(String.valueOf(HttpStatus.OK.value()), new ApiResponse().description(HttpStatus.OK.getReasonPhrase()));
                          responses.addApiResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), new ApiResponse().description(HttpStatus.NOT_FOUND.getReasonPhrase()));
                      }
                      case DELETE -> {
                          responses.addApiResponse(String.valueOf(HttpStatus.NO_CONTENT.value()), new ApiResponse().description(HttpStatus.NO_CONTENT.getReasonPhrase()));
                          responses.addApiResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), new ApiResponse().description(HttpStatus.NOT_FOUND.getReasonPhrase()));
                      }
                  }
              })
        );
    }
}
