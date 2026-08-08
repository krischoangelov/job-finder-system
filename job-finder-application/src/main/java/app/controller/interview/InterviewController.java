package app.controller.interview;

import app.model.dto.interview.InterviewResponse;
import app.service.interview.InterviewService;
import app.service.user.AuthenticationUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/interviews")
public class InterviewController {

   private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @GetMapping
    public ModelAndView getRecruiterInterviews(@AuthenticationPrincipal
                                             AuthenticationUserDetails principal) {

        List<InterviewResponse> interviewResponseList =
                interviewService.getInterviewsByRecruiterId(principal.getId());

        ModelAndView modelAndView = new ModelAndView("interviews");
        modelAndView.addObject("interviews", interviewResponseList);

        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getInterview(@PathVariable UUID id) {

        InterviewResponse interview =
                interviewService.getInterviewById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("details-interview");
        modelAndView.addObject("interview", interview);

        return modelAndView;
    }
}
