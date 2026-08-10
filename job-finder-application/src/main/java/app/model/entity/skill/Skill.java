package app.model.entity.skill;

import app.model.entity.user.User;
import app.model.enums.ProficiencyLevel;
import jakarta.persistence.*;
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
@Table(name = "skills")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NonNull
    @Size(min = 2, max = 50)
    @Column(nullable = false, length = 50)
    private String name;
    @NonNull
    @Enumerated(EnumType.STRING)
    private ProficiencyLevel level;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @NonNull
    private LocalDateTime acquiredAt;
}
