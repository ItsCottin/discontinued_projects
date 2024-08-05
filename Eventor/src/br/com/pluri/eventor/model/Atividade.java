package br.com.pluri.eventor.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.com.etechoracio.common.model.BaseORM;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="TBL_ATIVIDADE")
public class Atividade extends BaseORM {
	
	@Id
	@GeneratedValue
	@Column(name="ID_ATIVI")
	private Long id;
	
	@Column(name="NOME_ATIVI")
	private String nome;
	
	@Column(name="DATAINICIO_ATIVI")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInicio; 
	
	@Column(name="DATAFIM_ATIVI")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataFim;
	
	@Column(name="DETALHES_ATIVI")
	private String detalhes;
	
	@Column(name="ORGANIZACAO_ATIVI")
	private String organizacao;
	
	@Column(name="VAGAS_ATIVI")
	private int vagas;
	
	@Column(name="PRECO_ATIVI")
	private String preco;
	
	//Atributos transientes
	@Transient
	private Date horaInicio;
		
	@Transient
	private Date horaFim;
	
	// relação de atividade para demanda (muitos pra muitos)
	/*@ManyToMany
	@JoinTable(
			name="TBL_DEMANDA_ATIVIDADE_USUARIO",
			joinColumns=@JoinColumn(name="ID_ATIVI", referencedColumnName="ID_ATIVI"),
			inverseJoinColumns=@JoinColumn(name="ID_DEMAN", referencedColumnName="ID_DEMAN"))
	private List<Demanda> demandas;*/
	@ManyToOne
	@JoinColumn(name="ID_EVEN", referencedColumnName="ID_EVEN")
	private Evento evento;
	
	public Atividade(){}
	
	public Atividade(Long id){
		this.id = id;
	}
}
