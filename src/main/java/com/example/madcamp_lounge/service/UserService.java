package com.example.madcamp_lounge.service;

import java.util.Optional;

import com.example.madcamp_lounge.dto.LoginRequest;
import com.example.madcamp_lounge.entity.User;
import com.example.madcamp_lounge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 로그인 기능
     * 화면에서 LoginRequest(loginId, password)을 입력받아 loginId와 password가 일치하면 User
     * return
     * loginId가 존재하지 않거나 password가 일치하지 않으면 null return
     */
    public Optional<User> login(LoginRequest req) {
        Optional<User> optionalUser = userRepository.findByLoginId(req.getLoginId());

        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            return Optional.empty();
        }

        return Optional.of(user);
    }

    /**
     * userId(Long)를 입력받아 User을 return 해주는 기능
     * 인증, 인가 시 사용
     * userId가 null이거나(로그인 X) userId로 찾아온 User가 없으면 null return
     * userId로 찾아온 User가 존재하면 User return
     */
    public Optional<User> getLoginUserById(Long userId) {
        if (userId == null) {
            return Optional.empty();
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser;
    }

    /**
     * loginId(String)를 입력받아 User을 return 해주는 기능
     * 인증, 인가 시 사용
     * loginId가 null이거나(로그인 X) userId로 찾아온 User가 없으면 null return
     * loginId로 찾아온 User가 존재하면 User return
     */
    public Optional<User> getLoginUserByLoginId(String loginId) {
        if (loginId == null) {
            return Optional.empty();
        }

        Optional<User> optionalUser = userRepository.findByLoginId(loginId);
        return optionalUser;
    }
}
