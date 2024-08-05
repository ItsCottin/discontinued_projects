package br.com.pluri.eventor.enums;

public enum TipoNotificacaoEnum {
	
	ATIVIDADE("Atividade"),
	EVENTO("Evento");
	
	public String tipo;
	
	TipoNotificacaoEnum(String opcao) {
		tipo = opcao;
	}

}
