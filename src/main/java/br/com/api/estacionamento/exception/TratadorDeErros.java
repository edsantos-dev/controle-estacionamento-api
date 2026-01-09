package br.com.api.estacionamento.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<String> tratarRegraNegocio(RegraNegocioException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
