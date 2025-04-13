package com.shin.lucia.exception;

public class S3StorageException extends RuntimeException {
    public S3StorageException(String message) {
        super("Erro ao acessar armazenamento S3: " + message);
    }
}
