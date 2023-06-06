package lv.vladislavs.ewallet.model.database.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lv.vladislavs.ewallet.exception.transaction.TransactionStatusUnknownException;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum TransactionStatus {
    PENDING,
    COMPLETED,
    DECLINED;

    public static TransactionStatus fromString(String transactionStatus) {
        return Arrays.stream(values())
                .filter(archivingMethod -> archivingMethod.name().equalsIgnoreCase(transactionStatus))
                .findFirst()
                .orElseThrow(() -> new TransactionStatusUnknownException(transactionStatus));
    }
}
