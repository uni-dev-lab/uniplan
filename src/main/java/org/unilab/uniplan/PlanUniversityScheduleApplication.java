package org.unilab.uniplan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PlanUniversityScheduleApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlanUniversityScheduleApplication.class, args);
    }

}
