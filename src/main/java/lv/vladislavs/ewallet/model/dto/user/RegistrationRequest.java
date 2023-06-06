package lv.vladislavs.ewallet.model.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {
    private String email;
    private String passwordHash;
    private String name;
    private String surname;
    private String phoneNumber;
}
