package lv.vladislavs.ewallet.converter.transaction;

import lv.vladislavs.ewallet.model.database.transaction.TransactionStatus;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionStatusMapper {
    TransactionStatusMapper INSTANCE = Mappers.getMapper(TransactionStatusMapper.class);

    default TransactionStatus transactionStatusFromString(String transactionStatus) {
        return TransactionStatus.fromString(transactionStatus);
    }
}
