package br.com.api.estacionamento.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
import lombok.Setter;

@Table(name = "estadia")
@Entity

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
    
}
