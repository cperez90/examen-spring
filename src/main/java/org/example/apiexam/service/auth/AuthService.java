package org.example.apiexam.service.auth;

import lombok.RequiredArgsConstructor;
import org.example.apiexam.error.BadCredentialsException;
import org.example.apiexam.error.UserNotFoundException;
import org.example.apiexam.model.User;
import org.example.apiexam.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtUtils;

    public String login(String userName, String password) {
        User user = userRepository.findByName(userName)
                .orElseThrow(() -> new UserNotFoundException(userName));

        if (!passwordMatches(password, user.getPassword())) {
            throw new BadCredentialsException("Wrong credentials for user " + userName);
        }

        return jwtUtils.generateToken(user);
    }

    public boolean passwordMatches(String rawPassword, String encryptedPassword) {
        return BCrypt.checkpw(rawPassword, encryptedPassword);
    }
}
