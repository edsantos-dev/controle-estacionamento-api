package br.com.api.estacionamento.dto;

import java.math.BigDecimal;

public record DadosFaturamentoEstadiaDTO(

    Long quatidadeEstadias,
    BigDecimal faturamentoTotal
    
) {

}
