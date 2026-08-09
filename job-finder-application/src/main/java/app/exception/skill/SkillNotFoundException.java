package app.exception.skill;

import app.exception.ApplicationException;

import java.util.UUID;

public class SkillNotFoundException extends ApplicationException {
    public SkillNotFoundException(UUID skillId) {
        super("Skill with id" + skillId + " was not found.",
                "404",
                "Skill not found");
    }
}
