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
import app.utils.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
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

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Cannot create skill because userId={} was not found", userId);
                    return new UserNotFoundException(userId);
                });

        Skill skill = Skill.builder()
                .name(name)
                .level(level)
                .user(user)
                .acquiredAt(LocalDateTime.now())
                .build();

        skillRepository.save(skill);

        log.info("Skill id={} successfully created for userId={}", skill.getId(), userId);

        return Mapper.toSkillDTO(skill);
    }

    public List<SkillDTO> getSkillByUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Cannot retrieve skills because userId={} was not found", userId);
                    return new UserNotFoundException(userId);
                });

        List<SkillDTO> skills = skillRepository.findByUser(user)
                .stream()
                .map(Mapper::toSkillDTO)
                .toList();

        log.info("Retrieved {} skills for userId={}", skills.size(), userId);

        return skills;
    }

    public SkillDTO getSkillBySkillIdAndUserId(UUID skillId, UUID userId) {
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> {
                    log.warn("Skill id={} was not found", skillId);
                    return new SkillNotFoundException(skillId);
                });

        if (!skill.getUser().getId().equals(userId)) {
            log.warn("Unauthorized access attempt by userId={} for skillId={}", userId, skillId);
            throw new SkillAccessDeniedException(userId.toString());
        }

        log.info("Skill id={} successfully retrieved by userId={}", skillId, userId);

        return Mapper.toSkillDTO(skill);
    }


    public SkillDTO updateSkill(UUID skillId, UUID userId, SkillDTO skillDTO) {
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> {
                    log.warn("Cannot update skill because skillId={} was not found", skillId);
                    return new SkillNotFoundException(skillId);
                });

        if (!skill.getUser().getId().equals(userId)) {
            log.warn("Unauthorized update attempt by userId={} for skillId={}", userId, skillId);
            throw new SkillAccessDeniedException(userId.toString());
        }

        skill.setName(skillDTO.getName());
        skill.setLevel(skillDTO.getLevel());

        skillRepository.save(skill);

        log.info("Skill id={} successfully updated by userId={}", skillId, userId);

        return Mapper.toSkillDTO(skill);
    }

    public void deleteSkill(UUID skillId, UUID userId) {
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> {
                    log.warn("Cannot delete skill because skillId={} was not found", skillId);
                    return new SkillNotFoundException(skillId);
                });

        if (!skill.getUser().getId().equals(userId)) {
            log.warn("Unauthorized delete attempt by userId={} for skillId={}", userId, skillId);
            throw new SkillAccessDeniedException(userId.toString());
        }

        skillRepository.delete(skill);

        log.info("Skill id={} successfully deleted by userId={}", skillId, userId);
    }
}
