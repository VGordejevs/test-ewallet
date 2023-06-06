package lv.vladislavs.ewallet.model.database.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lv.vladislavs.ewallet.exception.transaction.TransactionTypeNotSupportedException;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum TransactionType {
    DEPOSIT,
    WITHDRAW,
    TRANSFER;

    public static TransactionType fromString(String transactionType) {
        return Arrays.stream(values())
                .filter(archivingMethod -> archivingMethod.name().equalsIgnoreCase(transactionType))
                .findFirst()
                .orElseThrow(() -> new TransactionTypeNotSupportedException(transactionType));
    }
}
