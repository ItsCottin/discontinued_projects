package br.com.pluri.eventor.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.etechoracio.common.model.BaseORM;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="TBL_NOTIFICACAO")
public class Notificacao extends BaseORM {
	
	@Id
	@GeneratedValue
	@Column(name="ID_NOTIF")
	private Long id;
	
	@Column(name="LINK_NOTIF")
	private String link;
	
	@Column(name="TITULO_NOTIF")
	private String titulo;
	
	@Column(name="DESCRICAO_NOTIF")
	private String descricao;
	
	@ManyToOne
	@JoinColumn(name="ID_EVEN", referencedColumnName="ID_EVEN")
	private Evento evento;
	
	/*@ManyToOne
	@JoinColumn(name="ID_DEMAN", referencedColumnName="ID_DEMAN")
	private Demanda demanda;*/
	
	@OneToMany(mappedBy="notificacao")
 	private List<UsuarioNotificacao> usuarioNotificacao;
}
