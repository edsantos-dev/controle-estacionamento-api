package br.com.api.estacionamento.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.api.estacionamento.dto.DadosDetalhamentoVeiculoDTO;
import br.com.api.estacionamento.dto.DadosEdicaoVeiculoDTO;
import br.com.api.estacionamento.dto.DadosVeiculoDTO;
import br.com.api.estacionamento.service.VeiculoService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

    @Autowired
    private VeiculoService veiculoService;

    @PostMapping
    public ResponseEntity<DadosDetalhamentoVeiculoDTO> cadastrarVeiculo(@RequestBody @Valid DadosVeiculoDTO dados, UriComponentsBuilder uriBuilder) {
        
        var veiculo = veiculoService.cadastrarVeiculo(dados);
        var uri = uriBuilder.path("/veiculos/{id}").buildAndExpand(veiculo.id()).toUri();

        return ResponseEntity.created(uri).body(veiculo);
    }
    
    @GetMapping
    public ResponseEntity<List<DadosDetalhamentoVeiculoDTO>> listarVeiculos(){

        var veiculo = veiculoService.listarVeiculos();

        return ResponseEntity.ok().body(veiculo);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoVeiculoDTO> listarVeiculoPorID(@PathVariable Long id) {
        
        var veiculo = veiculoService.listarVeiculoPorID(id);

        return ResponseEntity.ok(veiculo);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoVeiculoDTO> editarVeiculo(@PathVariable Long id, @RequestBody @Valid DadosEdicaoVeiculoDTO dados){

        var veiculo = veiculoService.editarVeiculo(id, dados);

        return ResponseEntity.ok(veiculo);
    }
    
}
