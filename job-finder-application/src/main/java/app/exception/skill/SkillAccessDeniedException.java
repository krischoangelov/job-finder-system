package app.exception.skill;

import app.exception.ApplicationException;

public class SkillAccessDeniedException extends ApplicationException {
    public SkillAccessDeniedException(String id) {
        super("User with id: " + id + "has no authorization to configure this skill",
                "403",
                "You are not allowed to configure this skill");
    }
}
