package br.com.api.estacionamento.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.api.estacionamento.model.Estadia;
import br.com.api.estacionamento.model.StatusEstadia;

public record DadosGeracaoDeCobrancaEstadiaDTO(

    Long id,
    int vagaNumero,
    String veiculoPlaca,
    LocalDateTime dataEntrada,
    LocalDateTime dataSaida,
    BigDecimal valorFinal,
    StatusEstadia status

) {

    public DadosGeracaoDeCobrancaEstadiaDTO(Estadia estadia){
        this(estadia.getId(), estadia.getVaga().getNumero(), estadia.getVeiculo().getPlaca(), 
        estadia.getDataEntrada(), estadia.getDataSaida(), estadia.getValorFinal(), estadia.getStatus());
    }
}
