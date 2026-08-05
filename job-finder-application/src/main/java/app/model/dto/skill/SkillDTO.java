package app.model.dto.skill;

import app.model.entity.user.User;
import app.model.enums.ProficiencyLevel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class SkillDTO {
    private UUID id;
    private String name;
    private ProficiencyLevel level;
    private User user;
    private LocalDateTime acquiredAt;
}
