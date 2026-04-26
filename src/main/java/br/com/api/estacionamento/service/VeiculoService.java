package br.com.api.estacionamento.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.api.estacionamento.dto.DadosDetalhamentoVeiculoDTO;
import br.com.api.estacionamento.dto.DadosEdicaoVeiculoDTO;
import br.com.api.estacionamento.dto.DadosVeiculoDTO;
import br.com.api.estacionamento.exception.RecursoNaoEncontradoException;
import br.com.api.estacionamento.exception.RegraNegocioException;
import br.com.api.estacionamento.model.Veiculo;
import br.com.api.estacionamento.repository.VeiculoRepository;

@Service
@Transactional (readOnly = true)
public class VeiculoService {

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Transactional
    public DadosDetalhamentoVeiculoDTO cadastrarVeiculo(DadosVeiculoDTO dados){

        if(veiculoRepository.existsByPlaca(dados.placa().trim().toUpperCase())){
            throw new RegraNegocioException("O veículo já está cadastrado.");
        }

        var veiculo = veiculoRepository.save(new Veiculo(dados));

        return new DadosDetalhamentoVeiculoDTO(veiculo);

    }

    public List<DadosDetalhamentoVeiculoDTO> listarVeiculos(){
        return veiculoRepository.findAll().stream().map(DadosDetalhamentoVeiculoDTO::new).toList();
    }

    public DadosDetalhamentoVeiculoDTO listarVeiculoPorID(Long id){

        var veiculo = encontrarVeiculo(id);

        return new DadosDetalhamentoVeiculoDTO(veiculo);

    }

    @Transactional
    public DadosDetalhamentoVeiculoDTO editarVeiculo(Long id, DadosEdicaoVeiculoDTO dados){

        var veiculo = encontrarVeiculo(id);

        veiculo.setTipo(dados.tipo());

        return new DadosDetalhamentoVeiculoDTO(veiculo);
    }

    private Veiculo encontrarVeiculo(Long id){

        return veiculoRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Veículo não encontrado."));
    }
}
