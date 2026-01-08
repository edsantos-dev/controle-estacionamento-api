package br.com.api.estacionamento.dto;

public record RespostaDeErroDTO(
    int status,
    String message
) {

}
