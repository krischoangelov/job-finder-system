package app.controller.user;

import app.model.dto.user.UserDTO;
import app.model.dto.user.UserUpdateProfileRequest;
import app.model.enums.UserRole;
import app.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }



    @GetMapping("/profile")
    public ModelAndView getProfilePage(HttpSession httpSession) {

        UUID uuid = (UUID) httpSession.getAttribute("user_id");

        UserDTO user = userService.getById(uuid);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("profile");
        modelAndView.addObject("userDTO", user);

        return modelAndView;
    }


    @PostMapping("/profile")
    public ModelAndView updateProfileSettings(HttpSession httpSession, @ModelAttribute UserUpdateProfileRequest userUpdateProfileRequest) {

        UUID id = (UUID) httpSession.getAttribute("user_id");

        UserDTO user = userService.updateProfile(id, userUpdateProfileRequest);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/profile");
        modelAndView.addObject("userDTO", user);
        return modelAndView;
    }

    @GetMapping("/dashboard")
    public ModelAndView getDashboardPage(HttpSession httpSession) {

        UUID id = (UUID) httpSession.getAttribute("user_id");

        UserDTO user = userService.getById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("dashboard");
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @GetMapping("/candidates")
    public ModelAndView getCandidatesPage(HttpSession httpSession) {

        UUID userId = (UUID) httpSession.getAttribute("user_id");

        UserDTO currentUser = userService.getById(userId);

        if (!currentUser.getRole().equals(UserRole.RECRUITER)) {
            throw new RuntimeException("Only recruiters can view candidates");
        }

        List<UserDTO> candidates = userService.getAllCandidatesByRecruiter(userId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("candidates");
        modelAndView.addObject("candidates", candidates);

        return modelAndView;
    }
}
