package lv.vladislavs.ewallet;

import java.math.BigDecimal;

public class Constants {
    public final static int AUTH_TOKEN_VALID_HOURS = 1;
    public final static BigDecimal TRANSACTION_MIN_AMOUNT = BigDecimal.ZERO;
    public final static BigDecimal TRANSACTION_MAX_AMOUNT = new BigDecimal("2000");
    public final static BigDecimal TRANSACTION_WITHDRAWAL_DAILY_LIMIT = new BigDecimal("5000");
    public final static BigDecimal TRANSACTION_SUSPICIOUS_AMOUNT = new BigDecimal("10000");
    public final static int TRANSACTION_SUSPICIOUS_HOURLY_COUNT = 5;
    public final static int TRANSACTION_PROHIBIT_HOURLY_COUNT = 10;
}
