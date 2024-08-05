package br.com.pluri.eventor.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.etechoracio.common.model.BaseORM;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="TBL_VISITANTE")
public class Visitante extends BaseORM {
	
	@Id
	@GeneratedValue
	@Column(name="ID_VISIT")
	private Long id;
	
	@Column(name="NOME_VISIT")
	private String nome;
	
	@Column(name="EMAIL_VISIT")
	private String email;
	
	@Column(name="TELEFONE_VISIT")
	private String telefone;
	
	@Column(name="ENDERECO_RESIDENT_VISIT")
	private String enderecoResidencial;
	
	@Column(name="ENDERECO_COMERC_VISIT")
	private String enderecoComercial;
	
	@Column(name="CARGO_VISIT")
	private String cargo;
	
	@Column(name="EMPRESA_VISIT")
	private String empresa;
	
	@Column(name="DATANASCIMENTO_VISIT")
	private Date dataNascimento;
	
	@Column(name="CPF_VISIT")
	private String cpf;
	
	@Column(name="CNPJ_VISIT")
	private String cnpj;
	
	@OneToMany(mappedBy="visitante")
 	private List<EventoVisitante> eventoVisitante;
}
