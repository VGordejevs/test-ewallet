package lv.vladislavs.ewallet.controller;

import lv.vladislavs.ewallet.authentication.SecurityContext;
import lv.vladislavs.ewallet.model.dto.wallet.CreateWalletRequest;
import lv.vladislavs.ewallet.model.dto.wallet.WalletDto;
import lv.vladislavs.ewallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {
    @Autowired
    WalletService walletService;

    @GetMapping
    public ResponseEntity<Iterable<WalletDto>> getWallets() {
        return ResponseEntity.ok(walletService.getUserWallets(SecurityContext.getUser()));
    }

    @PostMapping
    public ResponseEntity<Void> createWallet(@RequestBody CreateWalletRequest createWalletRequest) {
        walletService.createWallet(createWalletRequest, SecurityContext.getUser());
        return ResponseEntity.ok().build();
    }
}
