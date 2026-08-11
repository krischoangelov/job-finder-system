package app.service.user;

import app.exception.user.PasswordMismatchException;
import app.exception.user.RecruiterAccessRequiredException;
import app.exception.user.UserAlreadyExistsException;
import app.exception.user.UserNotFoundException;
import app.model.dto.jobapplication.JobApplicationDTO;
import app.model.dto.user.UserDTO;
import app.model.dto.user.UserRegisterRequestDTO;
import app.model.dto.user.UserUpdateProfileRequest;
import app.model.entity.jobapplication.JobApplication;
import app.model.entity.user.User;
import app.model.enums.UserRole;
import app.repository.user.UserRepository;
import app.utils.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public UserDTO register(UserRegisterRequestDTO userRegisterRequest) {

        Optional<User> user = userRepository.findByUsername(userRegisterRequest.getUsername());
        if (user.isPresent()) {
            log.warn("Registration failed because username={} already exists", userRegisterRequest.getUsername());
            throw new UserAlreadyExistsException(userRegisterRequest.getUsername());
        }

        if (!userRegisterRequest.getPassword().equals(userRegisterRequest.getConfirmPassword())) {
            log.warn("Registration failed because passwords do not match for username={}", userRegisterRequest.getUsername());
            throw new PasswordMismatchException();
        }

        String encodedPassword = passwordEncoder.encode(userRegisterRequest.getPassword());
        userRegisterRequest.setPassword(encodedPassword);

        User entityUser = Mapper.toUserEntity(userRegisterRequest);

        userRepository.save(entityUser);

        log.info("User registered successfully with id={} and username={}", entityUser.getId(), entityUser.getUsername());

        return Mapper.toUserDTO(entityUser);
    }

    public UserDTO getById(UUID uuid) {
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> {
                    log.warn("User with id={} was not found", uuid);
                    return new UserNotFoundException(uuid);
                });

        log.info("User with id={} retrieved successfully", uuid);

        return Mapper.toUserDTO(user);
    }


    public UserDTO updateProfile(UUID id, UserUpdateProfileRequest userUpdateProfileRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cannot update profile because userId={} was not found", id);
                    return new UserNotFoundException(id);
                });

        user.setUsername(userUpdateProfileRequest.getUsername());
        user.setEmail(userUpdateProfileRequest.getEmail());
        user.setFirstName(userUpdateProfileRequest.getFirstName());
        user.setLastName(userUpdateProfileRequest.getLastName());

        log.info("Profile successfully updated for userId={}", id);

        return Mapper.toUserDTO(userRepository.save(user));
    }

    public List<UserDTO> getAllCandidatesByRecruiter(UUID recruiterId) {
        User recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() -> {
                    log.warn("Cannot retrieve candidates because recruiterId={} was not found", recruiterId);
                    return new UserNotFoundException(recruiterId);
                });

        if (!recruiter.getRole().equals(UserRole.RECRUITER)) {
            log.warn("UserId={} attempted to view candidates without RECRUITER role", recruiterId);
            throw new RecruiterAccessRequiredException(recruiterId.toString());
        }

        List<UserDTO> candidates = recruiter.getCreatedJobs()
                .stream()
                .flatMap(jobOffer -> jobOffer.getJobApplications().stream())
                .map(JobApplication::getCandidate)
                .map(candidate -> mapCandidateForRecruiter(candidate, recruiterId))
                .toList();

        log.info("Retrieved {} candidates for recruiterId={}", candidates.size(), recruiterId);

        return candidates;
    }

    private UserDTO mapCandidateForRecruiter(User candidate, UUID recruiterId) {

        UserDTO candidateDTO = Mapper.toUserDTO(candidate);

        List<JobApplicationDTO> recruiterApplications = candidate.getJobApplications()
                .stream()
                .filter(application ->
                        application.getJobOffer()
                                .getRecruiter()
                                .getId()
                                .equals(recruiterId))
                .map(Mapper::toJobApplicationDTO)
                .toList();

        candidateDTO.setJobApplications(recruiterApplications);

        return candidateDTO;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Authentication failed because username={} was not found", username);
                    return new UsernameNotFoundException(username);
                });

        log.info("User with username={} loaded successfully for authentication", username);

        return AuthenticationUserDetails
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }
}
