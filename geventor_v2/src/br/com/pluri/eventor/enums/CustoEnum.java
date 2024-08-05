package br.com.pluri.eventor.enums;

public enum CustoEnum {
	
	PAGO("Pago"),
	GRATUITO("Gratuito");
	
	public String custo;
	
	CustoEnum(String opcao) {
		custo = opcao;
	}

}
