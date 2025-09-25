package org.unilab.uniplan.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.responses.ApiResponse;

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

                                                                      // Общи за всички
                                                                      responses.addApiResponse("400", new ApiResponse().description("Invalid input / Bad request"));
                                                                      responses.addApiResponse("500", new ApiResponse().description("Internal server error"));

                                                                      // По метод
                                                                      switch (httpMethod) {
                                                                          case POST -> responses.addApiResponse("201", new ApiResponse().description("Resource created"));
                                                                          case PUT -> {
                                                                              responses.addApiResponse("200", new ApiResponse().description("Resource updated"));
                                                                              responses.addApiResponse("404", new ApiResponse().description("Resource not found"));
                                                                          }
                                                                          case DELETE -> {
                                                                              responses.addApiResponse("204", new ApiResponse().description("Resource deleted"));
                                                                              responses.addApiResponse("404", new ApiResponse().description("Resource not found"));
                                                                          }
                                                                      }
                                                                  })
        );
    }
}
