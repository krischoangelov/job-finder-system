package app.model.dto.jobapplication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateJobApplicationRequest {

    @NotBlank(message = "Motivation letter is required")
    @Size(min = 50, max = 1000, message = "Motivation letter must be between 50 and 1000 characters")
    private String motivationLetter;
}
