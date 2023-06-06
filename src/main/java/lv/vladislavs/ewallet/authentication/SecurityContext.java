package lv.vladislavs.ewallet.authentication;

import lv.vladislavs.ewallet.exception.authentication.AuthenticationFailedException;
import lv.vladislavs.ewallet.exception.user.UserNotFoundException;
import lv.vladislavs.ewallet.model.database.user.User;
import lv.vladislavs.ewallet.model.dto.user.JwtUserInfo;
import lv.vladislavs.ewallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContext {
    @Autowired
    private static UserRepository userRepository;

    SecurityContext(UserRepository userRepository) {
        SecurityContext.userRepository = userRepository;
    }

    /**
     * @return Object that represents the user. Is guaranteed to be not null, if controller behind security is reached.
     */
    private static JwtUserInfo getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AuthenticationFailedException();
        }

        Object authenticationDetails = authentication.getDetails();
        if (authenticationDetails instanceof JwtUserInfo) {
            return (JwtUserInfo) authenticationDetails;
        }

        throw new AuthenticationFailedException();
    }

    public static User getUser() {
        JwtUserInfo jwtUserInfo = getUserInfo();
        return userRepository.findByEmail(jwtUserInfo.getEmail())
                .orElseThrow(() -> new UserNotFoundException(jwtUserInfo.getEmail()));
    }
}
