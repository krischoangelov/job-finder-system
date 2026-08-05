package app.controller.skill;

import app.model.dto.skill.SkillDTO;
import app.model.dto.skill.CreateSkillRequest;
import app.service.skill.SkillService;
import app.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/skills")
public class SkillController {

    private final SkillService skillService;
    private final UserService userService;

    public SkillController(SkillService skillService, UserService userService) {
        this.skillService = skillService;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getSkillsPage(HttpSession httpSession) {
        UUID skillId = (UUID) httpSession.getAttribute("user_id");
        List<SkillDTO> skills = skillService.getSkillByUser(skillId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("skills");
        modelAndView.addObject("skills", skills);

        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView getCreateSkillPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("create-skill");
        modelAndView.addObject("skill", new CreateSkillRequest());
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView createSkill(
            @ModelAttribute CreateSkillRequest createSkillRequest,
            HttpSession session) {

        UUID userId = (UUID) session.getAttribute("user_id");

        skillService.createNewSkill(
                userId,
                createSkillRequest.getName(),
                createSkillRequest.getLevel()
        );

        return new ModelAndView("redirect:/skills");
    }

    @GetMapping("/{id}/edit")
    public ModelAndView getEditSkillPage(@PathVariable UUID id, HttpSession httpSession) {
        UUID userid = (UUID) httpSession.getAttribute("user_id");

        SkillDTO skill = skillService.getSkillBySkillIdAndUserId(id, userid);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("edit-skill");
        modelAndView.addObject("skillDTO", skill);
        modelAndView.addObject("skillId", id);
        return modelAndView;
    }

    @PostMapping("/{id}/edit")
    public ModelAndView editSkill(@PathVariable UUID id, @ModelAttribute SkillDTO skillDTO, HttpSession httpSession) {
        UUID userId = (UUID) httpSession.getAttribute("user_id");
        skillService.updateSkill(id, userId, skillDTO);
        return new ModelAndView("redirect:/skills");
    }

    @PostMapping("/{id}/delete")
    public ModelAndView deleteSkill(@PathVariable UUID id, HttpSession httpSession) {
        UUID userId = (UUID) httpSession.getAttribute("user_id");

        skillService.deleteSkill(id, userId);

        return new ModelAndView("redirect:/skills");
    }
}
