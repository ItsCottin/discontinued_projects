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

@Setter
@Getter
@Entity
@Table(name="City")
public class Cidade extends BaseORM {
	
	@Id
	@GeneratedValue
	@Column(name="Id")
	private Long id;
	
	@Column(name="Name")
	private String name;
	
	@ManyToOne
	@JoinColumn(name="IdState", referencedColumnName="Id")
	private Estado estado;
}
