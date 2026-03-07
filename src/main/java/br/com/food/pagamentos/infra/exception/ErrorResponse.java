package br.com.food.pagamentos.infra.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record ErrorResponse(
        int status,
        LocalDateTime timeStamp,
        String error,
        List<ErrorValidationDetails> errorDetails
) {
    public static ErrorResponse detalhesDoErro(int status, LocalDateTime timeStamp, String error) {
        return new ErrorResponse(
                status,
                timeStamp,
                error,
                new ArrayList<>()
        );
    }

    public static ErrorResponse detalhesDoErro(int status, LocalDateTime timeStamp, String error, List<ErrorValidationDetails> validationDetails) {
        return new ErrorResponse(
                status,
                timeStamp,
                error,
                validationDetails
        );
    }
}
