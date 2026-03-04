package br.com.api.estacionamento.controller;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import br.com.api.estacionamento.dto.DadosEncerramentoEstadiaDTO;
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
    public void encerrarEstadiaCaso1(){

        Long idEstadiaFalsa = 1L;

        DadosEncerramentoEstadiaDTO dtoFalso = new DadosEncerramentoEstadiaDTO(
            idEstadiaFalsa,
            1,
            "ABC123",
            LocalDateTime.of(2026, 1, 1, 10, 0),
            LocalDateTime.of(2026, 1, 1, 11, 0),
            BigDecimal.valueOf(12.50),
            StatusEstadia.EM_COBRANCA
        );

        when(estadiaService.encerrarEstadia(idEstadiaFalsa)).thenReturn(dtoFalso);

        mockMvc.patch().uri("/estadias/{id}", idEstadiaFalsa)
               .contentType(MediaType.APPLICATION_JSON)
               .exchange()
               .assertThat()
               .hasStatusOk()
               .bodyJson()
               .convertTo(DadosEncerramentoEstadiaDTO.class)
               .satisfies(dtoRetornado -> {
                    assertThat(dtoRetornado)
                        .usingRecursiveComparison()
                        .isEqualTo(dtoFalso);
                });

        verify(estadiaService, times(1)).encerrarEstadia(idEstadiaFalsa);
    }

    @Test
    @DisplayName("Deve retornar status 404 Not Found quando a estadia não existir.")
    public void encerrarEstadiaCaso2(){

        long idEstadiaInexistente = 99L;

        when(estadiaService.encerrarEstadia(idEstadiaInexistente)).thenThrow(new RecursoNaoEncontradoException("Estadia não encontrada."));

        mockMvc.patch().uri("/estadias/{id}", idEstadiaInexistente)
               .contentType(MediaType.APPLICATION_JSON)
               .exchange()
               .assertThat()
               .hasStatus(HttpStatus.NOT_FOUND);

        verify(estadiaService, times(1)).encerrarEstadia(idEstadiaInexistente);
    }
}
