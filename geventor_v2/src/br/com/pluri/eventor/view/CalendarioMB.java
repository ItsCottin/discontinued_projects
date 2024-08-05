package br.com.pluri.eventor.view;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import lombok.Getter;
import lombok.Setter;

import org.primefaces.context.RequestContext;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.etechoracio.common.view.BaseMB;
import br.com.etechoracio.common.view.MessageBundleLoader;
import br.com.pluri.eventor.business.AtividadeSB;
import br.com.pluri.eventor.business.EventoSB;
import br.com.pluri.eventor.model.Atividade;

@Getter
@Setter
@Scope("view")
@Controller
public class CalendarioMB extends BaseMB {
	
	@Autowired
    private EventoSB eventoSB;
	
	@Autowired
    private AtividadeSB atividadeSB;
	
	private Atividade ativSel;
	private List<Atividade> minhasAtividades;
	private ScheduleModel eventModel;
	private DefaultScheduleEvent event = new DefaultScheduleEvent();
	
	@PostConstruct 
    protected void initialize() { 
		this.minhasAtividades = new ArrayList<Atividade>();
		carregarCalendario();
	}
	
	public void carregarCalendario(){
		this.minhasAtividades.clear();
		this.minhasAtividades = atividadeSB.findAllAtividadeByUsuario(getCurrentUserId());
		eventModel = new DefaultScheduleModel();
		for(Atividade ativ : minhasAtividades){
			this.event.setStartDate(ativ.getDataInicio());
			this.event.setEndDate(ativ.getDataFim());
			this.event.setTitle(ativ.getNome());
			this.event.setId(ativ.getId().toString());
		}
		if(event.getId() != null){
			eventModel.addEvent(event);
		}
		
	}
	
	public void onEventMove(ScheduleEntryMoveEvent event){
		criticaAlterEvent("critica.movecalendario");
	}
	
	public void onEventResize(ScheduleEntryResizeEvent event) {
		criticaAlterEvent("critica.resizecalendario");
    }
	
	public void onEventSelect(SelectEvent selectEvent) throws SQLException {
		DefaultScheduleEvent event = (DefaultScheduleEvent) selectEvent.getObject();
	    this.ativSel = new Atividade();
	    this.ativSel = atividadeSB.findByNome(event.getTitle());
	    this.ativSel.setQtdInscrito(atividadeSB.qtdInscritoInAtividade(ativSel.getId()));
	    RequestContext.getCurrentInstance().execute("openModal('modalConsAtividade')");
    }
	
	public void criticaAlterEvent(String critica){
		showInfoMessage(MessageBundleLoader.getMessage(critica));
		carregarCalendario();
	}
	
	public String formatarDataFromTela(Map<String, Object> params) {
		Date data = (Date) params.get("data");
		String formato = (String) params.get("formato");
        return formatarData(data, formato);
    }

}
