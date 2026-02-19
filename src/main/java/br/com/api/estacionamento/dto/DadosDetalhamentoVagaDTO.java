package br.com.api.estacionamento.dto;

import br.com.api.estacionamento.model.Vaga;

public record DadosDetalhamentoVagaDTO(
    Long id,
    int numero,
    boolean ocupada
) {

    public DadosDetalhamentoVagaDTO(Vaga vaga) {
        this(vaga.getId(), vaga.getNumero(), vaga.isOcupada());
    }

}
