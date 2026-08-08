package app.controller.user;

import app.model.dto.user.UserDTO;
import app.model.dto.user.UserUpdateProfileRequest;
import app.model.enums.UserRole;
import app.service.user.AuthenticationUserDetails;
import app.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ModelAndView getProfilePage(@AuthenticationPrincipal AuthenticationUserDetails principal) {

        UserDTO user = userService.getById(principal.getId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("profile");
        modelAndView.addObject("userDTO", user);

        return modelAndView;
    }


    @PostMapping("/profile")
    public ModelAndView updateProfileSettings(@AuthenticationPrincipal AuthenticationUserDetails principal,
                                              @ModelAttribute UserUpdateProfileRequest userUpdateProfileRequest) {

        UserDTO user = userService.updateProfile(principal.getId(), userUpdateProfileRequest);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/profile");
        modelAndView.addObject("userDTO", user);
        return modelAndView;
    }

    @GetMapping("/dashboard")
    public ModelAndView getDashboardPage(@AuthenticationPrincipal AuthenticationUserDetails principal) {

        UserDTO user = userService.getById(principal.getId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("dashboard");
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @GetMapping("/candidates")
    public ModelAndView getCandidatesPage(@AuthenticationPrincipal AuthenticationUserDetails principal) {

        UserDTO currentUser = userService.getById(principal.getId());

        if (!currentUser.getRole().equals(UserRole.RECRUITER)) {
            throw new RuntimeException("Only recruiters can view candidates");
        }

        List<UserDTO> candidates = userService.getAllCandidatesByRecruiter(principal.getId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("candidates");
        modelAndView.addObject("candidates", candidates);

        return modelAndView;
    }
}
