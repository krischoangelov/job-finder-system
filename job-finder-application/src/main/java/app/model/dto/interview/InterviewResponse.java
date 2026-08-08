package app.model.dto.interview;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class InterviewResponse {

    private UUID id;

    private LocalDateTime scheduledAt;

    private String meetingLink;
    private String location;

    private InterviewType type;
    private InterviewStatus status;

    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
