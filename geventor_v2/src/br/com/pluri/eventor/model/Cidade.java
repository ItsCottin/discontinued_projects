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

import lombok.Getter;
import lombok.Setter;
import br.com.etechoracio.common.model.BaseORM;

@Setter
@Getter
@Entity
@Table(name="cities")
public class Cidade extends BaseORM {
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="slug")
	private String slug;
	
	@ManyToOne
	@JoinColumn(name="state_id", referencedColumnName="id")
	public Estado estado;
	
	@OneToMany(mappedBy = "cidade")
	public List<Endereco> endereco;
	
	@OneToMany(mappedBy = "cidade")
	public List<Distrito> distrito;
}
