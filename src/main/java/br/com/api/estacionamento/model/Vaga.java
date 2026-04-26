package br.com.api.estacionamento.model;

import br.com.api.estacionamento.dto.DadosVagaDTO;
import br.com.api.estacionamento.exception.RegraNegocioException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "vaga")
@Entity

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Vaga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int numero;
    private boolean ocupada;
    private boolean ativa;

    public Vaga(DadosVagaDTO dados) {
        this.numero = dados.numero();
        this.ocupada = false;
        this.ativa = true;
    }

    public void ocupar(){
        if(!estaAtiva()){
            throw new RegraNegocioException("A vaga está desativada. Portanto, não é possível ocupar.");
        }
        if(this.ocupada){
            throw new RegraNegocioException("Vaga já está ocupada.");
        }

        this.ocupada = true;
    }

    public void liberar(){
        if(!estaAtiva()){
            throw new RegraNegocioException("A vaga está desativada. Portanto, não é possível liberar.");
        }
        if(!this.ocupada){
            throw new RegraNegocioException("Vaga já está livre.");
        }

        this.ocupada = false;
    }

    public void desativar(){
        
        if(!this.ativa){
            throw new RegraNegocioException("A vaga já está desativada.");
        }

        this.ativa = false;
    }

    public void ativar(){

        if(this.ativa){
            throw new RegraNegocioException("A vaga já está ativa.");
        }

        this.ativa = true;
    }

    private boolean estaAtiva(){
        return this.ativa;
    }

}
