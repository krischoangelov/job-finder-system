package app.model.dto.joboffer;

import app.model.entity.user.User;
import app.model.enums.EmploymentType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CreateJobOfferRequest {
    private String title;
    private String company;
    private String location;
    private String description;
    private BigDecimal salary;
    private EmploymentType type;
    private LocalDateTime createdOn;
    private LocalDate deadline;
    private User recruiter;
}
