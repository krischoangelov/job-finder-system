package app.model.dto.interview;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInterviewRequest {

    private LocalDateTime scheduledAt;

    private String meetingLink;

    private String location;

    private InterviewType type;
}
