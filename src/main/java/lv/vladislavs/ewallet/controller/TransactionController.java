package lv.vladislavs.ewallet.controller;

import lv.vladislavs.ewallet.authentication.SecurityContext;
import lv.vladislavs.ewallet.model.dto.transaction.CreateTransactionRequest;
import lv.vladislavs.ewallet.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Void> createTransaction(@RequestBody CreateTransactionRequest createTransactionRequest) {
        transactionService.createTransaction(createTransactionRequest, SecurityContext.getUser());
        return ResponseEntity.ok().build();
    }
}
