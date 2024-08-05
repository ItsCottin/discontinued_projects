package br.com.pluri.eventor.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import br.com.etechoracio.common.model.BaseORM;

@Getter
@Setter
@Entity
@Table(name="TBL_EVENTO_VISITANTE")
public class EventoVisitante extends BaseORM {
	
	@Id
	@GeneratedValue
	@Column(name="ID_EVEN_VISIT")
	private Long id;
	
	@Column(name="PRESENCA_COLAB")
	private String presenca;
	
	@ManyToOne
	@JoinColumn(name="ID_EVEN", referencedColumnName="ID_EVEN")
	private Evento evento;
	
	@ManyToOne
	@JoinColumn(name="ID_VISIT", referencedColumnName="ID_VISIT")
	private Visitante visitante;
}
