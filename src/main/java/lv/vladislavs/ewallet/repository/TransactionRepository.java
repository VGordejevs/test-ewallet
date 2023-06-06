package lv.vladislavs.ewallet.repository;

import lv.vladislavs.ewallet.model.database.transaction.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> findByInitiatorIdAndCreationDateTimeBetween(long initiatorUserId, ZonedDateTime from, ZonedDateTime to);
}