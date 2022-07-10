package com.fds.rest.controllers;

import com.fds.rest.config.FdsApiClient;
import com.fds.rest.model.dto.CompaniesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/companies/")
public class CompaniesController {
    @Autowired
    private FdsApiClient fdsApiClient;

    @GetMapping(value = "top-high-companies")
    @Secured("ROLE_USER")
    public ResponseEntity<List<CompaniesDto>> getTopHighCompanies() {

        return ResponseEntity.ok(fdsApiClient.getTopHighestCompanies());
    }


    @GetMapping(value = "top-greatest-change-companies")
    @Secured("ROLE_USER")
    public ResponseEntity<List<CompaniesDto>> getGreatestChangeCompanies() {
        return ResponseEntity.ok(fdsApiClient.getGreatestChangeCompanies());
    }
}
