package br.com.api.estacionamento.exception;

public class ValidacaoDeDadosException extends RuntimeException{
    
    public ValidacaoDeDadosException(String mensagem){
        super(mensagem);
    }
}
