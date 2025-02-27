package org.taskservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDto {
    private Long id;

    @NotBlank(message = "title: cannot be empty")
    @Size(min = 1, max = 255, message = "title: must be between 1 and 255 characters")
    private String title;

    @NotBlank(message = "description: cannot be empty")
    @Size(min = 1, max = 255, message = "description: must be between 1 and 255 characters")
    private String description;

    @NotBlank(message = "status: cannot be empty")
    @Size(min = 1, max = 255, message = "description: must be between 1 and 255 characters")
    private String status;

    @JsonProperty("user_id")
    @Positive(message = "user_id: must be positive")
    private Long userId;

    public String getDefaultStatusUpdateMessage() {
        return "Task with id: " + id + " has been updated status to: " + status + "\n";
    }
}