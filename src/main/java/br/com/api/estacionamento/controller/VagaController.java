package br.com.api.estacionamento.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.api.estacionamento.dto.DadosDetalhamentoVagaDTO;
import br.com.api.estacionamento.dto.DadosListagemVagaDTO;
import br.com.api.estacionamento.dto.DadosVagaDTO;
import br.com.api.estacionamento.service.VagaService;
import jakarta.validation.Valid;
import lombok.var;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/vagas")
public class VagaController {

    @Autowired
    private VagaService vagaService;
    
    @PostMapping
    public ResponseEntity<DadosDetalhamentoVagaDTO> cadastrarVaga(@RequestBody @Valid DadosVagaDTO dados, UriComponentsBuilder uriBuilder) {
        
        var vaga = vagaService.salvarVaga(dados);
        var uri = uriBuilder.path("/vagas/{id}").buildAndExpand(vaga.id()).toUri();

        return ResponseEntity.created(uri).body(vaga);
    }

    @GetMapping
    public ResponseEntity<List<DadosListagemVagaDTO>> listarVaga() {
        
        var listaVaga = vagaService.listarVagas();
        
        return ResponseEntity.ok().body(listaVaga); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoVagaDTO> listarVagaPorId(@PathVariable long id) {
        
        var vaga = vagaService.listarPorId(id);

        return ResponseEntity.ok(vaga);
    }
    
    @PatchMapping("{id}/ocupar")
    public ResponseEntity<DadosDetalhamentoVagaDTO> ocuparVaga(@PathVariable Long id){

        var vaga = vagaService.ocuparVaga(id);

        return ResponseEntity.ok(vaga);
    }
    
    @PatchMapping("{id}/liberar")
    public ResponseEntity<DadosDetalhamentoVagaDTO> liberarVaga(@PathVariable Long id){

        var vaga = vagaService.liberarVaga(id);

        return ResponseEntity.ok(vaga);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletarVaga(@PathVariable Long id){

        vagaService.deletarVaga(id);

        return ResponseEntity.noContent().build();
    }
    
}
