package br.com.api.estacionamento.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

import br.com.api.estacionamento.exception.RegraNegocioException;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "estadia")
@Entity

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Estadia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vaga_id")
    private Vaga vaga;

    @ManyToOne
    @JoinColumn(name = "veiculo_id")
    private Veiculo veiculo;

    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;
    private BigDecimal valorFinal;

    @Enumerated(EnumType.STRING)
    StatusEstadia status;

    public Estadia(Vaga vaga, Veiculo veiculo){
        this.vaga = vaga;
        this.veiculo = veiculo;
        this.dataEntrada = LocalDateTime.now();
        this.valorFinal = BigDecimal.ZERO;
        this.status = StatusEstadia.ATIVA;
    }

    public BigDecimal calcularValorFinal(){

        var diferenca = Duration.between(this.dataEntrada, this.dataSaida);
        long minutosTotais = diferenca.toMinutes();

        BigDecimal valorFixo = BigDecimal.valueOf(5);

        if (minutosTotais <= 15){
            return valorFixo;
        }

        Long minutosExcedidos = minutosTotais - 15;

        BigDecimal valorPorMinuto = BigDecimal.valueOf(10)
        .divide(BigDecimal.valueOf(60), 6, RoundingMode.HALF_UP);

        BigDecimal valorExcedido = valorPorMinuto
        .multiply(BigDecimal.valueOf(minutosExcedidos));
        
        return valorFixo.add(valorExcedido).setScale(2, RoundingMode.HALF_UP);
    }
    
    public void encerrarEstadia(){

        if(this.status != StatusEstadia.ATIVA){
            throw new RegraNegocioException("Não é possível encerrar uma estadia que não esteja ATIVA.");
        }

        this.dataSaida = LocalDateTime.now();
        this.valorFinal = calcularValorFinal();
        this.status = StatusEstadia.EM_COBRANCA;
    }
    
}
