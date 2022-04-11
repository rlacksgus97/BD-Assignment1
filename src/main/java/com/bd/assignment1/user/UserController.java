package com.bd.assignment1.user;

import com.bd.assignment1.user.dto.JoinReqDto;
import com.bd.assignment1.user.dto.JoinResDto;
import com.bd.assignment1.user.dto.LoginReqDto;
import com.bd.assignment1.user.dto.LoginResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<JoinResDto> join(@RequestBody JoinReqDto joinDto) {
        return ResponseEntity.ok(userService.join(joinDto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResDto> login(@RequestBody LoginReqDto loginDto) {
        return ResponseEntity.ok(userService.login(loginDto));
    }

    @GetMapping("/test")
    public ResponseEntity<LoginResDto> test() {
        return ResponseEntity.ok(null);
    }
}
