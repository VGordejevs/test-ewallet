package lv.vladislavs.ewallet.converter.transaction;

import lv.vladislavs.ewallet.model.database.transaction.TransactionType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionTypeMapper {
    TransactionTypeMapper INSTANCE = Mappers.getMapper(TransactionTypeMapper.class);

    default TransactionType transactionTypeFromString(String transactionType) {
        return TransactionType.fromString(transactionType);
    }
}
