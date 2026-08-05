package app.service.skill;


import app.model.dto.skill.SkillDTO;
import app.model.entity.skill.Skill;
import app.model.entity.user.User;
import app.model.enums.ProficiencyLevel;
import app.repository.skill.SkillRepository;
import app.repository.user.UserRepository;
import app.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SkillService {

    private SkillRepository skillRepository;
    private UserRepository userRepository;

    @Autowired
    public SkillService(SkillRepository skillRepository, UserRepository userRepository) {
        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
    }

    public SkillDTO createNewSkill(UUID userId, String name, ProficiencyLevel level) {

        User user = userRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("User not found"));

        Skill skill = Skill.builder()
                .name(name)
                .level(level)
                .user(user)
                .acquiredAt(LocalDateTime.now())
                .build();

        skillRepository.save(skill);

        return Mapper.toSkillDTO(skill);
    }

    public List<SkillDTO> getSkillByUser(UUID uuid) {
        User user = userRepository.findById(uuid).orElse(null);

        if (user == null) {
            throw new RuntimeException("No such user was found");
        }

        return skillRepository.findByUser(user).stream().map(Mapper::toSkillDTO).toList();
    }

    public SkillDTO getSkillBySkillIdAndUserId(UUID skillId, UUID userId) {
        Skill skill = skillRepository.findById(skillId).
                orElseThrow(() ->  new RuntimeException("No such skill was found"));

        if (!skill.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to edit this skill");
        }

        return Mapper.toSkillDTO(skill);
    }


    public SkillDTO updateSkill(UUID skillId, UUID userId, SkillDTO skillDTO) {
        Skill skill = skillRepository.findById(skillId).
                orElseThrow(() ->  new RuntimeException("No such skill was found"));

        if (!skill.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to edit this skill");
        }

        skill.setName(skillDTO.getName());
        skill.setLevel(skillDTO.getLevel());

        skillRepository.save(skill);

        return Mapper.toSkillDTO(skill);
    }

    public void deleteSkill(UUID skillId, UUID userId) {
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        if (!skill.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to delete this skill");
        }

        skillRepository.delete(skill);
    }
}
