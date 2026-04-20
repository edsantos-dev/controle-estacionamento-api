package br.com.api.estacionamento.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.api.estacionamento.dto.DadosFaturamentoEstadiaDTO;
import br.com.api.estacionamento.model.Estadia;
import br.com.api.estacionamento.model.StatusEstadia;

public interface EstadiaRepository extends JpaRepository<Estadia, Long>{

    boolean existsByVeiculoIdAndStatus(Long veiculoId, StatusEstadia status);

    Page<Estadia> findByStatus(StatusEstadia statusEstadia, Pageable pageable);

    @Query( """
            SELECT new br.com.api.estacionamento.dto.DadosFaturamentoEstadiaDTO(
                COUNT(e),
                COALESCE(SUM(e.valorFinal), 0)
            )
            FROM Estadia e
            WHERE e.status = :status
            AND e.dataPagamento BETWEEN :inicio AND :fim
            """)
    
    DadosFaturamentoEstadiaDTO calcularFaturamentoPorPeriodo(
        @Param("status") StatusEstadia status,
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim
    );
}