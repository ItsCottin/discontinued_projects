package br.com.pluri.eventor.enums;

public enum TipoAtividadeEnum {
	
	COMUM("Comum"),
	GERENCIA("Gerencia");
	
	public String tipo;
	
	TipoAtividadeEnum(String opcao) {
		tipo = opcao;
	}
}
