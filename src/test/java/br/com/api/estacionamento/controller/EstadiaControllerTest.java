package br.com.api.estacionamento.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import br.com.api.estacionamento.dto.DadosGeracaoDeCobrancaEstadiaDTO;
import br.com.api.estacionamento.dto.DadosListagemEstadiaDTO;
import br.com.api.estacionamento.dto.DadosQuitacaoEstadiaDTO;
import br.com.api.estacionamento.exception.RecursoNaoEncontradoException;
import br.com.api.estacionamento.model.StatusEstadia;
import br.com.api.estacionamento.service.EstadiaService;

@WebMvcTest(EstadiaController.class)
public class EstadiaControllerTest {

    @Autowired
    private MockMvcTester mockMvc;

    @MockitoBean
    private EstadiaService estadiaService;

    @Test
    @DisplayName("Deve retornar 200 OK e os dados ao encerrar estadia.")
    public void gerarCobrancaCaso1(){

        Long idEstadiaFalsa = 1L;

        DadosGeracaoDeCobrancaEstadiaDTO dtoFalso = new DadosGeracaoDeCobrancaEstadiaDTO(
            idEstadiaFalsa,
            1,
            "ABC123",
            LocalDateTime.of(2026, 1, 1, 10, 0),
            LocalDateTime.of(2026, 1, 1, 11, 0),
            BigDecimal.valueOf(12.50),
            StatusEstadia.EM_COBRANCA
        );

        when(estadiaService.gerarCobranca(idEstadiaFalsa)).thenReturn(dtoFalso);

        mockMvc.patch().uri("/estadias/{id}/cobranca", idEstadiaFalsa)
               .contentType(MediaType.APPLICATION_JSON)
               .exchange()
               .assertThat()
               .hasStatusOk()
               .bodyJson()
               .convertTo(DadosGeracaoDeCobrancaEstadiaDTO.class)
               .satisfies(dtoRetornado -> {
                    assertThat(dtoRetornado)
                        .usingRecursiveComparison()
                        .isEqualTo(dtoFalso);
                });

        verify(estadiaService, times(1)).gerarCobranca(idEstadiaFalsa);
    }

    @Test
    @DisplayName("Deve retornar status 404 Not Found quando a estadia não existir.")
    public void gerarCobrancaCaso2(){

        long idEstadiaInexistente = 99L;

        when(estadiaService.gerarCobranca(idEstadiaInexistente)).thenThrow(new RecursoNaoEncontradoException("Estadia não encontrada."));

        mockMvc.patch().uri("/estadias/{id}/cobranca", idEstadiaInexistente)
               .contentType(MediaType.APPLICATION_JSON)
               .exchange()
               .assertThat()
               .hasStatus(HttpStatus.NOT_FOUND);

        verify(estadiaService, times(1)).gerarCobranca(idEstadiaInexistente);
    }

    @Test
    @DisplayName("Deve retornar 200 OK e os dados ao quitar estadia.")
    public void quitarEstadiaCaso1(){

        Long idEstadiaFalsa = 1L;

        DadosQuitacaoEstadiaDTO dtoFalso = new DadosQuitacaoEstadiaDTO(
            idEstadiaFalsa, 
            1, 
            "ABC123", 
            LocalDateTime.of(2026, 1, 1, 10, 0), 
            LocalDateTime.of(2026, 1, 1, 11, 0), 
            LocalDateTime.of(2026, 1, 1, 11, 15), 
            BigDecimal.valueOf(12.50), 
            StatusEstadia.ENCERRADA
        );

        when(estadiaService.quitarEstadia(idEstadiaFalsa)).thenReturn(dtoFalso);

        mockMvc.patch().uri("/estadias/{id}/quitacao", idEstadiaFalsa)
        .contentType(MediaType.APPLICATION_JSON)
        .exchange()
        .assertThat()
        .hasStatusOk()
        .bodyJson()
        .convertTo(DadosQuitacaoEstadiaDTO.class)
        .satisfies(dtoRetornado -> {
                assertThat(dtoRetornado)
                .usingRecursiveComparison()
                .isEqualTo(dtoFalso);
            }
        );

        verify(estadiaService, times(1)).quitarEstadia(idEstadiaFalsa);
    }

    @Test
    @DisplayName("Deve retornar status 404 Not Found quando a estadia não existir ao tentar quitar.")
    public void quitarEstadiaCaso2(){

        Long idEntidadeInexistente = 99L;

        when(estadiaService.quitarEstadia(idEntidadeInexistente)).thenThrow(new RecursoNaoEncontradoException("Estadia não encontrada."));

        mockMvc.patch().uri("/estadias/{id}/quitacao", idEntidadeInexistente)
        .contentType(MediaType.APPLICATION_JSON)
        .exchange()
        .assertThat()
        .hasStatus(HttpStatus.NOT_FOUND);

        verify(estadiaService, times(1)).quitarEstadia(idEntidadeInexistente);
    }

    @Test
    @DisplayName("Deve retornar 200 OK e a página com todas as estadias quando não passar filtro.")
    public void listarEstadiaCaso1(){

        DadosListagemEstadiaDTO dtoFalso = new DadosListagemEstadiaDTO(
            1L,
            1,
            "ABC123",
            LocalDateTime.now(),
            null,
            null,
            BigDecimal.ZERO,
            StatusEstadia.ATIVA
        );

        Page<DadosListagemEstadiaDTO> paginaFalsa = new PageImpl<>(List.of(dtoFalso));

        when(estadiaService.listarEstadia(isNull(), any(Pageable.class))).thenReturn(paginaFalsa);

        var validacaoJson = mockMvc.get().uri("/estadias")
            .contentType(MediaType.APPLICATION_JSON)
            .exchange()
            .assertThat()
            .hasStatusOk()
            .bodyJson();
        
        validacaoJson.extractingPath("$.content[0].veiculoPlaca").asString().isEqualTo("ABC123");
        validacaoJson.extractingPath("$.totalElements").asNumber().isEqualTo(1);

        verify(estadiaService, times(1)).listarEstadia(isNull(), any(Pageable.class));
    }

    @Test
    @DisplayName("Deve retornar 200 OK e a página filtrada quando passar o status na URL.")
    public void listarEstadiaCaso2(){

        StatusEstadia statusFalso = StatusEstadia.ENCERRADA;

        DadosListagemEstadiaDTO dtoFalso = new DadosListagemEstadiaDTO(
            2L,
            2,
            "XYZ456",
            LocalDateTime.of(2026, 1, 1, 10, 0), 
            LocalDateTime.of(2026, 1, 1, 11, 0), 
            LocalDateTime.of(2026, 1, 1, 11, 15),
            BigDecimal.valueOf(12.50),
            statusFalso
        );

        Page<DadosListagemEstadiaDTO> paginaFalsa = new PageImpl<>(List.of(dtoFalso));

        when(estadiaService.listarEstadia(eq(statusFalso), any(Pageable.class))).thenReturn(paginaFalsa);

        var verificacaoJson = mockMvc.get().uri("/estadias?status={status}", statusFalso)
            .contentType(MediaType.APPLICATION_JSON)
            .exchange()
            .assertThat()
            .hasStatusOk()
            .bodyJson();

        verificacaoJson.extractingPath("$.content[0].veiculoPlaca").asString().isEqualTo("XYZ456");
        verificacaoJson.extractingPath("$.content[0].status").asString().isEqualTo("ENCERRADA");
        verificacaoJson.extractingPath("$.totalElements").asNumber().isEqualTo(1);

        verify(estadiaService, times(1)).listarEstadia(eq(statusFalso), any(Pageable.class));
    }
}
