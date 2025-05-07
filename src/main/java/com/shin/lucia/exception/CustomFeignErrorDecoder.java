package com.shin.lucia.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CustomFeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        HttpStatus status = HttpStatus.valueOf(response.status());

        return switch (status) {
            case NOT_FOUND -> new ResponseStatusException(status, "Empresa não encontrada");
            case UNAUTHORIZED -> new ResponseStatusException(status, "Token inválido");
            case FORBIDDEN -> new ResponseStatusException(status, "Acesso negado à empresa");
            default -> new ResponseStatusException(status, "Erro ao buscar empresa");
        };
    }
}
