package br.com.api.estacionamento.dto;

import jakarta.validation.constraints.NotNull;

public record DadosVagaDTO(

    @NotNull
    Integer numero

) {

}
