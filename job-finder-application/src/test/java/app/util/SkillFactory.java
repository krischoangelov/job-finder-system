package app.util;

import app.model.entity.user.User;
import app.model.enums.UserRole;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class SkillFactory {

    public static User createUser() {
        return createUser("skillUser", "skill@test.com");
    }

    public static User createUser(String username, String email) {
        return User.builder().username(username).firstName("Test").lastName("User").profilePicture("default.png").email(email).password("encodedPassword").role(UserRole.CANDIDATE).createdOn(LocalDateTime.now()).build();
    }
}
