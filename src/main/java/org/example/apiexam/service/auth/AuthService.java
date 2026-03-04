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
                .orElseThrow(() -> new UserNotFoundException("The user " + userName + " does not exist"));

        // Verificam el password
        if (!passwordMatches(password, user.getPassword())) {
            throw new BadCredentialsException("Wrong credentials for user " + userName);
        }

        // Genera i retorna token
        return jwtUtils.generateToken(user);
    }

    public boolean passwordMatches(String rawPassword, String encryptedPassword) {
        // Usa una biblioteca como BCrypt para comparar contraseñas
        return BCrypt.checkpw(rawPassword, encryptedPassword);
    }
}
