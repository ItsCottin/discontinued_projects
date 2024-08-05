package br.com.pluri.eventor.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.etechoracio.common.model.BaseORM;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="TBL_USUARIO_NOTIFICACAO")
public class UsuarioNotificacao extends BaseORM {
	
	@Id
	@GeneratedValue
	@Column(name="ID_USUA_NOTIF")
	private Long id;
	
	@Column(name="VISUALIZADO")
	private boolean visualizado;
	
	@ManyToOne
	@JoinColumn(name="ID_USUA", referencedColumnName="ID_USUA")
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name="ID_NOTIF", referencedColumnName="ID_NOTIF")
	private Notificacao notificacao;
}
