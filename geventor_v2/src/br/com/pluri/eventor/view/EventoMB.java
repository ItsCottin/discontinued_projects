package br.com.pluri.eventor.view;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.component.tabview.TabView;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.etechoracio.common.view.BaseMB;
import br.com.etechoracio.common.view.MessageBundleLoader;
import br.com.pluri.eventor.business.AtividadeSB;
import br.com.pluri.eventor.business.CidadeSB;
import br.com.pluri.eventor.business.DistritoSB;
import br.com.pluri.eventor.business.EnderecoSB;
import br.com.pluri.eventor.business.EstadoSB;
import br.com.pluri.eventor.business.EventoSB;
import br.com.pluri.eventor.business.NotificacaoSB;
import br.com.pluri.eventor.business.UsuarioAtividadeSB;
import br.com.pluri.eventor.business.UsuarioSB;
import br.com.pluri.eventor.business.exception.CEPInvalidoException;
import br.com.pluri.eventor.business.exception.DDDInvalidoException;
import br.com.pluri.eventor.business.exception.DataInvalidaException;
import br.com.pluri.eventor.business.exception.ExisteAtividadeVinculadaException;
import br.com.pluri.eventor.business.exception.LoginJaCadastradoException;
import br.com.pluri.eventor.business.exception.NRCasaUsuarioException;
import br.com.pluri.eventor.business.exception.PeriodoDataInvalidaException;
import br.com.pluri.eventor.business.util.PasswordUtils;
import br.com.pluri.eventor.enums.CustoEnum;
import br.com.pluri.eventor.enums.StatusInscritoEnum;
import br.com.pluri.eventor.enums.TipoAtividadeEnum;
import br.com.pluri.eventor.enums.TipoNotificacaoEnum;
import br.com.pluri.eventor.model.Atividade;
import br.com.pluri.eventor.model.Cidade;
import br.com.pluri.eventor.model.DiferencaData;
import br.com.pluri.eventor.model.Endereco;
import br.com.pluri.eventor.model.Estado;
import br.com.pluri.eventor.model.Evento;
import br.com.pluri.eventor.model.Notificacao;
import br.com.pluri.eventor.model.Usuario;
import br.com.pluri.eventor.model.UsuarioAtividade;

/**
 * Adicionado m�todo "retTitleEven(Evento even)" para retornar o titulo do evento
 * 
 * <pre>
 * Last Modified  $Date: 2023/04/04 12:27:45 $
 * Last Modified by $Author: Rodrigo Cotting $
 * </pre>
 * 
 * @author Rodrigo Cotting
 * @version $Revision: 1.0 $
 */

@Getter
@Setter
@Scope("view")
@Controller
public class EventoMB extends BaseMB {
	
	@Autowired
	private DistritoSB distritoSB;
	
	@Autowired
	private EnderecoSB enderecoSB;
	
	@Autowired
	private EventoSB eventoSB;
	
	@Autowired
	private UsuarioSB usuarioSB;
	
	@Autowired
	private CidadeSB cidadeSB;
	
	@Autowired
	private EstadoSB estadoSB;
	
	@Autowired
	private AtividadeSB atividadeSB;
	
	@Autowired
	private UsuarioAtividadeSB inscritosSB;
	
	@Autowired
	private NotificacaoSB notificacaoSB;
	
	private List<Atividade> resultadoAtividadeByEvento;
	private List<Evento> resultadoEvento;
	private Evento editEvento;
	private List<Cidade> cidades;
	private List<Estado> estados;
	
	// TODO Avaliar se houve impacto na tela e remover esse metodo caso não houve impacto
	//private int indexTab = 0;
	
	private List<Evento> resultEven;
	private List<Evento> allEvenMenosMeus;
	private static final long serialVersionUID = 1L;
	private boolean confirmaExclui = false;
	public boolean modoConsulta;
	public Atividade atividade;
	private Endereco enderecoDoCEP;
	private String usaTelefone;
	private String maskTelefone;
	public boolean dataValidada;
	public boolean cepvalidoinformado;
	public boolean exibeModalEditAtiv;
	public int vagasInAtividades;
	
	@PostConstruct
	public void postConstruct(){
		try{
			onEventos();
			estados = estadoSB.findAll();
			this.modoConsulta = false;
			this.editEvento = new Evento();
			this.editEvento.setGuid(PasswordUtils.generateGUID());
			findAllEventoMenosMeus();
			this.usaTelefone = "Telefone";
			this.maskTelefone = "(99) 9999-9999";
			this.editEvento.setVlr("Pago");
			this.dataValidada = false;
			this.cepvalidoinformado = false;
			this.exibeModalEditAtiv = false;
		} catch (ConstraintViolationException e){
			showInfoMessage(e.getStackTrace().toString());
		}
	}
	
	public void onSetNotificacao(String detalhe, String titulo, Usuario usuario, String tipo) {
		Notificacao noti = new Notificacao();
		noti.setTitulo(titulo);
		noti.setDetalhe(detalhe);
		noti.setUsuario(usuario);
		noti.setTipo(tipo);
		noti.setVisualizado(false);
		notificacaoSB.insert(noti);
	}
	
	public void findAllEventoMenosMeus(){
		try {
			this.allEvenMenosMeus = new ArrayList<Evento>();
			for (Evento even : eventoSB.findAllEventoMenosMeusComAtivExistPorTipoAtivRecen(getCurrentUserId(), TipoAtividadeEnum.COMUM.tipo)){
				DiferencaData dd = calcDifDate(even.getDataAlter(), getDateNow());
				even.setQtdDifTemp(dd.getValor());
				even.setTpDifTemp(dd.getUnidade());
				even.setDoEditEven(false);
				even.setQtdInscrito(eventoSB.qtdInscritoInEvento(even.getId()));
				this.allEvenMenosMeus.add(even);
			}
		} catch (SQLException e) {
			showErrorMessage(MessageBundleLoader.getMessage("critica.erroconexaosql"));
		}
	}
	
	public DiferencaData getDifDateFromTela(Map<Date, Object> params) {
		Date datainicio = (Date) params.get("datainicio");
		Date datafim = (Date) params.get("datafim");
		DiferencaData dd = calcDifDate(datainicio, datafim);
		return dd;
	}
	
	// TODO Avaliar se houve impacto na tela e remover esse metodo caso não houve impacto
	//public void onTabChange(TabChangeEvent event) {
	//    TabView tabView = (TabView) event.getComponent();
	//    int activeTabIndex = tabView.getActiveIndex();
	//    this.indexTab = activeTabIndex;
	//}
	
	public void onEstadoChange(){
		if(editEvento.getEstado() == null){
			cidades = new ArrayList<Cidade>();
			return;
		}
		cidades = cidadeSB.findByEstado(editEvento.getEstado());
	}
	
	public void getQtdVagasInAtividadesByEvento(Long idEven) {
		for (Atividade ativ : atividadeSB.findByIdEven(idEven)) {
			this.vagasInAtividades = vagasInAtividades + ativ.getVagas();
		}
	}
	
	public void onChangeUtilizarSite(){
		if(!editEvento.isSiteProprio()){
			this.editEvento.setSite("");
		} else {
			this.editEvento.setSite("https://geventor.azurewebsites.net/evento.xhtml?id=" + editEvento.getGuid());
		}
	}
	
	public void onChangeMyEndereco(){
		try{
			if (editEvento.isUsaMyEndereco()){
				Usuario usu = new Usuario();
				usu = usuarioSB.findById(getCurrentUserId());
				if(usu.getCep() == null || usu.getCep().equals("")){
					throw new CEPInvalidoException(MessageBundleLoader.getMessage("critica.endereconotfoundinusu"));
				} else {
					editEvento.setCep(usu.getCep());
					editEvento.setEstado(usu.getEstado());
					onEstadoChange();
					editEvento.setCidade(usu.getCidade());
					editEvento.setBairro(usu.getBairro());
					editEvento.setEndereco(usu.getEndereco());
					this.cepvalidoinformado = true;
					if(usu.getNrCasa() == null || usu.getNrCasa().equals("")){
						throw new NRCasaUsuarioException(MessageBundleLoader.getMessage("info.numeronotfoundinusu"));
					} else {
						editEvento.setNumeroLugar(usu.getNrCasa());
					}
				}
			} else {
				limpaEndereco();
			}
		} catch (CEPInvalidoException e) {
			showErrorMessage(e.getMessage());
			this.editEvento.setUsaMyEndereco(false);
			limpaEndereco();
		} catch (NRCasaUsuarioException ex) {
			showInfoMessage(ex.getMessage());			
		}
	}
	
	public void doConsultaAtiv(Atividade ativ) throws SQLException{
		this.atividade = ativ;
		this.atividade.setQtdInscrito(atividadeSB.qtdInscritoInAtividade(atividade.getId()));
	}
	
	public void onChangeMyTelefone(){
		if(editEvento.isUsaMyTelefone()){
			Usuario usu = new Usuario();
			usu = usuarioSB.findById(getCurrentUserId());
			if(usaTelefone.equals("Telefone")){
				if(usu.getTelefone() != null){
					if(usu.getTelefone().equals("")){
						retOnChangeMyTelefone("Telefone");
					} else {
						setMaskTelefone();
						editEvento.setTelefone(usu.getTelefone());
					}
				} else {
					retOnChangeMyTelefone("Telefone");
				}
			}
			if(usaTelefone.equals("Celular")){
				if(usu.getCelular() != null){
					if(usu.getCelular().equals("")){
						retOnChangeMyTelefone("Celular");
					} else {
						setMaskTelefone();
						editEvento.setTelefone(usu.getCelular());
					}
				} else {
					retOnChangeMyTelefone("Celular");
				}
			}
			if(usu.getEmail() == null || usu.getEmail().equals("")){
				retOnChangeMyTelefone("E-mail");
			} else {
				editEvento.setEmail(usu.getEmail());
			}
		} else {
			editEvento.setTelefone("");
			editEvento.setEmail("");
			if(usaTelefone.equals("Telefone")){
				this.maskTelefone = "(99) 9999-9999";
			} else {
				this.maskTelefone = "(99) 99999-9999";
			}
		}
	}
	
	public void retOnChangeMyTelefone(String msg){
		showErrorMessage(MessageBundleLoader.getMessage("info.informacaonotfoundinusu", new Object[] {msg}));
		this.editEvento.setUsaMyTelefone(false);
	}
	
	public void onChangePrecoEven(){
		if(editEvento.getVlr().equals("Pago")){
			editEvento.setVlr("Gratuito");
		} else {
			editEvento.setVlr("Pago");
		}
	}
	
	public void setMaskTelefone(){
		if(usaTelefone.equals("Telefone")){
			this.maskTelefone = "(99) 9999-9999";
		} else {
			this.maskTelefone = "(99) 99999-9999";
		}
	}
	
	public void doSave(boolean validaPeriodoAtiv) {
		try {
			if(editEvento.getVagas() == 0) {
				throw new LoginJaCadastradoException(MessageBundleLoader.getMessage("critica.qtdminimavagas"));
			}
			boolean isCad = false;
			if(editEvento.getId() == null){
				isCad = true;
			}
			if(editEvento.getId() != null && validaPeriodoAtiv){
				List<Atividade> newAtiv = new ArrayList<Atividade>();
				this.editEvento.setAtividades(atividadeSB.findByIdEven(editEvento.getId()));
				for (Atividade ativ : editEvento.getAtividades()){
					ativ.setForaPeriodoInicio(false);
					ativ.setForaPeriodoFim(false);
					if(isData1MaiorData2(editEvento.getDataInicio(), ativ.getDataInicio())){
						ativ.setForaPeriodoInicio(true);
					}
					if(isData1MaiorData2(ativ.getDataFim(), editEvento.getDataFim())){
						ativ.setForaPeriodoFim(true);
					}
					if(ativ.isForaPeriodoInicio() && isData1MaiorData2(editEvento.getDataInicio() ,ativ.getDataFim())) {
						ativ.setForaPeriodoFim(true);
					}
					if(ativ.isForaPeriodoFim() && isData1MaiorData2(ativ.getDataInicio(), editEvento.getDataFim())) {
						ativ.setForaPeriodoInicio(true);
					}
					newAtiv.add(ativ);
				}
				this.editEvento.getAtividades().clear();
				this.editEvento.setAtividades(newAtiv);
				for (Atividade ativ : editEvento.getAtividades()){
					if(ativ.isForaPeriodoInicio() || ativ.isForaPeriodoFim()){
						throw new ExisteAtividadeVinculadaException();
					}
				}
			}
			if(editEvento.getTitulo() != null) {
				eventoSB.insert(editEvento, getCurrentUserId());
				if(editEvento.getAtividades() != null) {
					if(editEvento.getAtividades().size() > 0) {
						for (Atividade ativ : editEvento.getAtividades()){
							if(ativ.getOrganizacao().equals(TipoAtividadeEnum.GERENCIA.tipo)) {
								if(!ativ.getNome().contains(editEvento.getTitulo())) {
									ativ.setNome(MessageBundleLoader.getMessage("nome.ativ.gerencia", new Object[] {editEvento.getTitulo()}));
									ativ.setDetalhes(MessageBundleLoader.getMessage("detalhe.ativ.gerencia", new Object[] {editEvento.getTitulo(), getCurrentUser().getUsername()}));
								}
							} else {
								for (UsuarioAtividade insc : inscritosSB.findAllInscritosByIdAtividade(ativ.getId())) {
									// TODO Testar funcionamento desse For
									if(ativ.isForaPeriodoInicio()) {
										onSetNotificacao(MessageBundleLoader.getMessage("notif.mudancadatainicio.ativ.detalhe", new Object[] {ativ.getNome(), 
												formatarData(ativ.getDataInicio(), "dd/MM/yyyy") + " as " + formatarData(ativ.getDataInicio(), "HH:mm")}), ativ.getNome(), insc.getUsuario(), TipoNotificacaoEnum.ATIVIDADE.tipo);
									}
									if(ativ.isForaPeriodoFim()) {
										onSetNotificacao(MessageBundleLoader.getMessage("notif.mudancadatainicio.ativ.detalhe", new Object[] {ativ.getNome(), 
												formatarData(ativ.getDataFim(), "dd/MM/yyyy") + " as " + formatarData(ativ.getDataFim(), "HH:mm")}), ativ.getNome(), insc.getUsuario(), TipoNotificacaoEnum.ATIVIDADE.tipo);
									}
									onSetNotificacao(MessageBundleLoader.getMessage("notif.alteracao.even.detalhe", new Object[] {editEvento.getTitulo()}), editEvento.getTitulo(), insc.getUsuario(), TipoNotificacaoEnum.EVENTO.tipo);
								}
							}
							atividadeSB.editAtiv(ativ, editEvento.getId());
							showInfoMessage(MessageBundleLoader.getMessage("ativ.update_sucess", new Object[] {ativ.getNome()}));
						}
					}
				}
			}
			if (isCad) {
				Atividade ativ = setRetAtivGerente();
				atividadeSB.insert(ativ, editEvento.getId());
				inscritosSB.insert(setRetInscritoGerente(ativ));
				showInfoMessage(MessageBundleLoader.getMessage("even.insert_sucess", new Object[] {editEvento.getTitulo()}));
			} else {
				showInfoMessage(MessageBundleLoader.getMessage("even.update_sucess", new Object[] {editEvento.getTitulo()}));
			}
			onEventos();
			doPrepareSave();
			RequestContext.getCurrentInstance().execute("selAba('visualizar')");
		} catch (ExisteAtividadeVinculadaException e) {
			RequestContext.getCurrentInstance().execute("openModal('modalEditAtiv')");
		} catch (LoginJaCadastradoException e) {
			this.editEvento.setVagas(1);
			showErrorMessage(e.getMessage());
		}
	}
	
	public Atividade setRetAtivGerente() {
		Atividade ativ = new Atividade();
		ativ.setDataInicio(editEvento.getDataInicio());
		ativ.setDataFim(editEvento.getDataFim());
		ativ.setDetalhes(MessageBundleLoader.getMessage("detalhe.ativ.gerencia", new Object[] {editEvento.getTitulo(), getCurrentUser().getUsername()}));
		ativ.setEvento(editEvento);
		ativ.setNome(MessageBundleLoader.getMessage("nome.ativ.gerencia", new Object[] {editEvento.getTitulo()}));
		ativ.setOrganizacao(TipoAtividadeEnum.GERENCIA.tipo);
		ativ.setIsgratuito(true);
		ativ.setPreco("Gratuito");
		ativ.setUsaPeriodoEven(true);
		ativ.setVagas(1);
		return ativ;
	}
	
	public UsuarioAtividade setRetInscritoGerente(Atividade ativ) {
		UsuarioAtividade inscrito = new UsuarioAtividade();
		inscrito.setAtividade(ativ);
		inscrito.setStatus(StatusInscritoEnum.APROVADO.status);
		inscrito.setUsuario(usuarioSB.findById(getCurrentUserId()));
		return inscrito;
	}
	
	public void doSavePopUp(){
		ajustPeriodoAtiv();
		doSave(false);
	}
	
	public void ajustPeriodoAtiv(){
		List<Atividade> newAtiv = new ArrayList<Atividade>();
		for (Atividade ativ : editEvento.getAtividades()) {
			if(ativ.isForaPeriodoInicio()) {
				ativ.setDataInicio(editEvento.getDataInicio());
			}
			if(ativ.isForaPeriodoFim()) {
				ativ.setDataFim(editEvento.getDataFim());
			}
			newAtiv.add(ativ);
		}
		this.editEvento.getAtividades().clear();
		this.editEvento.setAtividades(newAtiv);
	}
	
	public boolean isData1MaiorData2(Date data1, Date date2){
		int fim = data1.compareTo(date2);
		if(fim > 0){
			return true;
		}
		return false;
	}
	
	public void doPrepareSave(){
		editEvento = new Evento();
		this.vagasInAtividades = 0;
		this.editEvento.setGuid(PasswordUtils.generateGUID());
		this.modoConsulta = false;
		this.resultadoAtividadeByEvento = null;
		findAllEventoMenosMeus();
	}
	
	public String formatarDataFromTela(Map<String, Object> params) {
		Date data = (Date) params.get("data");
		String formato = (String) params.get("formato");
        return formatarData(data, formato);
    }
	
	public void validarDatasEvento(){
		try {
			this.dataValidada = false;
			if (editEvento.getDataInicio() != null && editEvento.getDataFim() != null) {
				// Nova Logica completa.
				if(editEvento.getDataInicio().after(editEvento.getDataFim())){
					throw new DataInvalidaException(MessageBundleLoader.getMessage("date.iniciomaiorfim", 
						new Object[] {formatarData(editEvento.getDataInicio(), "dd/MM/yyyy"), formatarData(editEvento.getDataFim(), "dd/MM/yyyy")}));
				}
				Date dataAtual = getDateNow();
				if(editEvento.getDataInicio().before(dataAtual)){
					throw new PeriodoDataInvalidaException(MessageBundleLoader.getMessage("date.iniciomenoratual", 
						new Object[] {formatarData(editEvento.getDataInicio(),"dd/MM/yyyy"), formatarData(dataAtual,"dd/MM/yyyy")}));
				}
				List<Atividade> myAtividade = new ArrayList<Atividade>();
				for(UsuarioAtividade insc : inscritosSB.findMyInscricoes(getCurrentUserId())){
					if(!insc.getAtividade().evento.getId().equals(editEvento.getId())) {
						myAtividade.add(insc.getAtividade());
					}
				}
				for(Atividade ativ : myAtividade){
					if(dataEstaDentroPeriodo(ativ.getDataInicio(), ativ.getDataFim(), editEvento.getDataInicio(), editEvento.getDataFim())){
						throw new PeriodoDataInvalidaException(MessageBundleLoader.getMessage("critica.periodojacomprometido", 
							new Object[] {ativ.getNome()}));
					}
				}
				this.dataValidada = true;
			} else {
				this.dataValidada = true;
			}
		} catch (PeriodoDataInvalidaException e){
			showErrorMessage(e.getMessage());
			this.editEvento.setDataInicio(null);
			this.editEvento.setDataFim(null);
		} catch (DataInvalidaException ex){
			showErrorMessage(ex.getMessage());
			this.editEvento.setDataInicio(null);
			this.editEvento.setDataFim(null);
		}
	}

	public void onEventos(){
		resultadoEvento = eventoSB.findEventosByUsuario(getCurrentUserId());
	}
	
	// 100% Funcional.
	public void doRemove(Evento exclui){
		List<UsuarioAtividade> inscritosNaAtiv = new ArrayList<UsuarioAtividade>();
		for(Atividade ativ : exclui.getAtividades()){
			inscritosNaAtiv = inscritosSB.findAllInscritosByIdAtividade(ativ.getId());
			if(inscritosNaAtiv.size() > 0){
				for(UsuarioAtividade insc : inscritosNaAtiv){
					// TODO testar o funcionamento de notificacoes desse for
					onSetNotificacao(MessageBundleLoader.getMessage("notif.delete.ativ.detalhe", new Object[] {ativ.getNome()}), ativ.getNome(), insc.getUsuario(), TipoNotificacaoEnum.ATIVIDADE.tipo);
					onSetNotificacao(MessageBundleLoader.getMessage("notif.delete.even.detalhe", new Object[] {editEvento.getTitulo()}), editEvento.getTitulo(), insc.getUsuario(), TipoNotificacaoEnum.EVENTO.tipo);
					inscritosSB.delete(insc);
				}
				inscritosNaAtiv.clear();
			}
			atividadeSB.delete(ativ);
			showInfoMessage(MessageBundleLoader.getMessage("ativ.delete_sucess", new Object[] {ativ.getNome()}));
		}
		exclui.setAtividades(null);
		eventoSB.delete(exclui);
		showInfoMessage(MessageBundleLoader.getMessage("even.delete_sucess", new Object[] {exclui.getTitulo()}));
		onEventos();
	}
	
	public void doEdit(Evento edit) throws SQLException {
		onPrepareEditOuConsulta(edit, false);
		getQtdVagasInAtividadesByEvento(edit.getId());
		this.editEvento.setDoEditEven(true);
	}
	
	public void validaVagasDoEditEven() {
		if(editEvento.getVagas() < vagasInAtividades) {
			showErrorMessage(MessageBundleLoader.getMessage("critica.qtdvagasineven", new Object[] {vagasInAtividades}));
			editEvento.setVagas(vagasInAtividades);
		} else if (editEvento.getVagas() < 1 && vagasInAtividades == 0) {
			showErrorMessage(MessageBundleLoader.getMessage("critica.qtdminimavagas"));
			editEvento.setVagas(1);
		}
	}
	
	public void doConsulta(Map<String, Object> params) throws SQLException {
		Evento edit = (Evento) params.get("evento");
		String request = (String) params.get("request");
		onPrepareEditOuConsulta(edit, true);
		this.editEvento.setDoEditEven(false);
		if (request.equals("card")){
			setAtividadeSeEstaInscrito(atividadeSB.findByIdEvenAndTipoAtiv(edit.getId(), TipoAtividadeEnum.COMUM.tipo));
		} else {
			this.resultadoAtividadeByEvento = null;
		}
	}
	
	public boolean findSeEstaInscritoNaAtividade(Long idAtiv){
		UsuarioAtividade usuAtiv = new UsuarioAtividade();
		usuAtiv = inscritosSB.findSeEstaInscritoNaAtividade(idAtiv, getCurrentUserId());
		if(usuAtiv == null){
			return false;
		} else {
			return true;
		}
	}
	
	public void setAtividadeSeEstaInscrito(List<Atividade> ativ){
		try {
			this.resultadoAtividadeByEvento = null;
			this.resultadoAtividadeByEvento = new ArrayList<Atividade>();
			for (Atividade ati : ativ) {
				ati.setEstaInscrito(findSeEstaInscritoNaAtividade(ati.getId()));
				ati.setQtdInscrito(atividadeSB.qtdInscritoInAtividade(ati.getId()));
				this.resultadoAtividadeByEvento.add(ati);
			}
		} catch (SQLException e) {
			showErrorMessage(MessageBundleLoader.getMessage("critica.erroconexaosql"));
		}
	}
	
	@SuppressWarnings("null")
	public void doIncrever(Atividade ativ){
		try {
			for (UsuarioAtividade myinsc : inscritosSB.findMyInscricoes(getCurrentUserId())) {
				if(!myinsc.getAtividade().getId().equals(ativ.getId()) && 
						dataEstaDentroPeriodo(myinsc.getAtividade().getDataInicio(), myinsc.getAtividade().getDataFim(), ativ.getDataInicio(), ativ.getDataFim())) {
					throw new PeriodoDataInvalidaException(MessageBundleLoader.getMessage("critica.inscritojacomprometido", new Object[] {myinsc.getAtividade().getNome(), ativ.getNome()}));
				}
			}
			UsuarioAtividade inscrito = inscritosSB.findSeEstaInscritoNaAtividade(ativ.getId(), getCurrentUserId());
			if(inscrito == null) {
				inscrito = new UsuarioAtividade();
				inscrito.setUsuario(usuarioSB.findById(getCurrentUserId()));
				inscrito.setAtividade(ativ);
				inscrito.setStatus(StatusInscritoEnum.PENDENTE.status);
			}
			inscritosSB.insert(inscrito);
			onSetNotificacao(MessageBundleLoader.getMessage("notif.newinscritoinativ.detalhe", new Object[] {inscrito.getUsuario().getNome() ,ativ.getNome()}), ativ.getNome(), ativ.evento.getUsuario(), TipoNotificacaoEnum.ATIVIDADE.tipo);
			setAtividadeSeEstaInscrito(atividadeSB.findByIdEvenAndTipoAtiv(ativ.evento.getId(), TipoAtividadeEnum.COMUM.tipo));
		} catch (PeriodoDataInvalidaException e) {
			showInfoMessage(e.getMessage());
		}
	}
	
	public void onPrepareEditOuConsulta(Evento edit, Boolean consulta) throws SQLException {
		doPrepareSave();
		this.modoConsulta = consulta;
		this.editEvento = edit;
		if(editEvento.getGuid() == null || editEvento.getGuid().equals("")){
			this.editEvento.setGuid(PasswordUtils.generateGUID());
		}
		this.editEvento.setAtividades(atividadeSB.findByIdEven(editEvento.getId()));
		this.editEvento.setExisteInscrito(false);
		if(eventoSB.qtdInscritoInEventoPorTipoAtiv(editEvento.getId(), TipoAtividadeEnum.COMUM.tipo) > 0){
			this.modoConsulta = true;
			editEvento.setExisteInscrito(true);
		}
		validaTpContato();
		onEstadoChange();
	}
	
	public void doPrepareInsert(){
		this.editEvento = new Evento();
		this.editEvento.setVlr(CustoEnum.PAGO.custo);
	}
	
	public void findEnderecoByCEP(){
		try {
			if (!editEvento.getCep().contains("_")){
				this.enderecoDoCEP = new Endereco();
				this.enderecoDoCEP = enderecoSB.findCidadeAndEstadoByCEP(editEvento.getCep());
				Usuario usu = new Usuario();
				usu = usuarioSB.findById(getCurrentUserId());
				if(!editEvento.getCep().equals(usu.getCep())){
					this.editEvento.setUsaMyEndereco(false);
				}
				if (enderecoDoCEP != null) {
					this.editEvento.setCep(enderecoDoCEP.getCep());
					this.editEvento.setEstado(enderecoDoCEP.cidade.estado.getUf());
					onEstadoChange();
					this.editEvento.setCidade(enderecoDoCEP.cidade.getName());
					this.editEvento.setBairro(distritoSB.findById(enderecoDoCEP.getIdDistrict()).getBairro());
					this.editEvento.setEndereco(enderecoDoCEP.getEndereco());
					this.cepvalidoinformado = true;
				} else {
					throw new CEPInvalidoException(MessageBundleLoader.getMessage("critica.cepnotfound", new Object[] {editEvento.getCep()}));
				}
			} else {
				limpaEndereco();
				this.editEvento.setUsaMyEndereco(false);
				this.cepvalidoinformado = false;
			}
		} catch (CEPInvalidoException e) {
			showErrorMessage(e.getMessage());
			limpaEndereco();
			this.cepvalidoinformado = false;
			this.editEvento.setUsaMyEndereco(false);
		}
	}
	
	public void doPrepareDel(Evento even){
		try {
			even.setExisteInscrito(false);
			if(eventoSB.qtdInscritoInEventoPorTipoAtiv(even.getId(), TipoAtividadeEnum.COMUM.tipo) > 0){
				even.setExisteInscrito(true);
			}
			even.setAtividades(atividadeSB.findByIdEven(even.getId()));
			this.editEvento = even;
		} catch (SQLException e) {
			showErrorMessage(MessageBundleLoader.getMessage("critica.erroconexaosql"));
		}
	}
	
	public void limpaEndereco(){
		editEvento.setCep("");
		editEvento.setEstado(null);
		editEvento.setCidade("");
		onEstadoChange();
		editEvento.setBairro("");
		editEvento.setEndereco("");
		editEvento.setNumeroLugar("");
		this.cepvalidoinformado = false;
	}
	
	public void validaTpContato(){
		if(editEvento.getTelefone().length() == 15){
			maskTelefone = "(99) 99999-9999";
			usaTelefone = "Celular";
		} else {
			maskTelefone = "(99) 9999-9999";
			usaTelefone = "Telefone";
		}
	}
	
	public void validaDDDAfterSelCidade(){
		if(editEvento.getTelefone() != null) {
			validaDDD(editEvento.getTelefone());
		}
	}
	
	public void validaDDD(String numero){
		try {
			if (!numero.contains("_")){
				if(!numero.equals("")){
					if (numero.length() == 15){
						validaPrimeiroDigCelular(numero);
					}
					if (!editEvento.getCidade().equals("")){
						if(editEvento.getCep() == null){
							boolean naoContem = true;
							List<Cidade> cidlist = new ArrayList<Cidade>();
							cidlist = cidadeSB.findByName(editEvento.getCidade());
							List<Integer> listddd;
							for (Cidade cid : cidlist){
								listddd = enderecoSB.findDDDInnerJoinCity(cid.getId());
								for (Integer ddd : listddd){
									if (ddd == Integer.parseInt(numero.substring(1, 3))){
										naoContem = false;
									}
								}
							}
							if(naoContem){
								throw new DDDInvalidoException(
										MessageBundleLoader.getMessage("critica.dddnaopertencecidade", new Object[] {numero, editEvento.getCidade()}));
							}
						} else {
							if(Integer.parseInt(numero.substring(1, 3)) != enderecoSB.findEnderecoByCEP(editEvento.getCep()).getDdd()){
								throw new DDDInvalidoException(
										MessageBundleLoader.getMessage("critica.dddnaopertencecidade", new Object[] {numero, editEvento.getCidade()}));
							}
						}
					}
				}
			}
		} catch (DDDInvalidoException e){
			showInfoMessage(e.getMessage());
		}
	}
	
	public void validaPrimeiroDigCelular(String numero){
		try {
			if (Integer.parseInt(numero.substring(5, 6)) != 9) {
				throw new DDDInvalidoException(MessageBundleLoader.getMessage("critica.digcelularinvalido", new Object[] {numero.substring(5, 6), numero}));
			}
		} catch (DDDInvalidoException e) {
			showErrorMessage(e.getMessage());
			editEvento.setTelefone("");
		}
	}	
}