package com.jobfinder.interviewschedulingservice.service;

import com.jobfinder.interviewschedulingservice.model.dto.CreateInterviewRequest;
import com.jobfinder.interviewschedulingservice.model.dto.InterviewResponse;

import com.jobfinder.interviewschedulingservice.model.dto.UpdateInterviewRequest;
import com.jobfinder.interviewschedulingservice.model.entity.Interview;
import com.jobfinder.interviewschedulingservice.model.enums.InterviewStatus;
import com.jobfinder.interviewschedulingservice.repository.InterviewRepository;
import com.jobfinder.interviewschedulingservice.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.jobfinder.interviewschedulingservice.utils.Mapper.mapToInterviewResponse;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;


    public InterviewResponse createInterview(CreateInterviewRequest request) {

        Interview interview = Interview.builder()
                .jobApplicationId(request.getJobApplicationId())
                .candidateId(request.getCandidateId())
                .recruiterId(request.getRecruiterId())
                .interviewDate(request.getScheduledAt().toLocalDate())
                .interviewTime(request.getScheduledAt().toLocalTime())
                .meetingLink(request.getMeetingLink())
                .location(request.getLocation())
                .type(request.getType())
                .status(InterviewStatus.SCHEDULED)
                .createdOn(LocalDateTime.now())
                .build();

        Interview savedInterview = interviewRepository.save(interview);

        return mapToInterviewResponse(savedInterview);
    }

    public InterviewResponse getInterviewById(UUID id) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Interview with the searched ID was not found")
                );

        return mapToInterviewResponse(interview);
    }


    public List<InterviewResponse> getInterviewsByRecruiterId(UUID recruiterId) {

        return interviewRepository.findAllByRecruiterId(recruiterId)
                .stream()
                .map(Mapper::mapToInterviewResponse)
                .toList();
    }

    public List<InterviewResponse> getInterviewsByCandidateId(UUID candidateId) {

        return interviewRepository.findAllByCandidateId(candidateId)
                .stream()
                .map(Mapper::mapToInterviewResponse)
                .toList();
    }

    public InterviewResponse updateInterview(
            UUID id,
            UpdateInterviewRequest request) {

        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Interview with this ID was not found")
                );

        interview.setInterviewDate(request.getScheduledAt().toLocalDate());

        interview.setInterviewTime(request.getScheduledAt().toLocalTime());

        interview.setMeetingLink(request.getMeetingLink());

        interview.setLocation(request.getLocation());

        interview.setType(request.getType());

        interview.setUpdatedOn(LocalDateTime.now());

        Interview updatedInterview =
                interviewRepository.save(interview);

        return mapToInterviewResponse(updatedInterview);
    }

    public InterviewResponse confirmInterview(UUID id) {

        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Interview with this ID was not found")
                );

        interview.setStatus(InterviewStatus.CONFIRMED);
        interview.setUpdatedOn(LocalDateTime.now());

        Interview updatedInterview =
                interviewRepository.save(interview);

        return mapToInterviewResponse(updatedInterview);
    }

    public InterviewResponse declineInterview(UUID id) {

        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Interview with this ID was not found")
                );

        interview.setStatus(InterviewStatus.DECLINED);
        interview.setUpdatedOn(LocalDateTime.now());

        Interview updatedInterview =
                interviewRepository.save(interview);

        return mapToInterviewResponse(updatedInterview);
    }

    public InterviewResponse cancelInterview(UUID id) {

        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Interview with this ID was not found")
                );

        interview.setStatus(InterviewStatus.CANCELLED);
        interview.setUpdatedOn(LocalDateTime.now());

        Interview updatedInterview =
                interviewRepository.save(interview);

        return mapToInterviewResponse(updatedInterview);
    }
}
