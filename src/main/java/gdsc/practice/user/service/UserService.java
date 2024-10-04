package gdsc.practice.user.service;

import gdsc.practice.user.domain.User;
import gdsc.practice.user.dto.LoginRequest;
import gdsc.practice.user.dto.SignupRequest;
import gdsc.practice.user.dto.UserInfo;
import gdsc.practice.user.exception.AlreadyExistsEmailException;
import gdsc.practice.user.exception.AlreadyExistsUsernameException;
import gdsc.practice.user.exception.PasswordNotMatchException;
import gdsc.practice.user.exception.UserNotFound;
import gdsc.practice.user.repository.UserRepository;
import gdsc.practice.utils.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(SignupRequest signupRequest) {
        Optional<User> userOptional = userRepository.findByEmail(signupRequest.getEmail());
        if(userOptional.isPresent()) {
            if(userOptional.get().getUsername().equals(signupRequest.getUsername())) {
                throw new AlreadyExistsEmailException();
            }
            throw new AlreadyExistsUsernameException();
        }
        if(!Objects.equals(signupRequest.getPassword1(), signupRequest.getPassword2())){
            throw new PasswordNotMatchException();
        }
        String encryptPassword = getEncryptedPassword(signupRequest.getEmail(), signupRequest.getPassword1());

        User user = User.builder()
                .email(signupRequest.getEmail())
                .password(encryptPassword)
                .username(signupRequest.getUsername())
                .build();

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserInfo login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(UserNotFound::new);

        if (!Objects.equals(user.getPassword(), loginRequest.getPassword())) {
            throw new PasswordNotMatchException();
        }

        return UserInfo.builder()
                .id(String.valueOf(user.getId()))
                .email(user.getEmail())
                .password(user.getPassword())
                .username(user.getUsername())
                .build();
    }

    private String getEncryptedPassword(String email, String password) {
        return passwordEncoder.encode(email, password);
    }
}
