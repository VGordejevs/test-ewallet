package lv.vladislavs.ewallet.exception;

import org.springframework.http.HttpStatus;

public class EWalletException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus httpStatusCode;

    public EWalletException(HttpStatus httpStatusCode) {
        this(Integer.toString(httpStatusCode.value()), httpStatusCode);
    }

    public EWalletException(String code, HttpStatus httpStatusCode) {
        this(code, httpStatusCode.getReasonPhrase(), httpStatusCode);
    }

    public EWalletException(String code, String message, HttpStatus httpStatusCode) {
        super(message);
        this.errorCode = code;
        this.httpStatusCode = httpStatusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatusCode() {
        return httpStatusCode;
    }
}