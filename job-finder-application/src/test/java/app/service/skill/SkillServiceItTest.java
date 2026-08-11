package app.service.skill;

import app.exception.skill.SkillAccessDeniedException;
import app.exception.skill.SkillNotFoundException;
import app.exception.user.UserNotFoundException;
import app.model.dto.skill.SkillDTO;
import app.model.entity.skill.Skill;
import app.model.entity.user.User;
import app.model.enums.ProficiencyLevel;
import app.repository.skill.SkillRepository;
import app.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static app.util.SkillFactory.createUser;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SkillServiceItTest {

    @Autowired
    private SkillService skillService;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenCreateNewSkill_shouldPersistSkill() {

        User user = userRepository.save(createUser());
        SkillDTO result = skillService.createNewSkill(user.getId(), "Java", ProficiencyLevel.ADVANCED);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Java", result.getName());
        assertEquals(ProficiencyLevel.ADVANCED, result.getLevel());
        assertTrue(skillRepository.findById(result.getId()).isPresent());
    }

    @Test
    public void whenCreateNewSkillWithInvalidUser_shouldThrowUserNotFoundException() {

        assertThrows(UserNotFoundException.class,
                () -> skillService.createNewSkill(UUID.randomUUID(), "Java", ProficiencyLevel.ADVANCED));
    }

    @Test
    public void whenGetSkillByUser_shouldReturnPersistedSkills() {

        User user = userRepository.save(createUser());
        skillService.createNewSkill(user.getId(), "Java", ProficiencyLevel.ADVANCED);
        skillService.createNewSkill(user.getId(), "Spring", ProficiencyLevel.INTERMEDIATE);

        List<SkillDTO> skills = skillService.getSkillByUser(user.getId());

        assertEquals(2, skills.size());
    }

    @Test
    public void whenGetSkillBySkillIdAndUserId_shouldReturnSkill() {

        User user = userRepository.save(createUser());

        SkillDTO createdSkill = skillService.createNewSkill(user.getId(), "Java", ProficiencyLevel.ADVANCED);
        SkillDTO result = skillService.getSkillBySkillIdAndUserId(createdSkill.getId(), user.getId());

        assertEquals(createdSkill.getId(), result.getId());
        assertEquals("Java", result.getName());
    }

    @Test
    public void whenGetSkillByInvalidId_shouldThrowSkillNotFoundException() {

        User user = userRepository.save(createUser());

        assertThrows(SkillNotFoundException.class,
                () -> skillService.getSkillBySkillIdAndUserId(UUID.randomUUID(), user.getId()));
    }

    @Test
    public void whenDifferentUserRequestsSkill_shouldThrowSkillAccessDeniedException() {

        User owner = userRepository.save(createUser("owner", "owner@test.com"));
        User anotherUser = userRepository.save(createUser("anotherUser", "another@test.com"));

        SkillDTO createdSkill = skillService.createNewSkill(owner.getId(), "Java", ProficiencyLevel.ADVANCED);

        assertThrows(SkillAccessDeniedException.class,
                () -> skillService.getSkillBySkillIdAndUserId(createdSkill.getId(), anotherUser.getId()));
    }

    @Test
    public void whenUpdateSkill_shouldPersistUpdatedSkill() {

        User user = userRepository.save(createUser());

        SkillDTO createdSkill = skillService.createNewSkill(user.getId(), "Java", ProficiencyLevel.INTERMEDIATE);
        SkillDTO updateDTO = SkillDTO.builder().name("Spring Boot").level(ProficiencyLevel.ADVANCED).build();
        SkillDTO result = skillService.updateSkill(createdSkill.getId(), user.getId(), updateDTO);

        assertEquals("Spring Boot", result.getName());
        assertEquals(ProficiencyLevel.ADVANCED, result.getLevel());
        Skill persistedSkill = skillRepository.findById(createdSkill.getId()).orElseThrow();
        assertEquals("Spring Boot", persistedSkill.getName());
    }

    @Test
    public void whenUpdateSkillByDifferentUser_shouldThrowSkillAccessDeniedException() {

        User owner = userRepository.save(createUser("owner", "owner@test.com"));
        User anotherUser = userRepository.save(createUser("another", "another@test.com"));

        SkillDTO createdSkill = skillService.createNewSkill(owner.getId(), "Java", ProficiencyLevel.INTERMEDIATE);
        SkillDTO updateDTO = SkillDTO.builder().name("Spring").level(ProficiencyLevel.ADVANCED).build();

        assertThrows(SkillAccessDeniedException.class, () -> skillService.updateSkill(createdSkill.getId(), anotherUser.getId(), updateDTO));
    }

    @Test
    public void whenDeleteSkill_shouldRemoveSkillFromDatabase() {

        User user = userRepository.save(createUser());

        SkillDTO createdSkill = skillService.createNewSkill(user.getId(), "Java", ProficiencyLevel.ADVANCED);
        skillService.deleteSkill(createdSkill.getId(), user.getId());

        assertFalse(skillRepository.findById(createdSkill.getId()).isPresent());
    }

    @Test
    public void whenDeleteSkillByDifferentUser_shouldThrowSkillAccessDeniedException() {

        User owner = userRepository.save(createUser("owner", "owner@test.com"));
        User anotherUser = userRepository.save(createUser("another", "another@test.com"));

        SkillDTO createdSkill = skillService.createNewSkill(owner.getId(), "Java", ProficiencyLevel.ADVANCED);
        assertThrows(SkillAccessDeniedException.class, () -> skillService.deleteSkill(createdSkill.getId(), anotherUser.getId()));

        assertTrue(skillRepository.findById(createdSkill.getId()).isPresent());
    }


}