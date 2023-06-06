package lv.vladislavs.ewallet.model.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class UserDto {
    private String email;
    private String name;
    private String surname;
    private String phoneNumber;
    private boolean isSuspicious;
    private ZonedDateTime prohibitOutgoingTransactions;
    private ZonedDateTime prohibitIncomingTransactions;
}