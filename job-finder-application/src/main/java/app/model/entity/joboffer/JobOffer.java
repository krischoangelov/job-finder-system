package app.model.entity.joboffer;

import app.model.entity.jobapplication.JobApplication;
import app.model.entity.user.User;
import app.model.enums.EmploymentType;
import jakarta.persistence.*;
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
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String company;
    @Column(nullable = false)
    private String location;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private BigDecimal salary;
    @Enumerated(EnumType.STRING)
    private EmploymentType type;
    private LocalDateTime createdOn;
    private LocalDate deadline;
    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private User recruiter;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "jobOffer")
    @OrderBy("appliedAt DESC")
    private List<JobApplication> jobApplications = new ArrayList<>();
}
