package br.com.api.estacionamento.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.api.estacionamento.model.Estadia;
import br.com.api.estacionamento.model.StatusEstadia;

public interface EstadiaRepository extends JpaRepository<Estadia, Long>{

    boolean existsByVeiculoIdAndStatus(Long veiculoId, StatusEstadia status);

    Page<Estadia> findByStatus(StatusEstadia statusEstadia, Pageable pageable);
}