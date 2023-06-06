package lv.vladislavs.ewallet.converter.wallet;

import lv.vladislavs.ewallet.model.database.user.User;
import lv.vladislavs.ewallet.model.database.wallet.Wallet;
import lv.vladislavs.ewallet.model.dto.wallet.CreateWalletRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(builder = @Builder(disableBuilder = true))
public interface CreateWalletRequestMapper {
    CreateWalletRequestMapper INSTANCE = Mappers.getMapper(CreateWalletRequestMapper.class);

    @Mapping(target = "user", ignore = true)
    Wallet createWalletRequestToWallet(CreateWalletRequest createWalletRequest, @Context User owner);

    @AfterMapping
    default Wallet convert(CreateWalletRequest createWalletRequest, @MappingTarget Wallet wallet, @Context User owner) {
        wallet.setUser(owner);
        return wallet;
    }
}
