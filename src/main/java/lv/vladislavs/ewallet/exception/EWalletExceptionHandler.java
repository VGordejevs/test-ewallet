package lv.vladislavs.ewallet.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class EWalletExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleErrorException(EWalletException exception) {
        ErrorResponse errorResponse = new ErrorResponse.ErrorResponseBuilder()
                .errorCode(exception.getErrorCode())
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, exception.getHttpStatusCode());
    }
}
