package app.model.entity.jobapplication;

import app.model.entity.joboffer.JobOffer;
import app.model.entity.user.User;
import app.model.enums.ApplicationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "applications")
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Size(min = 50, max = 1000)
    @Column(nullable = false, length = 1000)
    private String motivationLetter;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @NotNull
    private LocalDateTime appliedAt;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private User candidate;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "job_offer_id", nullable = false)
    private JobOffer jobOffer;
}
