package com.shin.lucia.exception;

public class IdeaNotFoundException extends RuntimeException {
    public IdeaNotFoundException(Long id) {
        super("Ideia com ID " + id + " não encontrada.");
    }
}
