package br.com.pluri.eventor.business.exception;

public class UsuarioNaoAutenticadoException extends Exception {

	public UsuarioNaoAutenticadoException(){
		super("Usuario ou senha inválida.");
	}
}
