package app.model.entity.user;

import app.model.entity.jobapplication.JobApplication;
import app.model.entity.joboffer.JobOffer;
import app.model.entity.skill.Skill;
import app.model.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NonNull
    @Size(min = 3, max = 30)
    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @NonNull
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String firstName;

    @NonNull
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String lastName;

    @NonNull
    @Column(nullable = false)
    private String profilePicture;

    @NonNull
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NonNull
    @Column(nullable = false)
    private String password;

    @NonNull
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @NonNull
    private LocalDateTime createdOn;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recruiter")
    @OrderBy("createdOn DESC")
    private List<JobOffer> createdJobs = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "candidate")
    @OrderBy("appliedAt DESC")
    private List<JobApplication> jobApplications = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @OrderBy("acquiredAt DESC")
    private List<Skill> skills = new ArrayList<>();
}
