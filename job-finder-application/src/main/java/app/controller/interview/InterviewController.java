package app.controller.interview;

import app.model.dto.interview.InterviewResponse;
import app.service.interview.InterviewService;
import app.service.user.AuthenticationUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/interviews")
public class InterviewController {

   private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @GetMapping
    public ModelAndView getNotificationsPage(@AuthenticationPrincipal
                                             AuthenticationUserDetails principal) {

        List<InterviewResponse> interviewResponseList =
                interviewService.getInterviewsByRecruiter(principal.getId());

        ModelAndView modelAndView = new ModelAndView("interviews");
        modelAndView.addObject("interviews", interviewResponseList);

        return modelAndView;
    }
}
