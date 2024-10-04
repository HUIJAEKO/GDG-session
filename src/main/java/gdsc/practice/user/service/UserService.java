package gdsc.practice.user.service;

import gdsc.practice.user.domain.User;
import gdsc.practice.user.dto.SignupRequest;
import gdsc.practice.user.repository.UserRepository;
import gdsc.practice.utils.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignupRequest signupRequest) {
        Optional<User> userOptional = userRepository.findByEmail(signupRequest.getEmail());

        String encryptPassword = getEncryptedPassword(signupRequest.getEmail(), signupRequest.getPassword1());

        User user = User.builder()
                .email(signupRequest.getEmail())
                .password(encryptPassword)
                .username(signupRequest.getUsername())
                .build();

        userRepository.save(user);
    }

    private String getEncryptedPassword(String email, String password) {
        return passwordEncoder.encode(email, password);
    }
}
