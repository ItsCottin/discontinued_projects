package br.com.pluri.eventor.business.exception;

import br.com.etechoracio.common.view.MessageBundleLoader;

public class UsuarioNaoAutenticadoException extends Exception {

	public UsuarioNaoAutenticadoException(){
		super(MessageBundleLoader.getMessage("critica.usuarioinvalido"));
	}
}
