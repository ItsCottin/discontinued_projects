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
@Table(name="address_searchs")
public class Endereco extends BaseORM {

	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;
	
	@Column(name="district_id")
	private Long idDistrict;
	
	@Column(name="city_id")
	private Long idCity;
		
	@Column(name="address")
	private String endereco;
		
	@Column(name="postal_code")
	private String cep;
		
	@Column(name="latitude")
	private String latitude;
		
	@Column(name="longitude")
	private String longitude;
		
	@Column(name="ddd")
	private int ddd;
	
	
	//@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	//@JoinColumn(name="district_id", referencedColumnName="id", nullable=false, insertable=false, updatable=false)
	//public Distrito distrito;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="city_id", referencedColumnName="id", nullable=false, insertable=false, updatable=false)
	public Cidade cidade;
	
}
