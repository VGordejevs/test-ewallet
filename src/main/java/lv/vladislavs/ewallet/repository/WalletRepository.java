package lv.vladislavs.ewallet.repository;

import lv.vladislavs.ewallet.model.database.wallet.Wallet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends CrudRepository<Wallet, Long> {
    List<Wallet> findByUserId(long userId);
}