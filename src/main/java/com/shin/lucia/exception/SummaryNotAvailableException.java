package com.shin.lucia.exception;

public class SummaryNotAvailableException extends RuntimeException {
    public SummaryNotAvailableException(Long ideaId) {
        super("Não foi encontrado sumário para a ideia de ID " + ideaId);
    }
}
