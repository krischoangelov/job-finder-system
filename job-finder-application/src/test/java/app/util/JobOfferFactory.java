package app.util;

import app.model.dto.joboffer.JobOfferDTO;
import app.model.dto.user.UserDTO;
import app.model.enums.EmploymentType;
import app.model.enums.UserRole;
import app.service.user.AuthenticationUserDetails;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class JobOfferFactory {

    public static AuthenticationUserDetails getCandidatePrincipal() {
        return AuthenticationUserDetails.builder()
                .id(UUID.randomUUID())
                .username("candidate")
                .password("password")
                .role(UserRole.CANDIDATE)
                .build();
    }

    public static AuthenticationUserDetails getRecruiterPrincipal() {
        return AuthenticationUserDetails.builder()
                .id(UUID.randomUUID())
                .username("recruiter")
                .password("password")
                .role(UserRole.RECRUITER)
                .build();
    }


    public static JobOfferDTO createJobOfferDTO() {
        return JobOfferDTO.builder()
                .id(UUID.randomUUID())
                .title("Java Developer")
                .company("Test Company")
                .location("Sofia")
                .description("Java developer position")
                .salary(BigDecimal.valueOf(5000))
                .type(EmploymentType.FULL_TIME)
                .deadline(LocalDate.now().plusDays(20))
                .jobApplications(List.of())
                .build();
    }
}
