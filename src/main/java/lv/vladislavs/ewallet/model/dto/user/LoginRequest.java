package lv.vladislavs.ewallet.model.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String email;
    private String passwordHash;
}
