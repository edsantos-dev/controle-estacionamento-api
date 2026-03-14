package br.com.api.estacionamento.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.api.estacionamento.dto.DadosDetalhamentoEstadiaDTO;
import br.com.api.estacionamento.dto.DadosGeracaoDeCobrancaEstadiaDTO;
import br.com.api.estacionamento.dto.DadosQuitacaoEstadiaDTO;
import br.com.api.estacionamento.dto.DadosEstadiaDTO;
import br.com.api.estacionamento.service.EstadiaService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/estadias")
public class EstadiaController {

    @Autowired
    private EstadiaService estadiaService;

    @PostMapping
    public ResponseEntity<DadosDetalhamentoEstadiaDTO> iniciarEstadia(@RequestBody @Valid DadosEstadiaDTO dados, UriComponentsBuilder uriBuilder) {
        
        var estadia = estadiaService.iniciarEstadia(dados);
        var uri = uriBuilder.path("/estadias/{id}").buildAndExpand(estadia.id()).toUri();

        return ResponseEntity.created(uri).body(estadia);
    }
    
    @PatchMapping("/{id}/cobranca")
    public ResponseEntity<DadosGeracaoDeCobrancaEstadiaDTO> gerarCobranca(@PathVariable Long id){

        var estadia = estadiaService.gerarCobranca(id);

        return ResponseEntity.ok(estadia);
    }

    @PatchMapping("/{id}/quitacao")
    public ResponseEntity<DadosQuitacaoEstadiaDTO> quitarEstadia(@PathVariable Long id){

        var estadia = estadiaService.quitarEstadia(id);

        return ResponseEntity.ok(estadia);
    }
}
