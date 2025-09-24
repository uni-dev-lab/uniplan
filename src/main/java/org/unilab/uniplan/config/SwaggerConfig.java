package org.unilab.uniplan.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "UniPlan - API", version = "1.0",
    description = "API for managing university schedules, resources, and academic programs, including students, lecturers, courses, disciplines, and rooms."
))
public class SwaggerConfig {

}
