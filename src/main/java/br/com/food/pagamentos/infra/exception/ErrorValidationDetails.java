package br.com.food.pagamentos.infra.exception;

public record ErrorValidationDetails(String field, String message) {
    public static ErrorValidationDetails validationDetails(String field, String message) {
        return new ErrorValidationDetails(
                field,
                message
        );
    }
}
