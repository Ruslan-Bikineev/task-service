package org.taskservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "title: cannot be empty")
    @Size(min = 1, max = 255, message = "title: must be between 1 and 255 characters")
    private String title;

    @NotBlank(message = "description: cannot be empty")
    @Size(min = 1, max = 255, message = "description: must be between 1 and 255 characters")
    private String description;

    @JsonProperty("user_id")
    @Positive(message = "user_id: must be positive")
    private Long userId;
}