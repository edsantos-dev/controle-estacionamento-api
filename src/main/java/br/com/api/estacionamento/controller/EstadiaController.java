package br.com.api.estacionamento.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.api.estacionamento.dto.DadosIniciacaoEstadiaDTO;
import br.com.api.estacionamento.dto.DadosGeracaoDeCobrancaEstadiaDTO;
import br.com.api.estacionamento.dto.DadosListagemEstadiaDTO;
import br.com.api.estacionamento.dto.DadosQuitacaoEstadiaDTO;
import br.com.api.estacionamento.model.StatusEstadia;
import br.com.api.estacionamento.dto.DadosEstadiaDTO;
import br.com.api.estacionamento.dto.DadosFaturamentoEstadiaDTO;
import br.com.api.estacionamento.service.EstadiaService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/estadias")
public class EstadiaController {

    @Autowired
    private EstadiaService estadiaService;

    @PostMapping
    public ResponseEntity<DadosIniciacaoEstadiaDTO> iniciarEstadia(@RequestBody @Valid DadosEstadiaDTO dados, UriComponentsBuilder uriBuilder) {
        
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

    @GetMapping
    public ResponseEntity<Page<DadosListagemEstadiaDTO>> listarEstadia(
        @RequestParam (required = false) StatusEstadia status, 
        @PageableDefault (size = 10, sort = {"dataEntrada"}) Pageable paginacao) {
        
        var page = estadiaService.listarEstadia(status, paginacao);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/faturamento")
    public ResponseEntity<DadosFaturamentoEstadiaDTO> relatorioFaturamento(
        @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
        @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        
        var relatorio = estadiaService.obterRelatorioFaturamento(inicio, fim);

        return ResponseEntity.ok(relatorio);
    }
}
