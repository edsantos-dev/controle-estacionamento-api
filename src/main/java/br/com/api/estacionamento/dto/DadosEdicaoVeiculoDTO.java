package br.com.api.estacionamento.dto;

import br.com.api.estacionamento.model.Tipo;
import jakarta.validation.constraints.NotNull;

public record DadosEdicaoVeiculoDTO(

    @NotNull
    Tipo tipo

) {

}
