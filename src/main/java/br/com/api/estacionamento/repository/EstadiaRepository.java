package br.com.api.estacionamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.api.estacionamento.model.Estadia;
import br.com.api.estacionamento.model.StatusEstadia;

public interface EstadiaRepository extends JpaRepository<Estadia, Long>{

    boolean existsByVeiculoIdAndStatus(Long veiculoId, StatusEstadia status);

}