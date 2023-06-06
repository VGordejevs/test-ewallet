package lv.vladislavs.ewallet.model.dto.transaction;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateTransactionRequest {
    private String type;
    private Long sourceWalletId;
    private Long targetWalletId;
    private BigDecimal amount;
}
