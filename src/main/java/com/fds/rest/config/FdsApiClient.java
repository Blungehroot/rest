package com.fds.rest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fds.rest.model.dto.CompaniesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class FdsApiClient {
    @Autowired
    private FdsProperties fdsProperties;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    public List<CompaniesDto> getTopHighestCompanies() {
        return Arrays.stream(restTemplate()
                        .getForEntity(fdsProperties.getUrls().getBaseUrl() + fdsProperties.getUrls().getTopHighUrl(), Object[].class)
                        .getBody())
                .map(o -> objectMapper().convertValue(o, CompaniesDto.class))
                .collect(Collectors.toList());
    }

    public List<CompaniesDto> getGreatestChangeCompanies() {return Arrays.stream(restTemplate()
                    .getForEntity(fdsProperties.getUrls().getBaseUrl() + fdsProperties.getUrls().getTopGreatChange(), Object[].class)
                    .getBody())
            .map(o -> objectMapper().convertValue(o, CompaniesDto.class))
            .collect(Collectors.toList());}
}
