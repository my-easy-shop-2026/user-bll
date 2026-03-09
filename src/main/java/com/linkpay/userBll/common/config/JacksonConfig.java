package com.linkpay.userBll.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.linkpay.commonModule.util.copyUtil.OffsetDateTimeDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.OffsetDateTime;

@Configuration
public class JacksonConfig {
  @Bean
  public ObjectMapper jsonObjectMapper() {

    var objectMapper = new ObjectMapper();
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    objectMapper.registerModule(new JavaTimeModule()
        .addDeserializer(OffsetDateTime.class, new OffsetDateTimeDeserializer())
    );

    return objectMapper;
  }
}
