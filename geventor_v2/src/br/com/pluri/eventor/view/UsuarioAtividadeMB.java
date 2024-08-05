package br.com.pluri.eventor.view;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.etechoracio.common.view.BaseMB;
import br.com.etechoracio.common.view.MessageBundleLoader;
import br.com.pluri.eventor.business.AtividadeSB;
import br.com.pluri.eventor.business.NotificacaoSB;
import br.com.pluri.eventor.business.UsuarioAtividadeSB;
import br.com.pluri.eventor.business.UsuarioSB;
import br.com.pluri.eventor.enums.TipoNotificacaoEnum;
import br.com.pluri.eventor.model.Atividade;
import br.com.pluri.eventor.model.Notificacao;
import br.com.pluri.eventor.model.Usuario;
import br.com.pluri.eventor.model.UsuarioAtividade;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Scope("view")
@Controller
public class UsuarioAtividadeMB extends BaseMB {

	@Autowired
	private UsuarioAtividadeSB usuAtivSB;
	
	@Autowired
	private UsuarioSB usuSB;
	
	@Autowired
	private AtividadeSB atividadeSB;
	
	@Autowired
	private NotificacaoSB notificacaoSB;
	
	private Usuario usuario;
	private List<Usuario> inscritos;
	private List<UsuarioAtividade> usuAtiv;
	private List<UsuarioAtividade> myInscricoes;	
	private Atividade atividade;
	
	@PostConstruct
	public void postConstruct(){
		findInscritosMyEventos();
		findMyInscricoes();
	}
	
	public void findInscritosMyEventos(){
		this.usuAtiv = usuAtivSB.findIncritosNoEventoByUsuarioLogadoV2(getCurrentUserId());
	}
	
	public void findMyInscricoes(){
		this.myInscricoes = usuAtivSB.findMyInscricoes(getCurrentUserId());
	}
	
	public void getInscritosByAtividade(Atividade ativ){
		inscritos = usuSB.findIncritosByAtividade(ativ);
	}
	
	public void doEdit(Map<String, Object> params) {
		String status = (String) params.get("status");
		UsuarioAtividade usuAtiv = (UsuarioAtividade) params.get("usuAtiv");
		usuAtiv.setStatus(status);
		// TODO testar a notificacao da linha abaixo
		onSetNotificacao(MessageBundleLoader.getMessage("notif.mudancastatusinscrito.detalhe", new Object[] {status, usuAtiv.getAtividade().getNome()}), usuAtiv.getAtividade().getNome(), usuAtiv.getUsuario(), TipoNotificacaoEnum.ATIVIDADE.tipo);
		usuAtivSB.insert(usuAtiv);
	}
	
	public void doDelete(UsuarioAtividade usuAtiv){
		usuAtivSB.delete(usuAtiv);
		// TODO Testar a notificacao da linha abaixo
		onSetNotificacao(MessageBundleLoader.getMessage("notif.inscritodesistiu.detalhe", new Object[] {usuAtiv.getUsuario().getNome(), usuAtiv.getAtividade().getNome()}), usuAtiv.getAtividade().getNome(), usuAtiv.getAtividade().evento.getUsuario(), TipoNotificacaoEnum.ATIVIDADE.tipo);
		findMyInscricoes();
	}
	
	public void doConsultaUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public void doConsultaAtiv(Atividade ativ) throws SQLException{
		this.atividade = ativ;
		this.atividade.setQtdInscrito(atividadeSB.qtdInscritoInAtividade(atividade.getId()));
	}
	
	public String formatarDataFromTela(Map<String, Object> params) {
		Date data = (Date) params.get("data");
		String formato = (String) params.get("formato");
        return formatarData(data, formato);
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
	
}
