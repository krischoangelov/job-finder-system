package com.jobfinder.interviewschedulingservice.repository;

import com.jobfinder.interviewschedulingservice.model.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, UUID> {

    List<Interview> findAllByRecruiterId(UUID recruiterId);
}
