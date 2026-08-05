package app.model.entity.user;

import app.model.entity.jobapplication.JobApplication;
import app.model.entity.joboffer.JobOffer;
import app.model.entity.skill.Skill;
import app.model.enums.UserRole;
import jakarta.persistence.*;
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
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String profilePicture;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;
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
