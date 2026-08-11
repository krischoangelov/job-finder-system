package app.controller.skill;

import app.model.dto.skill.SkillDTO;
import app.model.dto.skill.CreateSkillRequest;
import app.service.skill.SkillService;
import app.service.user.AuthenticationUserDetails;
import app.service.user.UserService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

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
    public ModelAndView getSkillsPage(@AuthenticationPrincipal AuthenticationUserDetails principal) {
        List<SkillDTO> skills = skillService.getSkillByUser(principal.getId());

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
            @AuthenticationPrincipal AuthenticationUserDetails principal) {

        skillService.createNewSkill(
                principal.getId(),
                createSkillRequest.getName(),
                createSkillRequest.getLevel()
        );

        return new ModelAndView("redirect:/skills");
    }

    @GetMapping("/{id}/edit")
    public ModelAndView getEditSkillPage(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationUserDetails principal) {

        SkillDTO skill = skillService.getSkillBySkillIdAndUserId(id, principal.getId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("edit-skill");
        modelAndView.addObject("skillDTO", skill);
        modelAndView.addObject("skillId", id);
        return modelAndView;
    }

    @PostMapping("/{id}/edit")
    public ModelAndView editSkill(@PathVariable UUID id, @ModelAttribute SkillDTO skillDTO,
                                  @AuthenticationPrincipal AuthenticationUserDetails principal) {
        skillService.updateSkill(id, principal.getId(), skillDTO);
        return new ModelAndView("redirect:/skills");
    }

    @PostMapping("/{id}/delete")
    public ModelAndView deleteSkill(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationUserDetails principal) {
        skillService.deleteSkill(id, principal.getId());

        return new ModelAndView("redirect:/skills");
    }
}
