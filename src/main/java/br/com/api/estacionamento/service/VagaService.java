package br.com.api.estacionamento.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.api.estacionamento.dto.DadosListagemVagaDTO;
import br.com.api.estacionamento.dto.DadosVagaDTO;
import br.com.api.estacionamento.exception.RegraNegocioException;
import br.com.api.estacionamento.model.Vaga;
import br.com.api.estacionamento.repository.VagaRepository;

@Service
public class VagaService {

    @Autowired
    private VagaRepository vagaRepository;
    
    public Vaga salvarVaga(DadosVagaDTO dados) {

        if (vagaRepository.existsByNumero(dados.numero())) {
            throw new RegraNegocioException("Vaga ja cadastrada");
        }

        return vagaRepository.save(new Vaga(dados));
    }

    public List<DadosListagemVagaDTO> listarVagas(){
        return vagaRepository.findAll().stream().map(DadosListagemVagaDTO::new).toList();
    }
        
}
