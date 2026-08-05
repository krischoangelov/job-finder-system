package app.model.entity.skill;

import app.model.entity.user.User;
import app.model.enums.ProficiencyLevel;
import jakarta.persistence.*;
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
    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    private ProficiencyLevel level;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private LocalDateTime acquiredAt;
}
