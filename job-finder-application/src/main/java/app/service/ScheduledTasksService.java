package app.service;

import app.service.jobapplication.JobApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledTasksService {

    private final JobApplicationService jobApplicationService;

    @Scheduled(cron = "0 0 0 * * *")
    public void rejectApplicationsForExpiredJobOffers() {

        int rejectedApplications =
                jobApplicationService.rejectExpiredPendingApplications();

        log.info("Cron scheduled task completed. Rejected {} expired pending applications", rejectedApplications);
    }

    @Scheduled(fixedDelay = 600000)
    public void deleteOldWithdrawnApplications() {

        int deletedApplications =
                jobApplicationService.deleteOldWithdrawnApplications();

        log.info("Fixed-delay scheduled task completed. Deleted {} old withdrawn applications", deletedApplications);
    }

}
