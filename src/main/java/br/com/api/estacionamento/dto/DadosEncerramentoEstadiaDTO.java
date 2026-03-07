package br.com.api.estacionamento.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.api.estacionamento.model.Estadia;
import br.com.api.estacionamento.model.StatusEstadia;

public record DadosEncerramentoEstadiaDTO(

    Long id,
    int vagaNumero,
    String veiculoPlaca,
    LocalDateTime dataEntrada,
    LocalDateTime dataSaida,
    BigDecimal valorFinal,
    StatusEstadia status

) {

    public DadosEncerramentoEstadiaDTO(Estadia estadia){
        this(estadia.getId(), estadia.getVaga().getNumero(), estadia.getVeiculo().getPlaca(), 
        estadia.getDataEntrada(), estadia.getDataSaida(), estadia.getValorFinal(), estadia.getStatus());
    }
}
