package lv.vladislavs.ewallet.service;

import lv.vladislavs.ewallet.Constants;
import lv.vladislavs.ewallet.authentication.JwtUtil;
import lv.vladislavs.ewallet.converter.user.RegistrationRequestMapper;
import lv.vladislavs.ewallet.converter.user.UserMapper;
import lv.vladislavs.ewallet.exception.user.UserRegistrationFailedException;
import lv.vladislavs.ewallet.exception.user.UserWrongCredentialsException;
import lv.vladislavs.ewallet.model.database.user.User;
import lv.vladislavs.ewallet.model.dto.user.JwtUserInfo;
import lv.vladislavs.ewallet.model.dto.user.LoginResponse;
import lv.vladislavs.ewallet.model.dto.user.RegistrationRequest;
import lv.vladislavs.ewallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public void performRegistration(@NonNull RegistrationRequest registrationRequest) {
        User newUser = RegistrationRequestMapper.INSTANCE.registrationRequestToUser(registrationRequest);

        try {
            userRepository.save(newUser);
        } catch (DataIntegrityViolationException e) {
            throw new UserRegistrationFailedException("This e-mail is already in use. Please, do not use this for account enumeration :)", HttpStatus.CONFLICT);
        }
    }

    public LoginResponse performLogin(@NonNull String email, @NonNull String passwordHash) {
        User user = userRepository.findByEmailAndPasswordHash(email, passwordHash)
                .orElseThrow(UserWrongCredentialsException::new);

        JwtUserInfo jwtUserInfo = UserMapper.INSTANCE.userToJwtUserInfo(user);

        return LoginResponse.builder()
                .jwtToken(JwtUtil.generateToken(jwtUserInfo, Constants.AUTH_TOKEN_VALID_HOURS))
                .user(UserMapper.INSTANCE.userToUserDto(user))
                .build();
    }
}