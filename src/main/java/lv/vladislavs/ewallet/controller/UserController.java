package lv.vladislavs.ewallet.controller;

import lv.vladislavs.ewallet.model.dto.user.LoginRequest;
import lv.vladislavs.ewallet.model.dto.user.LoginResponse;
import lv.vladislavs.ewallet.model.dto.user.RegistrationRequest;
import lv.vladislavs.ewallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegistrationRequest registrationRequest) {
        userService.performRegistration(registrationRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.performLogin(loginRequest.getEmail(), loginRequest.getPasswordHash()));
    }
}
