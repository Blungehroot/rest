package com.fds.rest.model.dto;

import lombok.Data;

@Data
public class CompaniesDto {
        private String symbol;
        private Double latestPrice;
        private String companyName;
        private String volume;
        private Integer previousVolume;
}
