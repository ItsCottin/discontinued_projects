package br.com.pluri.eventor.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name="TBL_EVENTO")
public class Evento extends BaseORM {
	
	@Id
	@GeneratedValue
	@Column(name="ID_EVEN")
	private Long id;
	
	@Column(name="LOCAL_EVEN")
	private String local;
	
	@Column(name="DATAINICIO_EVEN")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInicio;
	
	@Column(name="DATAFIM_EVEN")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataFim;
	
	@Column(name="TITULO_EVEN")
	private String titulo;
	
	@Column(name="DESCRICAO_EVEN")
	private String descricao;
	
	@Column(name="TWITTER_EVEN")
	private String twitter;
	
	@Column(name="FACEBOOK_EVEN")
	private String facebook;
	
	@Column(name="SITE_EVEN")
	private String site;
	
	@Column(name="CIDADE_EVEN")
	private String cidade;
	
	@Column(name="ESTADO_EVEN")
	private String estado;
	
	@Column(name="VAGAS")
	private int vagas;
	
	@Column(name="RESPONSAVEL_EVEN")
	private String responsavel;

	@Column(name="EMAIL_EVEN")
	private String email;
	
	@Column(name="PRECO_EVEN")
	private String preco;
	
	@Column(name="TELEFONE_EVEN")
	private String telefone;
	
	@JoinColumn(name="ID_USUA", referencedColumnName="ID_USUA")
	@ManyToOne
	private Usuario usuario;
	
	//Atributos transientes
	@Transient
	private Date horaInicio;
	
	@Transient
	private Date horaFim;
	
	@Transient
	private boolean siteProprio;
	
	// relação de evento para demanada (muitos pra 1)
	/*@ManyToOne
	@JoinColumn(name="ID_DEMAN", referencedColumnName="ID_DEMAN")
	private Demanda demanda;*/
	
	
	// relação de Evento para visitantes (muitos pra muitos)
	/*@ManyToMany
	@JoinTable(
			name="TBL_EVENTO_VISITANTE",
			joinColumns=@JoinColumn(name="ID_EVEN", referencedColumnName="ID_EVEN"),
			inverseJoinColumns=@JoinColumn(name="ID_VISIT", referencedColumnName="ID_VISIT"))
	private List<Visitante> visitantes;
	
	// relação de evento para medalha (1 pra muitos)
	@OneToMany(mappedBy="evento")
	private List<Medalha> medalhas;*/
	
	@OneToMany(mappedBy="evento")
	private List<Notificacao> notificacoes;
	
	/*@OneToMany(mappedBy="evento")
 	private List<UsuarioNo> usuarioEvento;*/
	
	@OneToMany(mappedBy="evento")
 	private List<EventoVisitante> eventoVisitante;
	
	@OneToMany(mappedBy="evento")
	private List<Atividade> atividades;

	
	public Evento(){}
	
	public Evento(Long id){
		this.id = id;
	}
}
