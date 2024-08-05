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
@Table(name="TBL_G_USUARIO_ATIVIDADE")
public class UsuarioAtividade extends BaseORM {
	
	
	@Id
	@GeneratedValue
	@Column(name="ID_USUA_ATIV")
	private Long id;
	
	@Column(name="STATUS")
	private String status;
	
	@ManyToOne
	@JoinColumn(name="ID_USUA", referencedColumnName="ID_USUA")
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name="ID_ATIVI", referencedColumnName="ID_ATIVI")
	private Atividade atividade;
}
