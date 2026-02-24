package br.com.api.estacionamento.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.api.estacionamento.dto.DadosEncerramentoEstadiaDTO;
import br.com.api.estacionamento.exception.RecursoNaoEncontradoException;
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
    void encerrarEstadiaCaso1(){
        
        Long idEstadia = 1L;

        Vaga vagaFalsa = new Vaga(1L, 1, false);
        Veiculo veiculoFalso = new Veiculo(1L, "AKY0U876", Tipo.CARRO);

        Estadia estadiaFalsa = new Estadia(vagaFalsa, veiculoFalso);

        when(estadiaRepository.findById(idEstadia)).thenReturn(Optional.of(estadiaFalsa));

        DadosEncerramentoEstadiaDTO resultado = estadiaService.encerrarEstadia(idEstadia);

        assertEquals(StatusEstadia.EM_COBRANCA, estadiaFalsa.getStatus());
        assertNotNull(resultado, "o DTO retornado não deveria ser nulo.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando o ID de estadia não existir.")
    void encerrarEstadiaCaso2(){

        Long idEstadiaInexistente = 99L;

        when(estadiaRepository.findById(idEstadiaInexistente)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class, () -> estadiaService.encerrarEstadia(idEstadiaInexistente));

        assertEquals("Estadia não encontrada.", exception.getMessage());
    }
}
