package lv.vladislavs.ewallet.model.dto.wallet;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WalletDto {
    private long id;
    private String title;
    private BigDecimal amount;
}
