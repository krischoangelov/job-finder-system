package app.model.dto.joboffer;

import app.model.entity.user.User;
import app.model.enums.EmploymentType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CreateJobOfferRequest {

    @NotNull(message = "Job title is required")
    @Size(min = 10, max = 100, message = "Job title must be between 10 and 100 characters")
    private String title;

    @NotNull(message = "Company is required")
    @Size(max = 100, message = "Company name cannot exceed 100 characters")
    private String company;

    @NotNull(message = "Location is required")
    @Size(max = 50, message = "Location cannot exceed 50 characters")
    private String location;

    @NotNull(message = "Description is required")
    @Size(min = 50, max = 2000, message = "Description must be between 50 and 2000 characters")
    private String description;

    @NotNull(message = "Salary is required")
    @DecimalMin(value = "620.00", message = "Salary must be greater than 620.00")
    private BigDecimal salary;

    @NotNull(message = "Employment type is required")
    private EmploymentType type;

    private LocalDateTime createdOn;

    @NotNull(message = "Deadline is required")
    @Future(message = "Deadline must be a future date")
    private LocalDate deadline;

    private User recruiter;
}
