package br.com.api.estacionamento.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.api.estacionamento.dto.DadosDetalhamentoVagaDTO;
import br.com.api.estacionamento.dto.DadosListagemVagaDTO;
import br.com.api.estacionamento.dto.DadosVagaDTO;
import br.com.api.estacionamento.exception.RecursoNaoEncontradoException;
import br.com.api.estacionamento.exception.RegraNegocioException;
import br.com.api.estacionamento.model.Vaga;
import br.com.api.estacionamento.repository.VagaRepository;
import lombok.var;

@Service
public class VagaService {

    @Autowired
    private VagaRepository vagaRepository;
    
    public DadosDetalhamentoVagaDTO salvarVaga(DadosVagaDTO dados) {

        if (vagaRepository.existsByNumero(dados.numero())) {
            throw new RegraNegocioException("Vaga já cadastrada.");
        }

        var vaga = vagaRepository.save(new Vaga(dados));

        return new DadosDetalhamentoVagaDTO(vaga);
    }

    public List<DadosListagemVagaDTO> listarVagas(){
        return vagaRepository.findAll().stream().map(DadosListagemVagaDTO::new).toList();
    }

    public DadosDetalhamentoVagaDTO listarPorId (Long id) {

        var vagaId = vagaRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Vaga não encontrada."));

        return new DadosDetalhamentoVagaDTO(vagaId);
    }

    public DadosDetalhamentoVagaDTO ocuparVaga(Long id){

        var vaga = vagaRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Vaga não encontrada."));

        vaga.ocupar();
        vagaRepository.save(vaga);

        return new DadosDetalhamentoVagaDTO(vaga);

    }

    public DadosDetalhamentoVagaDTO liberarVaga(Long id){

        var vaga = vagaRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Vaga não encontrada."));

        vaga.liberar();
        vagaRepository.save(vaga);

        return new DadosDetalhamentoVagaDTO(vaga);
    }

    public void deletarVaga(Long id){

        var vaga = vagaRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Vaga não encontrada."));

        if(!vaga.isOcupada()){
            throw new RegraNegocioException("Não é possível deletar uma vaga que está ocupada.");
        }

        vagaRepository.delete(vaga);
    }
        
}
