package br.com.pluri.eventor.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import br.com.etechoracio.common.model.BaseORM;
import br.com.pluri.eventor.enums.TipoParticipanteEnum;

@Getter
@Setter
@Entity
@Table(name="TBL_PARTICIPANTE")
public class Participante extends BaseORM {

	@Id
	@GeneratedValue
	@Column(name="ID_PART")
	private Long id;
	
	@Column(name="TIPO_PART")
	@Enumerated(EnumType.STRING)
	private TipoParticipanteEnum tipoParticipante;
	
	@ManyToMany
	@JoinTable(
			name="TBL_PARTICIPANTE_USUARIO",
			joinColumns=@JoinColumn(name="ID_PART", referencedColumnName="ID_PART"),
			inverseJoinColumns=@JoinColumn(name="ID_USUA", referencedColumnName="ID_USUA"))
	private List<Usuario> usuarios;
}
