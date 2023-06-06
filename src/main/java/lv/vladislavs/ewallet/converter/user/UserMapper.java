package lv.vladislavs.ewallet.converter.user;

import lv.vladislavs.ewallet.model.database.user.User;
import lv.vladislavs.ewallet.model.dto.user.JwtUserInfo;
import lv.vladislavs.ewallet.model.dto.user.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    JwtUserInfo userToJwtUserInfo(User user);
    UserDto userToUserDto(User user);
}
