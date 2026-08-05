package app.controller.jobapplication;

import app.model.dto.jobapplication.CreateJobApplicationRequest;
import app.model.dto.jobapplication.JobApplicationDTO;
import app.model.dto.user.UserDTO;
import app.model.enums.UserRole;
import app.service.jobapplication.JobApplicationService;
import app.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/applications")
public class JobApplicationController {

    private final UserService userService;
    private final JobApplicationService jobApplicationService;

    public JobApplicationController(UserService userService, JobApplicationService jobApplicationService) {
        this.userService = userService;
        this.jobApplicationService = jobApplicationService;
    }

    @GetMapping
    public ModelAndView getApplicationsPage(HttpSession httpSession) {
        UUID userId = (UUID) httpSession.getAttribute("user_id");
        List<JobApplicationDTO> applications = jobApplicationService.getApplicationsForUser(userId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("applications");
        modelAndView.addObject("applications", applications);
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getApplicationDetailsPage(@PathVariable UUID id,
                                                  HttpSession session) {
        UUID userId = (UUID) session.getAttribute("user_id");

        UserDTO user = userService.getById(userId);
        JobApplicationDTO application = jobApplicationService.getApplicationById(id, userId);

        ModelAndView modelAndView = new ModelAndView("details-application");
        modelAndView.addObject("jobApplication", application);
        modelAndView.addObject("isRecruiter", user.getRole().equals(UserRole.RECRUITER));
        modelAndView.addObject("isCandidate", user.getRole().equals(UserRole.CANDIDATE));

        return modelAndView;
    }

    @GetMapping("/create/{jobId}")
    public ModelAndView getCreateApplicationPage(@PathVariable UUID jobId) {
        ModelAndView modelAndView = new ModelAndView("create-application");
        modelAndView.addObject("jobId", jobId);
        modelAndView.addObject("applicationRequest", new CreateJobApplicationRequest());

        return modelAndView;
    }

    @PostMapping("/create/{jobId}")
    public ModelAndView createApplication(@PathVariable UUID jobId,
                                          @Valid @ModelAttribute("applicationRequest") CreateJobApplicationRequest request,
                                          BindingResult bindingResult,
                                          HttpSession session) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("create-application");
            modelAndView.addObject("jobId", jobId);
            return modelAndView;
        }

        UUID candidateId = (UUID) session.getAttribute("user_id");

        jobApplicationService.createApplication(jobId, candidateId, request);

        return new ModelAndView("redirect:/applications");
    }

    @GetMapping("/{id}/edit")
    public ModelAndView getEditApplicationPage(@PathVariable UUID id,
                                               HttpSession session) {
        UUID userId = (UUID) session.getAttribute("user_id");

        JobApplicationDTO application = jobApplicationService.getApplicationById(id, userId);

        CreateJobApplicationRequest request = new CreateJobApplicationRequest();
        request.setMotivationLetter(application.getMotivationLetter());

        ModelAndView modelAndView = new ModelAndView("edit-application");
        modelAndView.addObject("applicationId", id);
        modelAndView.addObject("applicationRequest", request);

        return modelAndView;
    }

    @PostMapping("/{id}/edit")
    public ModelAndView editApplication(@PathVariable UUID id,
                                        @Valid @ModelAttribute("applicationRequest") CreateJobApplicationRequest request,
                                        BindingResult bindingResult,
                                        HttpSession session) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("edit-application");
            modelAndView.addObject("applicationId", id);
            return modelAndView;
        }

        UUID candidateId = (UUID) session.getAttribute("user_id");

        jobApplicationService.updateApplication(id, candidateId, request);

        return new ModelAndView("redirect:/applications/" + id);
    }

    @PostMapping("/{id}/withdraw")
    public ModelAndView withdrawApplication(@PathVariable UUID id,
                                            HttpSession session) {
        UUID candidateId = (UUID) session.getAttribute("user_id");

        jobApplicationService.withdrawApplication(id, candidateId);

        return new ModelAndView("redirect:/applications");
    }

    @PostMapping("/{id}/accept")
    public ModelAndView acceptApplication(@PathVariable UUID id,
                                          HttpSession session) {
        UUID recruiterId = (UUID) session.getAttribute("user_id");

        jobApplicationService.acceptApplication(id, recruiterId);

        return new ModelAndView("redirect:/applications/" + id);
    }

    @PostMapping("/{id}/reject")
    public ModelAndView rejectApplication(@PathVariable UUID id,
                                          HttpSession session) {
        UUID recruiterId = (UUID) session.getAttribute("user_id");

        jobApplicationService.rejectApplication(id, recruiterId);

        return new ModelAndView("redirect:/applications/" + id);
    }
}
