package app.controller;

import app.model.dto.user.UserDTO;
import app.model.dto.user.UserLoginRequestDTO;
import app.model.dto.user.UserRegisterRequestDTO;
import app.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
public class IndexController {

    private UserService userService;

    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String getIndexPage() {
        return "index";
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage() {
        ModelAndView modelAndView = new ModelAndView();
        UserLoginRequestDTO userLoginRequest = UserLoginRequestDTO.builder().build();
        modelAndView.setViewName("login");
        modelAndView.addObject("userLoginRequest", userLoginRequest);
        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView login(@Valid UserLoginRequestDTO userLoginRequest, BindingResult bindingResult,
                              HttpSession httpSession) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("login");
        }

        UserDTO user = userService.login(userLoginRequest);
        httpSession.setAttribute("user_id", user.getId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("userLoginRequest", userLoginRequest);
        modelAndView.setViewName("redirect:/home");

        return modelAndView;
    }

    @GetMapping("/home")
    public ModelAndView getHomePage(HttpSession httpSession) {
        UUID uuid = (UUID) httpSession.getAttribute("user_id");
        UserDTO user = userService.getById(uuid);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage() {

        ModelAndView modelAndView = new ModelAndView();
        UserRegisterRequestDTO userRegisterRequest = UserRegisterRequestDTO.builder().build();
        modelAndView.setViewName("register");
        modelAndView.addObject("userRegisterRequest", userRegisterRequest);

        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView register(@Valid @ModelAttribute("userRegisterRequest") UserRegisterRequestDTO userRegisterRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("register");
        }

        userService.register(userRegisterRequest);

        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpSession httpSession) {
        httpSession.invalidate();
        return new ModelAndView("redirect:");
    }


}
