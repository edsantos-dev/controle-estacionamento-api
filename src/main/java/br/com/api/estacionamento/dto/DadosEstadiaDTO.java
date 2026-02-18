package br.com.api.estacionamento.dto;

import jakarta.validation.constraints.NotNull;

public record DadosEstadiaDTO(

    @NotNull
    Long idVaga,
    @NotNull
    Long idVeiculo

) {

}