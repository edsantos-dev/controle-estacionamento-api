package br.com.api.estacionamento.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.api.estacionamento.dto.DadosDetalhamentoVagaDTO;
import br.com.api.estacionamento.dto.DadosListagemVagaDTO;
import br.com.api.estacionamento.dto.DadosVagaDTO;
import br.com.api.estacionamento.exception.RecursoNaoEncontradoException;
import br.com.api.estacionamento.exception.RegraNegocioException;
import br.com.api.estacionamento.model.Vaga;
import br.com.api.estacionamento.repository.VagaRepository;
import lombok.var;

@Service
@Transactional (readOnly = true)
public class VagaService {

    @Autowired
    private VagaRepository vagaRepository;

    @Transactional
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

        var vagaId = encontrarVaga(id);

        return new DadosDetalhamentoVagaDTO(vagaId);
    }

    @Transactional
    public void deletarVaga(Long id){

        var vaga = encontrarVaga(id);

        if(vaga.isOcupada()){
            throw new RegraNegocioException("Não é possível deletar uma vaga que está ocupada.");
        }

        vagaRepository.delete(vaga);
    }

    private Vaga encontrarVaga(Long id){

        return vagaRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Vaga não encontrada."));
    }
        
}
