package br.com.api.estacionamento.dto;

import br.com.api.estacionamento.model.Tipo;
import br.com.api.estacionamento.model.Veiculo;

public record DadosDetalhamentoVeiculoDTO(

    Long id,
    String placa,
    Tipo tipo

) {

    public DadosDetalhamentoVeiculoDTO(Veiculo veiculo){
        this(veiculo.getId(), veiculo.getPlaca(), veiculo.getTipo());
    }

}
