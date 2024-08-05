package br.com.pluri.eventor.model;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
import br.com.etechoracio.common.model.BaseORM;
import br.com.pluri.eventor.enums.TipoPessoaEnum;
import br.com.pluri.eventor.enums.TipoColaboradorEnum;

@Getter
@Setter
@Entity
@Table(name="TBL_USUARIO")
public class Usuario extends BaseORM {
	
	@Id
	@GeneratedValue
	@Column(name="ID_USUA")
	private Long id;
	
	@Column(name="LOGIN_USUA")
	private String login;
	
	@Column(name="FUNCAO_USUA")
	private String funcao;
	
	@Column(name="AVALIACAO_USUA")
	private float avaliacao;
	
	@Column(name="NOME_USUA")
	private String nome;
	
	@Column(name="CPF_CNPJ_USUA")
	private String cpfCnpj;
	
	@Column(name="EMAIL_USUA")
	private String email;
	
	@Column(name="ENDERECO_USUA")
	private String endereco;
	
	@Column(name="SENHA_USUA")
	private String senha;
	
	@Transient
	private String confirmSenha;
	
	@Transient
	private String oldsenha;
	
	@Enumerated(EnumType.STRING)
	@Column(name="TP_PESSOA_USUA")
	private TipoPessoaEnum tipoPessoa;
	
	@Column(name="QR_CODE_USUA")
	private String qrCode;
	
	@Column(name="TELEFONE_USUA")
	private String telefone;
	
	@Enumerated(EnumType.STRING)
	@Column(name="TP_COLAB_USUA")
	private TipoColaboradorEnum tipoColaborador;
	
	public Usuario(){}

	public Usuario(Long id) {
		this.id = id;
	}
	
	@ManyToMany
	@JoinTable(
			name="TBL_PARTICIPANTE_USUARIO",
			joinColumns=@JoinColumn(name="ID_USUA", referencedColumnName="ID_USUA"),
			inverseJoinColumns=@JoinColumn(name="ID_PART", referencedColumnName="ID_PART"))
	private List<Participante> participantes;
	
	/*@ManyToMany
	@JoinTable(
			name="TBL_MEDALHA_USUARIO",
			joinColumns=@JoinColumn(name="ID_USUA", referencedColumnName="ID_USUA"),
			inverseJoinColumns=@JoinColumn(name="ID_RECOM", referencedColumnName="ID_RECOM"))
	private List<Medalha> medalhas;*/
	
	@OneToMany(mappedBy="usuario")
 	private List<UsuarioNotificacao> usuarioNotificacao;
	
	/*@OneToMany(mappedBy="usuario")
 	private List<UsuarioEvento> usuarioEvento;*/
}
