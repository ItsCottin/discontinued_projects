package br.com.pluri.eventor.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiferencaData {
	
	public DiferencaData(long valor, String unidade) {
        this.valor = valor;
        this.unidade = unidade;
    }
	
	private long valor;
    private String unidade;

}
