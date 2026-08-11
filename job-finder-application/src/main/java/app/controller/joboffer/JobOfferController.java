package app.controller.joboffer;

import app.model.dto.joboffer.CreateJobOfferRequest;
import app.model.dto.joboffer.JobOfferDTO;
import app.model.dto.user.UserDTO;
import app.model.enums.UserRole;
import app.service.joboffer.JobOfferService;
import app.service.user.AuthenticationUserDetails;
import app.service.user.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/jobs")
public class JobOfferController {

    private JobOfferService jobOfferService;
    private UserService userService;

    public JobOfferController(JobOfferService jobOfferService, UserService userService) {
        this.jobOfferService = jobOfferService;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getJobOffersPage(@AuthenticationPrincipal AuthenticationUserDetails principal) {
        UserDTO user = userService.getById(principal.getId());
        List<JobOfferDTO> jobs;
        if (user.getRole().equals(UserRole.RECRUITER)) {
            jobs = jobOfferService.getAllPostedJobOffersByRecruiter(principal.getId());
        } else {
            jobs = jobOfferService.getAllJobOffers();
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("jobs");
        modelAndView.addObject("jobs", jobs);
        modelAndView.addObject("isRecruiter", user.getRole().equals(UserRole.RECRUITER));
        modelAndView.addObject("isCandidate", user.getRole().equals(UserRole.CANDIDATE));

        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getJobDetailsPage(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationUserDetails principal) {

        UserDTO user = userService.getById(principal.getId());
        JobOfferDTO job = jobOfferService.getJobOfferById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("details-job");
        modelAndView.addObject("job", job);
        modelAndView.addObject("isRecruiter", user.getRole().equals(UserRole.RECRUITER));
        modelAndView.addObject("isCandidate", user.getRole().equals(UserRole.CANDIDATE));
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView getCreateJobPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("create-job");
        modelAndView.addObject("jobOfferRequest", new CreateJobOfferRequest());

        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView createJobOffer(
            @ModelAttribute("jobOfferRequest") CreateJobOfferRequest createJobOfferRequest,
            @AuthenticationPrincipal AuthenticationUserDetails principal) {


        jobOfferService.createJobOffer(principal.getId(), createJobOfferRequest);

        return new ModelAndView("redirect:/jobs");
    }

    @GetMapping("/{id}/edit")
    public ModelAndView getEditJobOfferPage(@PathVariable UUID id) {

        JobOfferDTO jobOffer = jobOfferService.getJobOfferById(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("edit-job");
        modelAndView.addObject("jobOfferDTO", jobOffer);
        modelAndView.addObject("jobId", id);
        return modelAndView;
    }

    @PostMapping("/{id}/edit")
    public ModelAndView editJobOffer(@PathVariable UUID id, @ModelAttribute JobOfferDTO jobOfferDTO,
                                     @AuthenticationPrincipal AuthenticationUserDetails principal) {
        jobOfferService.updateJobOffer(id, principal.getId(), jobOfferDTO);
        return new ModelAndView("redirect:/jobs");
    }

    @PostMapping("/{id}/delete")
    public ModelAndView deleteJobOffer(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationUserDetails principal) {

        jobOfferService.deleteJobOffer(id, principal.getId());

        return new ModelAndView("redirect:/jobs");
    }
}
