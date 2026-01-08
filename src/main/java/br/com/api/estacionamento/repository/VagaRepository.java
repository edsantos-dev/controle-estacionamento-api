package br.com.api.estacionamento.repository;

import br.com.api.estacionamento.model.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VagaRepository extends JpaRepository<Vaga, Long> {

    boolean existsByNumero(int numero);

}
