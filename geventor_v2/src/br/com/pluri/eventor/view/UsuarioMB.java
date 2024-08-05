package br.com.pluri.eventor.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.context.PrimeFacesContext;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import lombok.Getter;
import lombok.Setter;
import br.com.etechoracio.common.view.BaseMB;
import br.com.etechoracio.common.view.MessageBundleLoader;
import br.com.pluri.eventor.business.CidadeSB;
import br.com.pluri.eventor.business.DistritoSB;
import br.com.pluri.eventor.business.EnderecoSB;
import br.com.pluri.eventor.business.EstadoSB;
import br.com.pluri.eventor.business.EventoSB;
import br.com.pluri.eventor.business.UsuarioAtividadeSB;
import br.com.pluri.eventor.business.UsuarioSB;
import br.com.pluri.eventor.business.exception.CEPInvalidoException;
import br.com.pluri.eventor.business.exception.CPFNotValidException;
import br.com.pluri.eventor.business.exception.CampoObrigatorioException;
import br.com.pluri.eventor.business.exception.DDDInvalidoException;
import br.com.pluri.eventor.business.exception.LoginJaCadastradoException;
import br.com.pluri.eventor.business.exception.SenhaInvalidaException;
import br.com.pluri.eventor.model.Cidade;
import br.com.pluri.eventor.model.Endereco;
import br.com.pluri.eventor.model.Estado;
import br.com.pluri.eventor.model.Usuario;
import br.com.pluri.eventor.validator.ValidaCPF;

@Getter
@Setter
@Controller
@Scope("view")
public class UsuarioMB extends BaseMB  {

	@Autowired
	private UsuarioSB usuarioSB;
	
	@Autowired
	private UsuarioAtividadeSB usuarioAtividadeSB;
	
	@Autowired
	private CidadeSB cidadeSB;
	
	@Autowired
	private EnderecoSB enderecoSB;
	
	@Autowired
	private EstadoSB estadoSB;

	@Autowired
	private DistritoSB distritoSB;
	
	@Autowired
	private EventoSB eventoSB;
	
	private ValidaCPF validaCPF = new ValidaCPF();
	
	private Endereco enderecoDoCEP;
	private String cepInformado;
	private List<Cidade> cidades;
	private List<Estado> estados;
	private List<Usuario> usuarios;
	private List<Usuario> usuariosSelecionados;
	private Usuario editUsuario = new Usuario();
	private Usuario usuarioLogado = new Usuario();
	private boolean modoConsulta;
	public String modalPosCadUsu;
	
	// TODO Implementar funcionalidade que após alteração de 'Nome de Login' não permitir mais realizar alteração de usuario ate um proximo login
	private boolean edicaoIndisponivel;  
	
	
	private int myeventos;
	private int inscritospen;
	private int myinscricoes;
	private String proxevent;
	
	public void doSave(){
		try {
			usuarioSB.insert(editUsuario);
			showInfoMessage(MessageBundleLoader.getMessage("usu.insert_sucess", new Object[] {editUsuario.getLogin()}));
			RequestContext.getCurrentInstance().execute("selAba('login')");
		} catch (LoginJaCadastradoException e2){
			showErrorMessage(e2.getMessage());
		} catch (CampoObrigatorioException e) {
			showErrorMessage(e.getMessage());
		} catch (SenhaInvalidaException e) {
			showErrorMessage(e.getMessage());
			editUsuario.setSenha(null);
			editUsuario.setConfirmSenha(null);
		}
	}
	
	public void findAll(){
		this.usuarios = usuarioSB.findAll();
	}
	
	@PostConstruct
	public void infoUserLogado(){
		if (getCurrentUserId()!=null){
			this.estados = estadoSB.findAll();
			this.editUsuario = usuarioSB.findById(getCurrentUserId());
			this.usuarioLogado = usuarioSB.findById(getCurrentUserId());
			editUsuario.setLoginVerificado(true);
			this.modoConsulta = true;
			this.edicaoIndisponivel = false;
			this.editUsuario.setAtualizaSenha("N");
			if(editUsuario.getEstado() != null){
				onEstadoChange();
			}
			if(editUsuario.getCep() != null){
				this.cepInformado = editUsuario.getCep();
			}
			findMyInscricoes();
			findInscritosPenMyEventos();
			getDataProxEventoDoUsuLogado();
			findMyEvento();
		}
	}
	
	public void findMyEvento(){
		this.myeventos = eventoSB.findEventosByUsuario(getCurrentUserId()).size();
	}
	
	public void findMyInscricoes(){
		this.myinscricoes = usuarioAtividadeSB.findMyInscricoes(getCurrentUserId()).size();
	}
	
	public void findInscritosPenMyEventos(){
		this.inscritospen = usuarioAtividadeSB.findIncritosNoEventoByUsuarioLogadoByStatus(getCurrentUserId(), "Pendente").size();
	}
	
	public void getDataProxEventoDoUsuLogado(){
		this.proxevent = formatarData(eventoSB.getDataProxEventoDoUsuLogado(getCurrentUserId()), "dd/MM");
	}
	
	public void preEdit() {
		if(modoConsulta) {
			this.modoConsulta = false;
		} else {
			this.modoConsulta = true;
			this.editUsuario = usuarioSB.findById(getCurrentUserId());
			this.editUsuario.setAtualizaSenha("N");
			this.editUsuario.loginVerificado = true;
		}
		if(editUsuario.getCep() != null){
			this.cepInformado = editUsuario.getCep();
		} else {
			this.cepInformado = null;
		}
	}
	
	public void setAvatarEdit(String avatar) throws SenhaInvalidaException{
		this.editUsuario.setAvatar(avatar);
		doEdit();
	}
	
	public void onEstadoChange(){
		if(editUsuario.getEstado() == null){
			cidades = new ArrayList<Cidade>();
			return;
		}
		cidades = cidadeSB.findByEstado(editUsuario.getEstado());
	}
	
	public boolean validaNumeroComplet(String numero) {
		if (numero.matches(".*\\d+.*") && numero.contains("_")) {
			return false;
		} else {
			return true;
		}
	}
	
	public void validaDDD(String numero) {
		try {
			if (!numero.contains("_")) {
				if(!numero.equals("")){
					if (numero.length() == 15) {
						validaPrimeiroDigCelular(numero);
					}
					if (!editUsuario.getCidade().equals("")){
						if(editUsuario.getCep() == null){
							boolean naoContem = true;
							List<Cidade> cidlist = new ArrayList<Cidade>();
							cidlist = cidadeSB.findByName(editUsuario.getCidade());
							List<Integer> listddd;
							for (Cidade cid : cidlist) {
								listddd = enderecoSB.findDDDInnerJoinCity(cid.getId());
								for (Integer ddd : listddd){
									if (ddd == Integer.parseInt(numero.substring(1, 3))){
										naoContem = false;
									}
								}
							}
							if(naoContem) {
								throw new DDDInvalidoException(
										MessageBundleLoader.getMessage("critica.dddnaopertencecidade", new Object[] {numero, editUsuario.getCidade()}));
							}
						} else {
							if(Integer.parseInt(numero.substring(1, 3)) != enderecoSB.findEnderecoByCEP(editUsuario.getCep()).getDdd()){
								throw new DDDInvalidoException(
										MessageBundleLoader.getMessage("critica.dddnaopertencecidade", new Object[] {numero, editUsuario.getCidade()}));
							}
						}
					}
				}
			}
		} catch (DDDInvalidoException e) {
			showInfoMessage(e.getMessage());
		}
	}
	
	public void validaDDDAfterSelCidade(){
		if(editUsuario.getCelular() != null) {
			validaDDD(editUsuario.getCelular());
			validaDDD(editUsuario.getTelefone());
		}
	}
	
	public void validaPrimeiroDigCelular(String numero) {
		try {
			if (Integer.parseInt(numero.substring(5, 6)) != 9) {
				throw new DDDInvalidoException(MessageBundleLoader.getMessage("critica.digcelularinvalido", new Object[] {numero.substring(5, 6), numero}));
			}
		} catch (DDDInvalidoException e) {
			showErrorMessage(e.getMessage());
			editUsuario.setCelular("");
		}
	}
	
	public void findEnderecoByCEP() {
		try {
			if (!cepInformado.contains("_")) {
				this.enderecoDoCEP = new Endereco();
				this.enderecoDoCEP = enderecoSB.findCidadeAndEstadoByCEP(cepInformado);
				if (enderecoDoCEP != null) {
					this.editUsuario.setCep(enderecoDoCEP.getCep());
					this.editUsuario.setEstado(enderecoDoCEP.cidade.estado.getUf());
					onEstadoChange();
					this.editUsuario.setCidade(enderecoDoCEP.cidade.getName());
					this.editUsuario.setBairro(distritoSB.findById(enderecoDoCEP.getIdDistrict()).getBairro());
					this.editUsuario.setEndereco(enderecoDoCEP.getEndereco());
				} else {
					throw new CEPInvalidoException(MessageBundleLoader.getMessage("critica.cepnotfound", new Object[] {cepInformado}));
				}
			} else {
				throw new CEPInvalidoException(MessageBundleLoader.getMessage("critica.cepinvalido", new Object[] {cepInformado.replace("_", "")}));
			}
		} catch (CEPInvalidoException e) {
			showErrorMessage(e.getMessage());
			limpaEndereco();
		}
	}
	
	public void limpaEndereco(){
		this.editUsuario.setCep(null);
		this.cepInformado = null;
		this.editUsuario.setEstado(null);
		this.editUsuario.setCidade("");
		this.editUsuario.setBairro("");
		this.editUsuario.setEndereco("");
		onEstadoChange();
	}
	
	@SuppressWarnings("static-access")
	public void validaCPFTab(String cpf) {
		try {
			if(!cpf.contains("_") && !validaCPF.isCPF(cpf.replace(".", "").replace("-", ""))) {
				throw new CPFNotValidException(MessageBundleLoader.getMessage("critica.cpfinvalido", new Object[] {cpf}));
			}
			
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
			editUsuario.setCpfCnpj("");
		}
	}
	
	public void verificaLoginExiste() {
		try {
			Usuario usuarioLogin = new Usuario();
			usuarioLogin = usuarioSB.findUsuarioByLogin(editUsuario.getLogin());
			if(getCurrentUser() != null){
				if (!usuarioLogado.getLogin().equals(editUsuario.getLogin()) && usuarioLogin != null) {
					this.editUsuario.setLoginVerificado(false);
					throw new LoginJaCadastradoException(MessageBundleLoader.getMessage("critica.loginjacadastrado", new Object[] {editUsuario.getLogin()}));
				} else {
					this.editUsuario.setLoginVerificado(true);
				}
			} else if (usuarioLogin != null){
				this.editUsuario.setLoginVerificado(false);
				throw new LoginJaCadastradoException(MessageBundleLoader.getMessage("critica.loginjacadastrado", new Object[] {editUsuario.getLogin()}));
			}
			this.editUsuario.setLoginVerificado(true);
		} catch (LoginJaCadastradoException e) {
			showErrorMessage(e.getMessage());
			if(getCurrentUser() != null){
				editUsuario.setLogin(usuarioLogado.getLogin());
			} else {
				editUsuario.setLogin(null);
			}
		}
	}
	
	public void verificaAlterLogin(String login) {
		if(getCurrentUser() != null){
			if(!login.equals(usuarioLogado.getLogin())) {
				this.editUsuario.setLoginVerificado(false);
			} else {
				this.editUsuario.setLoginVerificado(true);
			}
		} else {
			this.editUsuario.setLoginVerificado(false);
		}
	}
	
	public void doEdit() throws SenhaInvalidaException {
		try {
			if(editUsuario.getAtualizaSenha().equals("N")){
				this.editUsuario.setSenha(usuarioSB.findById(getCurrentUserId()).getSenha());
			}
			if(editUsuario.getCelular() != null) {
				if(editUsuario.getCelular().contains("_")){
					editUsuario.setCelular(null);
				}
			}
			if(editUsuario.getTelefone() != null) {
				if(editUsuario.getTelefone().contains("_")){
					editUsuario.setTelefone(null);
				}
			}
			usuarioSB.editeUsuario(editUsuario);	
			showInfoMessage(MessageBundleLoader.getMessage("usu.update_sucess"));
			this.editUsuario = new Usuario();
			this.editUsuario = usuarioSB.findById(getCurrentUserId());
			this.modoConsulta = true;
			this.editUsuario.setAtualizaSenha("N");
			verificaAlterLogin(editUsuario.getLogin());
		} catch (SenhaInvalidaException es) {
			showErrorMessage(es.getMessage());
		} catch (LoginJaCadastradoException e) {
			showErrorMessage(e.getMessage());
		}
	}
	
	public String formatarDataFromTela(Map<String, Object> params) {
		Date data = (Date) params.get("data");
		String formato = (String) params.get("formato");
        return formatarData(data, formato);
	}	
}
