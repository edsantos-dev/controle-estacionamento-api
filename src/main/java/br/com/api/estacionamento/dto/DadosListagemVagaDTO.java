package br.com.api.estacionamento.dto;

import br.com.api.estacionamento.model.Vaga;

public record DadosListagemVagaDTO(
    
    int numero, 
    boolean ocupada
) {

    public DadosListagemVagaDTO(Vaga vaga){
        this(vaga.getNumero(), vaga.isOcupada());
    }
}
