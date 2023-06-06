package lv.vladislavs.ewallet.model.database.transaction;

import jakarta.persistence.*;
import lombok.*;
import lv.vladislavs.ewallet.model.database.user.User;
import lv.vladislavs.ewallet.model.database.wallet.Wallet;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity(name = "transaction")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Transaction {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="initiatorUserId", nullable = false)
    private User initiator;

    @CreatedDate
    private ZonedDateTime creationDateTime;

    private TransactionType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="sourceWalletId")
    private Wallet sourceWallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="targetWalletId")
    private Wallet targetWallet;

    private BigDecimal amount;

    @Builder.Default
    @Column(nullable = false)
    private TransactionStatus status = TransactionStatus.PENDING;

    @Builder.Default
    @Column(nullable = false)
    private boolean isSuspicious = false;
}
