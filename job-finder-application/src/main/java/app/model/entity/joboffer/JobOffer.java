package app.model.entity.joboffer;

import app.model.entity.jobapplication.JobApplication;
import app.model.entity.user.User;
import app.model.enums.EmploymentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "job_offers")
public class JobOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(nullable = false, length = 100)
    private String title;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(nullable = false)
    private String company;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(nullable = false)
    private String location;

    @NotNull
    @Size(min = 20, max = 2000)
    @Column(nullable = false, length = 2000)
    private String description;

    @NotNull
    @DecimalMin("620.00")
    @Column(nullable = false)
    private BigDecimal salary;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EmploymentType type;

    @NotNull
    private LocalDateTime createdOn;

    @NotNull
    private LocalDate deadline;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private User recruiter;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "jobOffer")
    @OrderBy("appliedAt DESC")
    private List<JobApplication> jobApplications = new ArrayList<>();
}
