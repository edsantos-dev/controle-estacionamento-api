package br.com.api.estacionamento.dto;

import br.com.api.estacionamento.model.Vaga;

public record DadosListagemVagaDTO(
    
    int numero, 
    boolean status
) {

    public DadosListagemVagaDTO(Vaga vaga){
        this(vaga.getNumero(), vaga.isStatus());
    }
}
