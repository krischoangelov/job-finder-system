package app.model.dto.skill;

import app.model.enums.ProficiencyLevel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateSkillRequest {

    @NotNull(message = "Skill name is required")
    @Size(min = 2, max = 50, message = "Skill name must be between 2 and 50 characters")
    private String name;

    @NotNull(message = "Proficiency level is required")
    private ProficiencyLevel level;
}
