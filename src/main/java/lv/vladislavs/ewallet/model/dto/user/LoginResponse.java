package lv.vladislavs.ewallet.model.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginResponse {
    private String jwtToken;
    private UserDto user;
}
