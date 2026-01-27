package br.com.api.estacionamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.api.estacionamento.model.Veiculo;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long>{

    boolean existsByPlaca(String placa);
    
}
