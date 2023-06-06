package lv.vladislavs.ewallet.model.database.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

import java.time.ZonedDateTime;

@Entity(name = "ewallet_user")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(unique = true, nullable = false)
    private String email;

    private String passwordHash;

    private String name;

    private String surname;

    private String phoneNumber;

    @Builder.Default
    @Column(nullable = false)
    private boolean isSuspicious = false;

    private ZonedDateTime prohibitOutgoingTransactions;

    private ZonedDateTime prohibitIncomingTransactions;
}
