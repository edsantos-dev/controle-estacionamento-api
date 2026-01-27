package br.com.api.estacionamento.dto;

import br.com.api.estacionamento.model.Tipo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosVeiculoDTO(

    @NotBlank
    String placa,
    @NotNull
    Tipo tipo

) {

}
