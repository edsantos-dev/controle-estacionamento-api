package br.com.api.estacionamento.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.api.estacionamento.dto.RespostaDeErroDTO;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(RegraNegocioException.class)
    public RespostaDeErroDTO tratarRegraNegocio(RegraNegocioException ex) {
        return new RespostaDeErroDTO(HttpStatus.CONFLICT.value(), ex.getMessage());
    }

}
