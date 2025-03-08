package dev.shorturl.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TagDTO {
    private Long id;
    
    @NotBlank(message = "Tag name cannot be empty")
    private String name;
    
    @NotBlank(message = "Tag description cannot be empty")
    private String description;
}