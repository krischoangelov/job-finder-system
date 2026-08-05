package app.controller.user;

import app.model.dto.user.UserDTO;
import app.model.dto.user.UserUpdateProfileRequest;
import app.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
}
