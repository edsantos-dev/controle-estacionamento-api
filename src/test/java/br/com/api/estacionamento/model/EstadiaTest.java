package br.com.api.estacionamento.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.api.estacionamento.exception.RegraNegocioException;

public class EstadiaTest {

    @Test
    @DisplayName("Deve inicializar a estadia com valores padrão corretos no construtor customizado.")
    public void criarEstadiaFalsa(){

        Estadia estadia = new Estadia(new Vaga(), new Veiculo());

        assertNotNull(estadia.getDataEntrada(), "A data de entrada não pode ser nula.");
        assertEquals(BigDecimal.ZERO, estadia.getValorFinal(), "O valor final precisa ser zero.");
        assertEquals(StatusEstadia.ATIVA, estadia.getStatus(), "O status da estadia deve ser ATIVA.");
    }

    @Test
    @DisplayName("Deve calcular o valor até os 15 minutos iniciais, valor final precisa ser o fixo (R$5,00).")
    public void calcularValorFinalCaso1(){

        LocalDateTime dataEntradaFalsa = LocalDateTime.of(2026, 2, 27, 10, 0);
        LocalDateTime dataSaidaFalsa = LocalDateTime.of(2026, 2, 27, 10, 15);

        Estadia estadia = new Estadia(1L, new Vaga(), new Veiculo(), dataEntradaFalsa, dataSaidaFalsa, BigDecimal.ZERO, StatusEstadia.ATIVA);

        BigDecimal valor = estadia.calcularValorFinal();

        assertEquals(BigDecimal.valueOf(5), valor, "O valor final precisa ser 5 reais.");
    }

    @Test
    @DisplayName("Deve devolver o valor excedido mais o valor fixo depois dos 15 minutos iniciais.")
    public void calcularValorFinalCaso2(){

        LocalDateTime dataEntradaFalsa = LocalDateTime.of(2026, 2, 27, 10, 0);
        LocalDateTime dataSaidaFalsa = LocalDateTime.of(2026, 2, 27, 11, 0);

        Estadia estadia = new Estadia(1L, new Vaga(), new Veiculo(), dataEntradaFalsa, dataSaidaFalsa, BigDecimal.ZERO, StatusEstadia.ATIVA);

        BigDecimal valor = estadia.calcularValorFinal();

        BigDecimal valorEsperado = BigDecimal.valueOf(12.50).setScale(2, RoundingMode.HALF_UP);

        assertEquals(valorEsperado, valor, "O cálculo do valor proporcional está incorreto.");
    }

    @Test
    @DisplayName("Deve encerrar a estadia corretamente mudando status e preenchendo valor e data de saída.")
    public void encerrarEstadiaCaso1(){

        Estadia estadia = new Estadia(new Vaga(), new Veiculo());

        estadia.encerrarEstadia();

        assertEquals(StatusEstadia.EM_COBRANCA, estadia.getStatus(), "O status deve ser alterado para EM_COBRANCA.");
        assertNotNull(estadia.getDataSaida(), "A data de saída deve ser preenchida com o momento atual.");
        assertNotNull(estadia.getValorFinal(), "O valor final deve ter sido calculado.");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar encerrar uma estadia que não está ATIVA.")
    public void encerrarEstadiaCaso2(){

        Estadia estadia = new Estadia(1L, new Vaga(), new Veiculo(), LocalDateTime.now(), null, BigDecimal.ZERO, StatusEstadia.EM_COBRANCA);

        RegraNegocioException exception = assertThrows(RegraNegocioException.class, () -> estadia.encerrarEstadia());

        assertEquals("Não é possível encerrar uma estadia que não esteja ATIVA.", exception.getMessage());
    }
}
