package lv.vladislavs.ewallet.converter.transaction;

import lv.vladislavs.ewallet.exception.transaction.TransactionTypeNotSupportedException;
import lv.vladislavs.ewallet.model.database.transaction.TransactionType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransactionTypeMapperTest {
    TransactionTypeMapper mapper = TransactionTypeMapper.INSTANCE;

    @Test
    void testTransactionTypeMapper() {
        assertEquals(TransactionType.DEPOSIT, mapper.transactionTypeFromString("deposit"));
        assertEquals(TransactionType.WITHDRAW, mapper.transactionTypeFromString("withdraw"));
        assertEquals(TransactionType.TRANSFER, mapper.transactionTypeFromString("transfer"));
    }

    @Test
    void testTransactionTypeMapperNotSupported() {
        assertThrows(TransactionTypeNotSupportedException.class, () ->
                mapper.transactionTypeFromString("unsupported-transaction-type"));
    }
}
