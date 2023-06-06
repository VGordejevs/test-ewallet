package lv.vladislavs.ewallet.model.database.wallet;

import jakarta.persistence.*;
import lombok.*;
import lv.vladislavs.ewallet.model.database.user.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity(name = "wallet")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Wallet {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId", nullable = false)
    private User user;

    @CreatedDate
    @Column(nullable = false)
    private ZonedDateTime creationDateTime;

    private String title;

    @NonNull
    @Builder.Default
    @Column(nullable = false)
    private BigDecimal amount = BigDecimal.ZERO;
}
