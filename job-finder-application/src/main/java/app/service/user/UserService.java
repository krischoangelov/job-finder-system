package app.service.user;

import app.model.dto.user.UserDTO;
import app.model.dto.user.UserLoginRequestDTO;
import app.model.dto.user.UserRegisterRequestDTO;
import app.model.dto.user.UserUpdateProfileRequest;
import app.model.entity.user.User;
import app.repository.user.UserRepository;
import app.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO login(UserLoginRequestDTO userLoginRequest) {
       User user = userRepository.findByEmail(userLoginRequest.getEmail()).orElse(null);

       if (user == null || !passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
           throw new RuntimeException("Email or password is invalid.");
       }

        return Mapper.toUserDTO(user);
    }

    public UserDTO register(UserRegisterRequestDTO userRegisterRequest) {

        Optional<User> user = userRepository.findByUsername(userRegisterRequest.getUsername());
        if (user.isPresent()) {
            throw new RuntimeException("User with this username already exists");
        }

        if (!userRegisterRequest.getPassword().equals(userRegisterRequest.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        String encodedPassword = passwordEncoder.encode(userRegisterRequest.getPassword());
        userRegisterRequest.setPassword(encodedPassword);

        User entityUser = Mapper.toUserEntity(userRegisterRequest);

        userRepository.save(entityUser);

        return Mapper.toUserDTO(entityUser);
    }

    public UserDTO getById(UUID uuid) {
        User user = userRepository.findById(uuid).orElse(null);

        if (user == null)  {
            throw new RuntimeException("No such user was found");
        }

        return Mapper.toUserDTO(user);
    }


    public UserDTO updateProfile(UUID id, UserUpdateProfileRequest userUpdateProfileRequest) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            throw new RuntimeException("User with the searched ID does not exist");
        }

        user.setUsername(userUpdateProfileRequest.getUsername());
        user.setEmail(userUpdateProfileRequest.getEmail());
        user.setFirstName(userUpdateProfileRequest.getFirstName());
        user.setLastName(userUpdateProfileRequest.getLastName());

        return Mapper.toUserDTO(userRepository.save(user));
    }
}
