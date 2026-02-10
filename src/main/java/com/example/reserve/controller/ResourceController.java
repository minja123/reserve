package com.example.reserve.controller;

import com.example.reserve.dto.ApiResponse;
import com.example.reserve.dto.ResourceResponse;
import com.example.reserve.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @GetMapping
    public ApiResponse<List<ResourceResponse>> getAllResources() {
        List<ResourceResponse> allResources = resourceService.getAllResources();
        return ApiResponse.success(allResources);
    }
}
