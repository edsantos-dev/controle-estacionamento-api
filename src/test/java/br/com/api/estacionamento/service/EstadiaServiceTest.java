package br.com.api.estacionamento.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.com.api.estacionamento.dto.DadosFaturamentoEstadiaDTO;
import br.com.api.estacionamento.dto.DadosGeracaoDeCobrancaEstadiaDTO;
import br.com.api.estacionamento.dto.DadosListagemEstadiaDTO;
import br.com.api.estacionamento.dto.DadosQuitacaoEstadiaDTO;
import br.com.api.estacionamento.exception.RecursoNaoEncontradoException;
import br.com.api.estacionamento.exception.ValidacaoDeDadosException;
import br.com.api.estacionamento.model.Estadia;
import br.com.api.estacionamento.model.StatusEstadia;
import br.com.api.estacionamento.model.Tipo;
import br.com.api.estacionamento.model.Vaga;
import br.com.api.estacionamento.model.Veiculo;
import br.com.api.estacionamento.repository.EstadiaRepository;
import br.com.api.estacionamento.repository.VagaRepository;
import br.com.api.estacionamento.repository.VeiculoRepository;

@ExtendWith(MockitoExtension.class)
public class EstadiaServiceTest {
    
    @Mock
    private VagaRepository vagaRepository;

    @Mock
    private VeiculoRepository veiculoRepository;
    
    @Mock
    private EstadiaRepository estadiaRepository;

    @InjectMocks
    @Autowired
    EstadiaService estadiaService;
    
    @Test
    @DisplayName("Deve encerrar estadia com sucesso e passar para o status EM_COBRANCA quando os dados forem válidos.")
    void gerarCobrancaCaso1(){
        
        Long idEstadia = 1L;

        Vaga vagaFalsa = new Vaga(1L, 1, false);
        Veiculo veiculoFalso = new Veiculo(1L, "AKY0U876", Tipo.CARRO);

        Estadia estadiaFalsa = new Estadia(vagaFalsa, veiculoFalso);

        when(estadiaRepository.findById(idEstadia)).thenReturn(Optional.of(estadiaFalsa));

        DadosGeracaoDeCobrancaEstadiaDTO resultado = estadiaService.gerarCobranca(idEstadia);

        assertEquals(StatusEstadia.EM_COBRANCA, estadiaFalsa.getStatus());
        assertNotNull(resultado, "o DTO retornado não deveria ser nulo.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando o ID de estadia não existir.")
    void gerarCobrancaCaso2(){

        Long idEstadiaInexistente = 99L;

        when(estadiaRepository.findById(idEstadiaInexistente)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class, () -> estadiaService.gerarCobranca(idEstadiaInexistente));

        assertEquals("Estadia não encontrada.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve quitar estadia com sucesso e passar para o status ENCERRADA quando os dados forem válidos.")
    void quitarEstadiaCaso1(){

        Long idEstadia = 1L;
        LocalDateTime dataEntradaFalsa = LocalDateTime.of(2026, 2, 27, 10, 0);
        LocalDateTime dataSaidaFalsa = LocalDateTime.of(2026, 2, 27, 11, 0);

        Estadia estadiaFalsa = new Estadia(1L, new Vaga(), new Veiculo(), dataEntradaFalsa, dataSaidaFalsa, null, BigDecimal.valueOf(12.50), StatusEstadia.EM_COBRANCA);

        when(estadiaRepository.findById(idEstadia)).thenReturn(Optional.of(estadiaFalsa));

        DadosQuitacaoEstadiaDTO resultado = estadiaService.quitarEstadia(idEstadia);

        assertNotNull(resultado, "O DTO retornado não deveria ser nulo.");
        assertEquals(StatusEstadia.ENCERRADA, estadiaFalsa.getStatus(), "O status da estadia deveria ser ENCERRADA após a quitação.");

        verify(estadiaRepository, times(1)).findById(idEstadia);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar quitar uma estadia que não existe.")
    void quitarEstadiaCaso2(){

        Long idEstadiaInexistente = 99L;

        when(estadiaRepository.findById(idEstadiaInexistente)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class, () -> estadiaService.quitarEstadia(idEstadiaInexistente));

        assertEquals("Estadia não encontrada.", exception.getMessage());

        verify(estadiaRepository, times(1)).findById(idEstadiaInexistente);
    }

    @Test
    @DisplayName("Deve listar todas as estadias paginadas quando o status for nulo.")
    void listarEstadiaCaso1(){

        Pageable paginacao = PageRequest.of(0, 10);
        Estadia estadiaFalsa = new Estadia(new Vaga(), new Veiculo());

        Page<Estadia> paginaFalsa = new PageImpl<>(List.of(estadiaFalsa));

        when(estadiaRepository.findAll(paginacao)).thenReturn(paginaFalsa);

        Page<DadosListagemEstadiaDTO> resultado = estadiaService.listarEstadia(null, paginacao);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());

        verify(estadiaRepository).findAll(paginacao);
        verify(estadiaRepository, never()).findByStatus(any(), any());
    }

    @Test
    @DisplayName("Deve filtrar as estadias paginadas quando o status for informado.")
    void listarEstadiaCaso2(){

        Pageable paginacao = PageRequest.of(0,10);
        Estadia estadiaFalsa = new Estadia(new Vaga(), new Veiculo());
        StatusEstadia statusFalso = StatusEstadia.ATIVA;

        Page<Estadia> paginaFalsa = new PageImpl<>(List.of(estadiaFalsa));

        when(estadiaRepository.findByStatus(statusFalso, paginacao)).thenReturn(paginaFalsa);

        Page<DadosListagemEstadiaDTO> resultado = estadiaService.listarEstadia(statusFalso, paginacao);

        assertNotNull(resultado);
        verify(estadiaRepository).findByStatus(statusFalso, paginacao);
        verify(estadiaRepository, never()).findAll(paginacao);
    }

    @Test
    @DisplayName("Deve obter relatório de faturamento com datas ajustadas para os limites do dia")
    void obterRelatorioFaturamentoCaso1(){

        LocalDate inicioFalso = LocalDate.of(2026, 4, 1);
        LocalDate fimFalso = LocalDate.of(2026, 4, 10);

        DadosFaturamentoEstadiaDTO relatorioEsperado = new DadosFaturamentoEstadiaDTO(15L, new BigDecimal("750.50"));

        when(estadiaRepository.calcularFaturamentoPorPeriodo(eq(StatusEstadia.ENCERRADA), eq(inicioFalso.atStartOfDay()), eq(fimFalso.atTime(LocalTime.MAX)))).thenReturn(relatorioEsperado);

        DadosFaturamentoEstadiaDTO resultado = estadiaService.obterRelatorioFaturamento(inicioFalso, fimFalso);

        assertEquals(relatorioEsperado, resultado, "O relatório resultante precisa ser como o esperado.");
        verify(estadiaRepository).calcularFaturamentoPorPeriodo(eq(StatusEstadia.ENCERRADA), eq(inicioFalso.atStartOfDay()), eq(fimFalso.atTime(LocalTime.MAX)));
    }

    @Test
    @DisplayName("Deve lançar ValidacaoDeDadosException quando a data de início for maior que a data de fim")
    void obterRelatorioFaturamentoCaso2(){

        LocalDate inicioFalso = LocalDate.of(2026, 4, 10);
        LocalDate fimFalso = LocalDate.of(2026, 4, 1);

        ValidacaoDeDadosException exception = assertThrows(ValidacaoDeDadosException.class, () -> estadiaService.obterRelatorioFaturamento(inicioFalso, fimFalso));

        assertEquals("A data de fim não pode ser menor que a de início.", exception.getMessage());

        verifyNoInteractions(estadiaRepository);
    }
}
