package com.example.reserve.dto;

import com.example.reserve.entity.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResourceResponse {
    private Long id;
    private String name;

    public static ResourceResponse from(Resource resource) {
        return new ResourceResponse(
                resource.getId(),
                resource.getName()
        );
    }
}
