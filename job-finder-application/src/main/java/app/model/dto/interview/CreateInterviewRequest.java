package app.model.dto.interview;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateInterviewRequest {

    private UUID jobApplicationId;

    private UUID candidateId;

    private UUID recruiterId;

    private LocalDateTime scheduledAt;

    private String meetingLink;

    private String location;

    private InterviewType type;
}
