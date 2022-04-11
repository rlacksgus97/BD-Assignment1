package com.bd.assignment1.user;

import com.bd.assignment1.config.jwt.JwtService;
import com.bd.assignment1.user.dto.JoinReqDto;
import com.bd.assignment1.user.dto.JoinResDto;
import com.bd.assignment1.user.dto.LoginReqDto;
import com.bd.assignment1.user.dto.LoginResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Transactional
    public JoinResDto join(JoinReqDto joinDto) {
        Optional<User> user = userRepository.findByEmail(joinDto.getEmail());
        if (user.isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        } else {
            User newUser = User.builder()
                    .email(joinDto.getEmail())
                    .password(joinDto.getPassword())
                    .build();
            return new JoinResDto(userRepository.save(newUser).getId());
        }
    }

    @Transactional
    public LoginResDto login(LoginReqDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
        if (user.getPassword().equals(loginDto.getPassword())) {
            return new LoginResDto(jwtService.createToken(user.getId()));
        } else {
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }
    }
}
