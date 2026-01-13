package br.com.api.estacionamento.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.api.estacionamento.dto.RespostaDeErroDTO;


@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<RespostaDeErroDTO> tratarRegraNegocio(RegraNegocioException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new RespostaDeErroDTO(409, ex.getMessage()));
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<RespostaDeErroDTO> tratarNaoEncotrado(RecursoNaoEncontradoException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RespostaDeErroDTO(404, ex.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> tratarDTONaoPreenchidoCorretamente (){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RespostaDeErroDTO(400, "Algum(ns) campo(s) foi(ram) preenchido(s) incorretamente."));
    }

}
