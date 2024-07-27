package com.dk.tax_rates.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.problem.jackson.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
@Configuration
public class AppConfiguration {

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer problemObjectMapperModules() {
    return jacksonObjectMapperBuilder ->
        jacksonObjectMapperBuilder.modules(
            new ProblemModule(), new ConstraintViolationProblemModule(), new JavaTimeModule());
  }
}
