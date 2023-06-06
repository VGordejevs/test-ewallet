package lv.vladislavs.ewallet.converter.user;

import lv.vladislavs.ewallet.model.database.user.User;
import lv.vladislavs.ewallet.model.dto.user.RegistrationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RegistrationRequestMapper {
    RegistrationRequestMapper INSTANCE = Mappers.getMapper(RegistrationRequestMapper.class);

    User registrationRequestToUser(RegistrationRequest registrationRequest);
}
