package app.model.dto.joboffer;

import app.model.dto.jobapplication.JobApplicationDTO;
import app.model.entity.user.User;
import app.model.enums.EmploymentType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class JobOfferDTO {
    private UUID id;
    private String title;
    private String company;
    private String location;
    private String description;
    private BigDecimal salary;
    private EmploymentType type;
    private LocalDateTime createdOn;
    private LocalDate deadline;
    private User recruiter;
    private List<JobApplicationDTO> jobApplications;
}
