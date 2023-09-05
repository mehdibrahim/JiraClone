package com.proxym.jira.jira.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Content;
import com.proxym.jira.jira.entity.PStatus;
import com.proxym.jira.jira.repository.PStatusRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/status")
public class PStatusController {

    @Autowired
    PStatusRepository pStatusRepository;
    @Operation(summary = "Get List Of Status",description = "Get List Of Status",tags ="Project Status")
    @ApiResponse( responseCode = "200",description="List Of Status",content = {@Content(mediaType ="application/json")})
    @GetMapping
    List<PStatus> getStatus() {
        return pStatusRepository.findAll();
    }
    @Operation(summary = "Add New Status",description = "Add New Status",tags ="Project Status")
    @ApiResponse( responseCode = "200",description="New Status Added",content = {@Content(mediaType ="application/json")})
    @PostMapping
    PStatus createStatus(@RequestBody PStatus status) {
        return pStatusRepository.save(status);
    }
}
