package lv.vladislavs.ewallet.service;

import lv.vladislavs.ewallet.converter.wallet.CreateWalletRequestMapper;
import lv.vladislavs.ewallet.converter.wallet.WalletMapper;
import lv.vladislavs.ewallet.model.database.user.User;
import lv.vladislavs.ewallet.model.database.wallet.Wallet;
import lv.vladislavs.ewallet.model.dto.wallet.CreateWalletRequest;
import lv.vladislavs.ewallet.model.dto.wallet.WalletDto;
import lv.vladislavs.ewallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletService {
    @Autowired
    WalletRepository walletRepository;

    public Iterable<WalletDto> getUserWallets(@NonNull User owner) {
        List<Wallet> wallets = walletRepository.findByUserId(owner.getId());
        return WalletMapper.INSTANCE.walletsToWalletDtos(wallets);
    }

    public void createWallet(@NonNull CreateWalletRequest createWalletRequest, @NonNull User owner) {
        Wallet newWallet = CreateWalletRequestMapper.INSTANCE.createWalletRequestToWallet(createWalletRequest, owner);
        walletRepository.save(newWallet);
    }
}