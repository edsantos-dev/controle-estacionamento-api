package br.com.api.estacionamento.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.api.estacionamento.dto.DadosDetalhamentoEstadiaDTO;
import br.com.api.estacionamento.dto.DadosEncerramentoEstadiaDTO;
import br.com.api.estacionamento.dto.DadosEstadiaDTO;
import br.com.api.estacionamento.exception.RecursoNaoEncontradoException;
import br.com.api.estacionamento.exception.RegraNegocioException;
import br.com.api.estacionamento.model.Estadia;
import br.com.api.estacionamento.model.StatusEstadia;
import br.com.api.estacionamento.model.Vaga;
import br.com.api.estacionamento.model.Veiculo;
import br.com.api.estacionamento.repository.EstadiaRepository;
import br.com.api.estacionamento.repository.VagaRepository;
import br.com.api.estacionamento.repository.VeiculoRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EstadiaService {

    @Autowired
    private VagaRepository vagaRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private EstadiaRepository estadiaRepository;

    public DadosDetalhamentoEstadiaDTO iniciarEstadia(DadosEstadiaDTO dados){

        Vaga vaga = vagaRepository.findById(dados.idVaga())
        .orElseThrow(() -> new RecursoNaoEncontradoException("A vaga não foi encontrada."));

        Veiculo veiculo = veiculoRepository.findById(dados.idVeiculo())
        .orElseThrow(() -> new RecursoNaoEncontradoException("O veículo não foi encontrado."));

        if(estadiaRepository.existsByVeiculoIdAndStatus(veiculo.getId(), StatusEstadia.ATIVA)){
            throw new RegraNegocioException("O veículo já possui uma estadia ativa.");
        }

        vaga.ocupar();
        vagaRepository.save(vaga);

        Estadia estadia = estadiaRepository.save(new Estadia(vaga, veiculo));

        return new DadosDetalhamentoEstadiaDTO(estadia);
    }

    public DadosEncerramentoEstadiaDTO encerrarEstadia(Long id){

        Estadia estadia = estadiaRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Estadia não encontrada."));

        estadia.encerrarEstadia();

        return new DadosEncerramentoEstadiaDTO(estadia);
    }

}
