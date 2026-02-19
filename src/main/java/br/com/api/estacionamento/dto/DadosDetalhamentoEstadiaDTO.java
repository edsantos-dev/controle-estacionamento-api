package br.com.api.estacionamento.dto;

import java.time.LocalDateTime;

import br.com.api.estacionamento.model.Estadia;
import br.com.api.estacionamento.model.StatusEstadia;

public record DadosDetalhamentoEstadiaDTO(

    Long id,
    int vagaNumero,
    String veiculoPlaca,
    LocalDateTime dataEntrada,
    StatusEstadia status

) {

    public DadosDetalhamentoEstadiaDTO(Estadia estadia){
        this(estadia.getId(), estadia.getVaga().getNumero(), estadia.getVeiculo().getPlaca(), estadia.getDataEntrada(), estadia.getStatus());
    }

}
