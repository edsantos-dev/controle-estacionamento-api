package br.com.api.estacionamento.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.api.estacionamento.dto.DadosListagemVagaDTO;
import br.com.api.estacionamento.dto.DadosVagaDTO;
import br.com.api.estacionamento.repository.VagaRepository;
import br.com.api.estacionamento.service.VagaService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/vagas")
public class VagaController {

    @Autowired
    private VagaRepository vagaRepository;

    @Autowired
    private VagaService vagaService;
    
    @PostMapping
    public void cadastrarVaga(@RequestBody DadosVagaDTO dados) {
        
        vagaService.salvarVaga(dados);
    }

    @GetMapping
    public List<DadosListagemVagaDTO> listarVaga() {
        return vagaRepository.findAll().stream().map(DadosListagemVagaDTO::new).toList();
    }
    
    
}
