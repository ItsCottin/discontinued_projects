package br.com.pluri.eventor.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.com.etechoracio.common.model.BaseORM;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="TBL_G_EVENTO")
public class Evento extends BaseORM {
	
	@Id
	@GeneratedValue
	@Column(name="ID_EVEN")
	private Long id;
	
	@Column(name="ENDERECO_EVEN")
	private String endereco;
	
	@Column(name="DATAINICIO_EVEN")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInicio;
	
	@Column(name="DATAFIM_EVEN")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataFim;
	
	@Column(name="TITULO_EVEN")
	private String titulo;
	
	@Column(name="DESCRICAO_EVEN")
	private String descricao;
	
	@Column(name="SITE_EVEN")
	private String site;
	
	@Column(name="CIDADE_EVEN")
	private String cidade;
	
	@Column(name="ESTADO_EVEN")
	private String estado;
	
	@Column(name="VAGAS")
	private int vagas;

	@Column(name="EMAIL_EVEN")
	private String email;
	
	@Column(name="VLR_EVEN")
	private String vlr;
	
	@Column(name="TELEFONE_EVEN")
	private String telefone;
	
	@Column(name="CEP_EVEN")
	private String cep;
	
	@Column(name="BAIRRO_EVEN")
	private String bairro;
	
	@Column(name="NUMERO_LUGAR_EVEN")
	private String numeroLugar;
	
	@Column(name="DT_ALTER_EVEN")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAlter;
	
	@Column(name="SITE_PROPRIO")
	private boolean siteProprio;
	
	@Column(name="MY_ENDERECO_EVEN")
	private boolean usaMyEndereco;
	
	@Column(name="MY_TELEFONE_EVEN")
	private boolean usaMyTelefone;
	
	@Column(name="GUID_EVEN")
	private String guid;
	
	@Transient
	private String telefonecelular;
	
	@Transient
	private long qtdInscrito;
	
	@Transient
	private boolean doEditEven;
	
	@Transient
	private long qtdDifTemp;
	
	@Transient
    private String tpDifTemp;
	
	@Transient
	private boolean existeInscrito;
	
	@JoinColumn(name="ID_USUA", referencedColumnName="ID_USUA")
	@ManyToOne
	private Usuario usuario;
	
	@OneToMany(mappedBy="evento")
	private List<Atividade> atividades;

	public Evento(){}
	
	public Evento(Long id){
		this.id = id;
	}
}
