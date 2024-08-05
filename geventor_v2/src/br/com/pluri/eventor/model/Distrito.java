package br.com.pluri.eventor.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name="districts")
public class Distrito extends BaseORM {
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;
	
	@Column(name="name")
	private String bairro;
	
	@Column(name="slug")
	private String slug;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="city_id", referencedColumnName="id", nullable=false, insertable=false, updatable=false)
	private Cidade cidade;
	
	//@OneToMany(mappedBy = "distrito")
	//private List<Endereco> endereco;

}
