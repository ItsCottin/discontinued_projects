package br.com.pluri.eventor.enums;

public enum StatusInscritoEnum {
	
	APROVADO("Aprovado"),
	PENDENTE("Pendente"),
	REPROVADO("Reprovado");
	
	public String status;
	
	StatusInscritoEnum(String opcao) {
		status = opcao;
	}

}
