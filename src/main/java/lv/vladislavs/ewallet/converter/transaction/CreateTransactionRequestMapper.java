package lv.vladislavs.ewallet.converter.transaction;

import lv.vladislavs.ewallet.exception.wallet.WalletNotFoundException;
import lv.vladislavs.ewallet.model.database.transaction.Transaction;
import lv.vladislavs.ewallet.model.dto.transaction.CreateTransactionRequest;
import lv.vladislavs.ewallet.repository.WalletRepository;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(builder = @Builder(disableBuilder = true),
        uses = {TransactionTypeMapper.class})
public interface CreateTransactionRequestMapper {
    CreateTransactionRequestMapper INSTANCE = Mappers.getMapper(CreateTransactionRequestMapper.class);

    @Mapping(target = "sourceWallet", ignore = true)
    @Mapping(target = "targetWallet", ignore = true)
    Transaction createTransactionRequestToTransaction(CreateTransactionRequest createTransactionRequest, @Context WalletRepository walletRepository);

    @AfterMapping
    default Transaction convert(CreateTransactionRequest createTransactionRequest, @MappingTarget Transaction transaction, @Context WalletRepository walletRepository) {
        Long sourceWalletId = createTransactionRequest.getSourceWalletId();
        Long targetWalletId = createTransactionRequest.getTargetWalletId();

        switch (transaction.getType()) {
            case DEPOSIT -> transaction.setTargetWallet(walletRepository.findById(targetWalletId)
                    .orElseThrow(() -> new WalletNotFoundException(targetWalletId)));

            case WITHDRAW -> transaction.setSourceWallet(walletRepository.findById(sourceWalletId)
                    .orElseThrow(() -> new WalletNotFoundException(sourceWalletId)));

            case TRANSFER -> {
                transaction.setTargetWallet(walletRepository.findById(targetWalletId)
                        .orElseThrow(() -> new WalletNotFoundException(targetWalletId)));
                transaction.setSourceWallet(walletRepository.findById(sourceWalletId)
                        .orElseThrow(() -> new WalletNotFoundException(sourceWalletId)));
            }
        }
        return transaction;
    }
}
