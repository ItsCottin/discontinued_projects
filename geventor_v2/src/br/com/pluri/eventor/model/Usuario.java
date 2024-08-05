package br.com.pluri.eventor.model;


import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
import br.com.etechoracio.common.model.BaseORM;
import br.com.pluri.eventor.enums.TipoPessoaEnum;
import br.com.pluri.eventor.enums.TipoUsuarioEnum;

@Getter
@Setter
@Entity
@Table(name="TBL_G_USUARIO")
public class Usuario extends BaseORM {
	
	@Id
	@GeneratedValue
	@Column(name="ID_USUA")
	private Long id;
	
	@Column(name="LOGIN_USUA")
	private String login;
	
	@Column(name="NOME_USUA")
	private String nome;
	
	@Column(name="CPF_CNPJ_USUA")
	private String cpfCnpj;
	
	@Column(name="EMAIL_USUA")
	private String email;
	
	@Column(name="ENDERECO_USUA")
	private String endereco;
	
	@Column(name="NUMERO_CASA_USUA")
	private String nrCasa;
	
	@Column(name="CIDADE_USUA")
	private String cidade;
	
	@Column(name="UF_USUA")
	private String estado;
	
	@Column(name="BAIRRO_USUA")
	private String bairro;
	
	@Column(name="CEP_USUA")
	private String cep;
	
	@Column(name="AVATAR_DIR")
	private String avatar;
	
	@Enumerated(EnumType.STRING)
	@Column(name="TP_USUA")
	private TipoUsuarioEnum tpUsuario;
	
	@Column(name="SENHA_USUA")
	private String senha;
	
	@Transient
	private String confirmSenha;
	
	@Transient
	private String oldsenha;
	
	@Transient
	public boolean loginVerificado;
	
	@Transient
	public String atualizaSenha;
	
	@Enumerated(EnumType.STRING)
	@Column(name="TP_PESSOA_USUA")
	private TipoPessoaEnum tipoPessoa;

	@Column(name="TELEFONE_USUA")
	private String telefone;
	
	@Column(name="CELULAR_USUA")
	private String celular;
	
	@Column(name="DT_ALTER_USUA")
	private Date dataAlter;
	
	public Usuario(){}

	public Usuario(Long id) {
		this.id = id;
	}
	
	@OneToMany(mappedBy="usuario")
	private List<UsuarioAtividade> usuarioAtividade;
	
	@OneToMany(mappedBy="usuario")
	private List<Notificacao> notificacoes;
	
}
