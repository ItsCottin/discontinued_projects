package br.com.pluri.eventor.view;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.primefaces.context.RequestContext;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import lombok.Getter;
import lombok.Setter;
import br.com.etechoracio.common.view.BaseMB;
import br.com.etechoracio.common.view.MessageBundleLoader;
import br.com.pluri.eventor.business.AtividadeSB;
import br.com.pluri.eventor.business.EventoSB;
import br.com.pluri.eventor.business.NotificacaoSB;
import br.com.pluri.eventor.business.UsuarioAtividadeSB;
import br.com.pluri.eventor.business.exception.LoginJaCadastradoException;
import br.com.pluri.eventor.business.exception.PeriodoDataInvalidaException;
import br.com.pluri.eventor.enums.TipoAtividadeEnum;
import br.com.pluri.eventor.enums.TipoNotificacaoEnum;
import br.com.pluri.eventor.model.Atividade;
import br.com.pluri.eventor.model.Evento;
import br.com.pluri.eventor.model.Notificacao;
import br.com.pluri.eventor.model.Usuario;
import br.com.pluri.eventor.model.UsuarioAtividade;

@Getter
@Setter
@Scope("view")
@Controller
public class AtividadeMB extends BaseMB {
	
	@Autowired
	private EventoSB eventoSB;

	@Autowired
	private AtividadeSB atividadeSB;
	
	@Autowired
	private UsuarioAtividadeSB inscritosSB;
	
	@Autowired
	private NotificacaoSB notificacaoSB;
	
	private List<Atividade> resultadoAtividadeByEvento;
	private Atividade editAtividade = new Atividade();
	public boolean modoConsulta;
	public Evento evenSel;
	private List<Evento> resultadoEvento;
	public Long idEvento;
	public boolean dataValidada;
	public String infoDataInicioEven;
	public String infoDataFimEven;
	public boolean evenSemVaga;
	
	private int vagasRestant;
	private int vagaTotalEven;
	private int vagaOldAtiv;
	
	@PostConstruct
	public void PostConstruct(){
		onEventos();
		this.modoConsulta = false;
		onAllAtividade();
		this.dataValidada = false;
	}
	
	public void doSave(){
		try {
			if(editAtividade.getVagas() == 0) {
				throw new LoginJaCadastradoException(MessageBundleLoader.getMessage("critica.qtdminimavagas"));
			}
			if(idEvento == null) {
				idEvento = editAtividade.evento.getId();
			}
			if(editAtividade.getId() == null){
				atividadeSB.insert(editAtividade, idEvento);
				showInfoMessage(MessageBundleLoader.getMessage("ativ.insert_sucess", new Object[] {editAtividade.getNome()}));
			}else{
				atividadeSB.editAtiv(editAtividade, idEvento);
				for (UsuarioAtividade insc : inscritosSB.findAllInscritosByIdAtividade(editAtividade.getId())) {
					// TODO Testar funcionamento desse For
					onSetNotificacao(MessageBundleLoader.getMessage("notif.alteracao.ativ.detalhe", new Object[] {editAtividade.getNome()}), editAtividade.getNome(), insc.getUsuario(), TipoNotificacaoEnum.ATIVIDADE.tipo);
				}
				showInfoMessage(MessageBundleLoader.getMessage("ativ.update_sucess", new Object[] {editAtividade.getNome()}));
			}
			RequestContext.getCurrentInstance().execute("selAba('visualizar')");
			onAllAtividade();
			doPrepareSave();
		} catch(LoginJaCadastradoException e) {
			showErrorMessage(e.getMessage());
			this.editAtividade.setVagas(1);
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
	
	// Metodo criado para alterar a atividade entre eventos.
	public void onEventoChangeV2(){
		editAtividade.setVagas(0);
		editAtividade.evento = eventoSB.findById(idEvento);
		setQtdVagasRest();
		this.evenSemVaga = false;
		this.modoConsulta = false;
		if(vagasRestant == 0) {
			this.evenSemVaga = true;
			this.modoConsulta = true;
		}
		if(editAtividade.evento.getVlr().equals("Gratuito")){
			editAtividade.setIsgratuito(true);
			editAtividade.setPreco("Gratuito");
		} else {
			editAtividade.setIsgratuito(false);
			editAtividade.setPreco(null);
		}
		onAllAtividade();
		getInfoDataEven();
	}
	
	public void onChangeCheckGratuito(){
		if(editAtividade.getNome() != null) {
			if (editAtividade.getPreco().equals("Gratuito")) {
				editAtividade.setIsgratuito(true);
			} else {
				editAtividade.setIsgratuito(false);
			}
		}
	}
	
	public void getInfoDataEven(){
		if(editAtividade.evento != null){
			StringBuilder sb = new StringBuilder();
			sb.append(formatarData(editAtividade.evento.getDataInicio(), "dd/MM/yyyy") + " " + MessageBundleLoader.getMessage("acento.das") + " ");
			sb.append(formatarData(editAtividade.evento.getDataInicio(), "HH:mm"));
			this.infoDataInicioEven = sb.toString();
			sb = new StringBuilder();
			sb.append(formatarData(editAtividade.evento.getDataFim(), "dd/MM/yyyy") + " " + MessageBundleLoader.getMessage("acento.as") + " ");
			sb.append(formatarData(editAtividade.evento.getDataFim(), "HH:mm"));
			this.infoDataFimEven = sb.toString();
		} else {
			this.infoDataFimEven = "";
			this.infoDataInicioEven = "";
		}
	}
	
	public void setTituloAtiv(){
		this.editAtividade.setNome(editAtividade.getNome());
	}
	
	public void setPrecoAtiv(){
		this.editAtividade.setPreco(editAtividade.getPreco());
	}
	
	public void validaDataInicio(){
		try {
			if(editAtividade.getDataInicio() != null){
				this.dataValidada = false;
				validaDataIniAtivDataFimAtiv();
				if(editAtividade.getDataInicio().before(editAtividade.evento.getDataInicio())){
					throw new PeriodoDataInvalidaException(MessageBundleLoader.getMessage("ativ.datemenordataeven", 
									new Object[] {formatarData(editAtividade.getDataInicio(), "dd/MM/yyyy"), 
											formatarData(editAtividade.evento.getDataInicio(), "dd/MM/yyyy")}));
				}
				if(editAtividade.getDataInicio().after(editAtividade.evento.getDataFim())){
					throw new PeriodoDataInvalidaException(
							MessageBundleLoader.getMessage("ativ.datemaiordataeven", 
									new Object[] {formatarData(editAtividade.getDataInicio(), "dd/MM/yyyy"), 
											formatarData(editAtividade.evento.getDataFim(), "dd/MM/yyyy")}));
				}
				this.dataValidada = true;
			}
		} catch (PeriodoDataInvalidaException e){
			showErrorMessage(e.getMessage());
			this.editAtividade.setDataInicio(null);
		}
	}
	
	public void validaDataFim(){
		try {
			if(editAtividade.getDataFim() != null){
				this.dataValidada = false;
				validaDataIniAtivDataFimAtiv();
				if(editAtividade.getDataFim().after(editAtividade.evento.getDataFim())){
					throw new PeriodoDataInvalidaException(
							MessageBundleLoader.getMessage("ativ.datemaiordataeven", 
									new Object[] {formatarData(editAtividade.getDataFim(), "dd/MM/yyyy"), 
											formatarData(editAtividade.evento.getDataFim(), "dd/MM/yyyy")}));
				}
				if(editAtividade.getDataFim().before(editAtividade.evento.getDataInicio())){
					throw new PeriodoDataInvalidaException(
							MessageBundleLoader.getMessage("ativ.datemenordataeven", 
									new Object[] {formatarData(editAtividade.getDataFim(), "dd/MM/yyyy"), 
										formatarData(editAtividade.evento.getDataInicio(), "dd/MM/yyyy")}));
				}
				this.dataValidada = true;
			}
		} catch (PeriodoDataInvalidaException e){
			showErrorMessage(e.getMessage());
			this.editAtividade.setDataFim(null);
		}
	}
	
	public void validaDataIniAtivDataFimAtiv(){
		try {
			if(editAtividade.getDataFim() != null && editAtividade.getDataInicio() != null) {
				if(editAtividade.getDataInicio().after(editAtividade.getDataFim())){
					throw new PeriodoDataInvalidaException(
							MessageBundleLoader.getMessage("date.iniciomaiorfim", 
									new Object[] {formatarData(editAtividade.getDataInicio(), "dd/MM/yyyy"), 
											 formatarData(editAtividade.getDataFim(), "dd/MM/yyyy")}, 
									Locale.getDefault()));
				}
			}
		} catch (PeriodoDataInvalidaException e){
			showErrorMessage(e.getMessage());
			this.editAtividade.setDataFim(null);
			this.editAtividade.setDataInicio(null);
		}
	}
	
	public void onSetDataEvenNaAtiv() {
		if(editAtividade.getEvento() != null) {
			if(editAtividade.isUsaPeriodoEven()) {
				this.editAtividade.setDataInicio(editAtividade.evento.getDataInicio());
				this.editAtividade.setDataFim(editAtividade.evento.getDataFim());
			} else {
				this.editAtividade.setDataInicio(null);
				this.editAtividade.setDataFim(null);
			}
		} else {
			this.editAtividade.setUsaPeriodoEven(false);
			showInfoMessage(MessageBundleLoader.getMessage("ativ.info_no_even"));
		}
	}
	
	public void onChangeGratuito(){
		if(editAtividade.isIsgratuito()){
			editAtividade.setPreco("Gratuito");
		} else {
			if (editAtividade.getId() != null) {
				editAtividade.setPreco(atividadeSB.findById(editAtividade.getId()).getPreco());
			} else {
				editAtividade.setPreco("");
			}
		}
	}
	
	public void onAllAtividade() {
		resultadoAtividadeByEvento = atividadeSB.findAllAtividadeByUsuario(getCurrentUserId());
	}
	
	public boolean isVigenteAtiv(Date data){
		int result = data.compareTo(getDateNowAtiv());
		if(result > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public Date getDateNowAtiv(){
		LocalDateTime agora = LocalDateTime.now();
	    ZoneId zoneId = ZoneId.of("UTC");
	    ZonedDateTime zdt = agora.atZone(zoneId);
	    return Date.from(zdt.toInstant());
	}
	
	public void onEventos(){
		resultadoEvento = eventoSB.findEventosByUsuario(getCurrentUserId());
		Iterator<Evento> it = resultadoEvento.iterator();
	    while (it.hasNext()) {
	        Evento even = it.next();
	        if (!isVigenteAtiv(even.getDataInicio())) {
	            if (editAtividade.getNome() != null) {
	                if (editAtividade.evento.getId() != even.getId()) {
	                    it.remove();
	                }
	            } else {
	                it.remove();
	            }
	        }
	    }
	}
	
	public void doRemove(Atividade exclui){
		List<UsuarioAtividade> inscritosNaAtiv = new ArrayList<UsuarioAtividade>();
		inscritosNaAtiv = inscritosSB.findAllInscritosByIdAtividade(exclui.getId());
		if(inscritosNaAtiv.size() > 0){
			for(UsuarioAtividade insc : inscritosNaAtiv){
				// TODO testar o funcionamento da notificacao desse for
				onSetNotificacao(MessageBundleLoader.getMessage("notif.delete.ativ.detalhe", new Object[] {exclui.getNome()}), exclui.getNome(), insc.getUsuario(), TipoNotificacaoEnum.ATIVIDADE.tipo);
				inscritosSB.delete(insc);
			}
			inscritosNaAtiv.clear();
		}
		exclui.setUsuarioAtividade(null);
		atividadeSB.delete(exclui);
		showInfoMessage(MessageBundleLoader.getMessage("ativ.delete_sucess", new Object[] {exclui.getNome()}));
	}
	
	public void setIdEvento(Long idEvento){
		this.idEvento = idEvento;
	}
	
	public void doPrepareInsert(){
		this.editAtividade = new Atividade();
		this.editAtividade.evento = new Evento();
		this.editAtividade.evento.setVagas(0);
		this.editAtividade.evento.setVlr("Gratuito");
	}
	
	public void doPrepareDel(Atividade ativ){
		try {
			ativ.setExisteInscrito(false);
			if(atividadeSB.qtdInscritoInAtividade(ativ.getId()) > 0 && ativ.getOrganizacao().equals(TipoAtividadeEnum.COMUM.tipo)){
				ativ.setExisteInscrito(true);
			}
			this.editAtividade = ativ;
		} catch (SQLException e) {
			showErrorMessage(MessageBundleLoader.getMessage("critica.erroconexaosql"));
		}
	}
	
	public void doEdit(Atividade edit) throws SQLException{
		doPrepareSave();
		prepareEditOuConsulta(edit);
		this.editAtividade.setDoEditAtiv(true);
		if(edit.evento != null){
			if (isVigente(edit.evento.getDataInicio())){
				if(!editAtividade.isExisteInscrito() && editAtividade.getOrganizacao().equals(TipoAtividadeEnum.COMUM.tipo)){
					this.modoConsulta = false;
				}
			} else {
				this.modoConsulta = true;
				editAtividade.setEventonaovigente(true);
			}
		} else {
			this.modoConsulta = true;
		}
	}
	
	public void doPrepareSave(){
		this.vagaOldAtiv = 0;
		this.evenSemVaga = false;
		this.editAtividade = new Atividade();
		onEventos();
		this.vagasRestant = 0; 
		this.idEvento = null;
		this.modoConsulta = false;
		onAllAtividade();
	}
	
	public void doConsulta(Atividade edit) throws SQLException {
		doPrepareSave();
		prepareEditOuConsulta(edit);
		this.editAtividade.setDoEditAtiv(false);
		this.modoConsulta = true;
	}
	
	public void prepareEditOuConsulta(Atividade edit) throws SQLException{
		this.editAtividade = edit;
		this.idEvento = editAtividade.evento.getId();
		this.editAtividade.setEventonaovigente(false);
		this.editAtividade.setExisteInscrito(false);
		this.editAtividade.setQtdInscrito(atividadeSB.qtdInscritoInAtividade(editAtividade.getId()));
		if(editAtividade.getQtdInscrito() > 0 && editAtividade.getOrganizacao().equals(TipoAtividadeEnum.COMUM.tipo)){
			this.modoConsulta = true;
			editAtividade.setExisteInscrito(true);
		}
		if(editAtividade.getOrganizacao().equals(TipoAtividadeEnum.GERENCIA.tipo)) {
			this.modoConsulta = true;
		}
		setQtdVagasRest();
		getInfoDataEven();
		onEventos();
		onChangeCheckGratuito();
		validaInputVaga();
	}
	
	public void setQtdVagasRest(){
		this.vagasRestant = eventoSB.findById(idEvento).getVagas();
		for (Atividade ativ : atividadeSB.findByIdEven(idEvento)){
			if(editAtividade.getId() != null){
				if(!ativ.getId().equals(editAtividade.getId())){
					this.vagasRestant = vagasRestant - ativ.getVagas();
				}
			} else {
				this.vagasRestant = vagasRestant - ativ.getVagas();
			}
		}
	}
	
	public void setQtdVagasComInputTela() {
		setQtdVagasRest();
		this.vagasRestant = vagasRestant - editAtividade.getVagas();
	}
	
	public void validaInputVaga(){
		if(editAtividade.evento != null){
			setQtdVagasComInputTela();
			if(vagasRestant < 0){
				showErrorMessage(MessageBundleLoader.getMessage("critica.vagamaiordisponivel"));
				editAtividade.setVagas(1);
				setQtdVagasComInputTela();
			} else if(editAtividade.getVagas() == 0){
				showErrorMessage(MessageBundleLoader.getMessage("critica.qtdminimavagas"));
				editAtividade.setVagas(1);
				setQtdVagasComInputTela();
			} else {
				setQtdVagasComInputTela();
			}
		}
	}
	
	public String formatarDataFromTela(Map<String, Object> params) {
		Date data = (Date) params.get("data");
		String formato = (String) params.get("formato");
        return formatarData(data, formato);
    }
}
