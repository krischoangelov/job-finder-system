package app.model.dto.interview;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInterviewRequest {

    @NotNull(message = "Interview date and time are required")
    private LocalDateTime scheduledAt;

    @Size(max = 200, message = "Meeting link cannot exceed 200 characters")
    private String meetingLink;

    @Size(max = 100, message = "Location cannot exceed 100 characters")
    private String location;

    @NotNull(message = "Interview type is required")
    private InterviewType type;
}
