package br.com.api.estacionamento.service;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.api.estacionamento.dto.DadosIniciacaoEstadiaDTO;
import br.com.api.estacionamento.dto.DadosGeracaoDeCobrancaEstadiaDTO;
import br.com.api.estacionamento.dto.DadosListagemEstadiaDTO;
import br.com.api.estacionamento.dto.DadosQuitacaoEstadiaDTO;
import br.com.api.estacionamento.dto.DadosEstadiaDTO;
import br.com.api.estacionamento.dto.DadosFaturamentoEstadiaDTO;
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

    public DadosIniciacaoEstadiaDTO iniciarEstadia(DadosEstadiaDTO dados){

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

        return new DadosIniciacaoEstadiaDTO(estadia);
    }

    public DadosGeracaoDeCobrancaEstadiaDTO gerarCobranca(Long id){

        Estadia estadia = encontrarEstadia(id);

        estadia.gerarCobranca();

        return new DadosGeracaoDeCobrancaEstadiaDTO(estadia);
    }

    public DadosQuitacaoEstadiaDTO quitarEstadia(Long id){

        Estadia estadia = encontrarEstadia(id);

        estadia.quitarEstadia();

        return new DadosQuitacaoEstadiaDTO(estadia);
    }

    public Page<DadosListagemEstadiaDTO> listarEstadia(StatusEstadia status, Pageable paginacao){
        
        Page<Estadia> estadiaPage;

        if(status == null){
            estadiaPage = estadiaRepository.findAll(paginacao);
        } else{
            estadiaPage = estadiaRepository.findByStatus(status, paginacao);
        }

        return estadiaPage.map(DadosListagemEstadiaDTO::new);
    }

    public DadosFaturamentoEstadiaDTO obterRelatorioFaturamento(LocalDate inicio, LocalDate fim){

        var dataIniciDoDia = inicio.atStartOfDay();
        var dataFimDoDia = fim.atTime(LocalTime.MAX);

        return estadiaRepository.calcularFaturamentoPorPeriodo(
            StatusEstadia.ENCERRADA,
            dataIniciDoDia,
            dataFimDoDia
        );
    }

    private Estadia encontrarEstadia(long id){
        
        return estadiaRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Estadia não encontrada."));
    }
}
