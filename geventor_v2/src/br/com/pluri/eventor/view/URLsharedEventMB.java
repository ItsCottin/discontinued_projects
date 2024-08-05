package br.com.pluri.eventor.view;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.etechoracio.common.view.BaseMB;
import br.com.pluri.eventor.business.AtividadeSB;
import br.com.pluri.eventor.business.EventoSB;
import br.com.pluri.eventor.business.UsuarioAtividadeSB;
import br.com.pluri.eventor.business.UsuarioSB;
import br.com.pluri.eventor.model.Atividade;
import br.com.pluri.eventor.model.DiferencaData;
import br.com.pluri.eventor.model.Evento;
import br.com.pluri.eventor.model.Usuario;
import br.com.pluri.eventor.model.UsuarioAtividade;


@Getter
@Setter
@Scope("view")
@Controller
public class URLsharedEventMB extends BaseMB {
	
	@Autowired
	private EventoSB eventoSB;
	
	@Autowired
	private AtividadeSB atividadeSB;
	
	@Autowired
	private UsuarioAtividadeSB inscritoSB;
	
	@Autowired
	private UsuarioSB usuarioSB;
	
	private Evento evento;
	private Usuario usuario;
	public String idEven;
	
	@PostConstruct
	public void postConstruct(){
		try {
			this.evento = new Evento();
			getIdEvento();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getIdEvento() throws SQLException {
	    this.evento = eventoSB.findByGUID(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id").toString());
	    this.evento.setAtividades(atividadeSB.findByIdEven(evento.getId()));
	    List<Atividade> newAtiv = new ArrayList<Atividade>();
	    for (Atividade ativ : evento.getAtividades()) {
	    	ativ.setQtdInscrito(atividadeSB.qtdInscritoSitInAtividade("Aprovado", ativ.getId()));
	    	DiferencaData dd = calcDifDate(ativ.getDataAlter(), getDateNow());
	    	ativ.setQtdDifTemp(dd.getValor());
	    	ativ.setTpDifTemp(dd.getUnidade());
	    	ativ.setUsuarioAtividade(inscritoSB.findAllInscritosByIdAtividadeAndStatus("Aprovado", ativ.getId()));
	    	newAtiv.add(ativ);
	    }
	    this.evento.getAtividades().clear();
	    this.evento.setAtividades(newAtiv);
	}
	
	public String formatarDataFromTela(Map<String, Object> params) {
		Date data = (Date) params.get("data");
		String formato = (String) params.get("formato");
        return formatarData(data, formato);
    }
	
	public void doConsultaUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
