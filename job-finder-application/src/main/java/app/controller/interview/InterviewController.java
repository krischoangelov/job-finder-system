package app.controller.interview;

import app.model.dto.interview.CreateInterviewRequest;
import app.model.dto.interview.InterviewResponse;
import app.model.dto.interview.UpdateInterviewRequest;
import app.model.dto.jobapplication.JobApplicationDTO;
import app.service.interview.InterviewService;
import app.service.jobapplication.JobApplicationService;
import app.service.user.AuthenticationUserDetails;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/interviews")
public class InterviewController {

    private final InterviewService interviewService;
    private final JobApplicationService jobApplicationService;

    public InterviewController(
            InterviewService interviewService,
            JobApplicationService jobApplicationService) {

        this.interviewService = interviewService;
        this.jobApplicationService = jobApplicationService;
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
    public ModelAndView getInterviewDetailsPage(@PathVariable UUID id) {

        InterviewResponse interview =
                interviewService.getInterviewById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("details-interview");
        modelAndView.addObject("interview", interview);

        return modelAndView;
    }


    @GetMapping("/create/{applicationId}")
    public ModelAndView getCreateInterviewPage(@PathVariable UUID applicationId) {

        ModelAndView modelAndView =
                new ModelAndView("create-interview");

        modelAndView.addObject("applicationId", applicationId);

        modelAndView.addObject("interviewRequest", new CreateInterviewRequest());

        return modelAndView;
    }


    @PostMapping("/create/{applicationId}")
    public ModelAndView createInterview(
            @PathVariable UUID applicationId,
            @Valid
            @ModelAttribute("interviewRequest")
            CreateInterviewRequest request,
            BindingResult bindingResult,
            @AuthenticationPrincipal AuthenticationUserDetails principal) {

        if (bindingResult.hasErrors()) {

            ModelAndView modelAndView = new ModelAndView("create-interview");

            modelAndView.addObject("applicationId", applicationId);

            return modelAndView;
        }

        request.setJobApplicationId(applicationId);
        request.setRecruiterId(principal.getId());

        JobApplicationDTO application = jobApplicationService.getApplicationById(applicationId, principal.getId());

        request.setCandidateId(application.getCandidate().getId());

        interviewService.createInterview(request);

        return new ModelAndView(
                "redirect:/interviews"
        );
    }


    @GetMapping("/{id}/edit")
    public ModelAndView getEditInterviewPage(
            @PathVariable UUID id) {

        InterviewResponse interview = interviewService.getInterviewById(id);

        UpdateInterviewRequest request = new UpdateInterviewRequest();

        request.setScheduledAt(interview.getScheduledAt());

        request.setMeetingLink(interview.getMeetingLink());

        request.setLocation(interview.getLocation());

        request.setType(interview.getType());

        ModelAndView modelAndView = new ModelAndView("edit-interview");

        modelAndView.addObject("interviewId", id);

        modelAndView.addObject("interviewRequest", request);

        return modelAndView;
    }


    @PostMapping("/{id}/edit")
    public ModelAndView updateInterview(
            @PathVariable UUID id,
            @Valid
            @ModelAttribute("interviewRequest")
            UpdateInterviewRequest request,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            ModelAndView modelAndView = new ModelAndView("edit-interview");

            modelAndView.addObject("interviewId", id);

            return modelAndView;
        }

        interviewService.updateInterview(id, request);

        return new ModelAndView("redirect:/interviews/" + id);
    }


    @PostMapping("/{id}/confirm")
    public ModelAndView confirmInterview(
            @PathVariable UUID id) {

        interviewService.confirmInterview(id);

        return new ModelAndView("redirect:/interviews/" + id);
    }


    @PostMapping("/{id}/decline")
    public ModelAndView declineInterview(
            @PathVariable UUID id) {

        interviewService.declineInterview(id);

        return new ModelAndView("redirect:/interviews/" + id);
    }


    @PostMapping("/{id}/cancel")
    public ModelAndView cancelInterview(
            @PathVariable UUID id) {

        interviewService.cancelInterview(id);

        return new ModelAndView("redirect:/interviews");
    }
}
