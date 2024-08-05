package br.com.pluri.eventor.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import br.com.etechoracio.common.model.BaseORM;

@Getter
@Setter
@Entity
@Table(name="State")
public class Estado extends BaseORM {
	
	@Id
	@GeneratedValue
	@Column(name="Id")
	private Long id;
	
	@Column(name="Name")
	private String name;
	
	@Column(name="Acronym")
	private String uf;
	
	@OneToMany(mappedBy="estado")
	private List<Cidade> cidades;
	
}
