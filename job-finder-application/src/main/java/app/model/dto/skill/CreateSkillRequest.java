package app.model.dto.skill;

import app.model.enums.ProficiencyLevel;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateSkillRequest {

    private String name;
    private ProficiencyLevel level;
}
