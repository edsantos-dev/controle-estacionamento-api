package br.com.api.estacionamento.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.api.estacionamento.dto.DadosDetalhamentoVagaDTO;
import br.com.api.estacionamento.dto.DadosListagemVagaDTO;
import br.com.api.estacionamento.dto.DadosVagaDTO;
import br.com.api.estacionamento.service.VagaService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.var;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/vagas")
public class VagaController {

    @Autowired
    private VagaService vagaService;
    
    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoVagaDTO> cadastrarVaga(@RequestBody @Valid DadosVagaDTO dados, UriComponentsBuilder uriBuilder) {
        
        var vaga = vagaService.salvarVaga(dados);
        var uri = uriBuilder.path("/vagas/{id}").buildAndExpand(vaga.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoVagaDTO(vaga));
    }

    @GetMapping
    public ResponseEntity<List<DadosListagemVagaDTO>> listarVaga() {
        
        var listaVaga = vagaService.listarVagas();
        
        return ResponseEntity.ok().body(listaVaga); 
    }
    
    
}
