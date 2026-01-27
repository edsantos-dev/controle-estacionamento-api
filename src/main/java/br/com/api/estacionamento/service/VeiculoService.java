package br.com.api.estacionamento.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.api.estacionamento.dto.DadosDetalhamentoVeiculoDTO;
import br.com.api.estacionamento.dto.DadosEdicaoVeiculoDTO;
import br.com.api.estacionamento.dto.DadosVeiculoDTO;
import br.com.api.estacionamento.exception.RecursoNaoEncontradoException;
import br.com.api.estacionamento.exception.RegraNegocioException;
import br.com.api.estacionamento.model.Veiculo;
import br.com.api.estacionamento.repository.VeiculoRepository;

@Service
public class VeiculoService {

    @Autowired
    private VeiculoRepository veiculoRepository;

    public DadosDetalhamentoVeiculoDTO cadastrarVeiculo(DadosVeiculoDTO dados){

        if(veiculoRepository.existsByPlaca(dados.placa().trim().toUpperCase())){
            throw new RegraNegocioException("O veiculo ja esta cadastrado");
        }

        var veiculo = veiculoRepository.save(new Veiculo(dados));

        return new DadosDetalhamentoVeiculoDTO(veiculo);

    }

    public DadosDetalhamentoVeiculoDTO listarVeiculoPorID(Long id){

        var veiculoId = veiculoRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Veiculo nao encontrado"));

        return new DadosDetalhamentoVeiculoDTO(veiculoId);

    }

    public DadosDetalhamentoVeiculoDTO editarVeiculo(Long id, DadosEdicaoVeiculoDTO dados){

        var veiculoId = veiculoRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Veiculo nao encontrado"));

        veiculoId.setTipo(dados.tipo());
        veiculoRepository.save(veiculoId);

        return new DadosDetalhamentoVeiculoDTO(veiculoId);
    }

}
