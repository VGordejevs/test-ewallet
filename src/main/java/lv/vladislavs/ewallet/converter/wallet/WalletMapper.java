package lv.vladislavs.ewallet.converter.wallet;

import lv.vladislavs.ewallet.model.database.wallet.Wallet;
import lv.vladislavs.ewallet.model.dto.wallet.WalletDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WalletMapper {
    WalletMapper INSTANCE = Mappers.getMapper(WalletMapper.class);

    WalletDto walletToWalletDto(Wallet wallet);

    Iterable<WalletDto> walletsToWalletDtos(Iterable<Wallet> wallets);
}
