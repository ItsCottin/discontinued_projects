package br.com.pluri.eventor.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.etechoracio.common.view.BaseMB;
import br.com.pluri.eventor.business.NotificacaoSB;
import br.com.pluri.eventor.model.Notificacao;

@Scope("view")
@Controller
@ManagedBean(name="Notificacao")
public class NotificacaoMB extends BaseMB {
	
	@Autowired
	private NotificacaoSB notificacaoSB;
	
	private List<Notificacao> notificacoes;
	private int notiNaoVisu;
	private int qtdNotif;
	
	@PostConstruct
	public void postConstruct(){
		if(getCurrentUserId() != null) {
			getNotificacoesV2();
		} else {
			this.notificacoes = new ArrayList<Notificacao>();
		}
	}
	
	public void getNotificacoesV2(){
		this.notificacoes = notificacaoSB.findAllNotificacaoByUsuario(getCurrentUserId());
		getQtdNotifNaoVisu();
		setQtdNofit();
	}
	
	public void setQtdNofit(){
		this.qtdNotif = notificacoes.size();
	}
	
	public void getQtdNotifNaoVisu(){
		this.notiNaoVisu = 0;
		for(Notificacao notif : notificacoes){
			this.notiNaoVisu = notiNaoVisu + retSeVisuNOtifInt(notif);
		}
	}
	
	public int retSeVisuNOtifInt(Notificacao notif){
		if(notif.isVisualizado()){
			return 0;
		}else{
			return 1;
		}
	}
	
	public void doRemove(Notificacao exclui){
		notificacaoSB.delete(exclui);
		getNotificacoesV2();
	}
	
	public void doRemoveAll(){
		for(Notificacao notif : notificacoes){
			notificacaoSB.delete(notif);
		}
		getNotificacoesV2();
	}
	
	public void setVisualizado(){
		for(Notificacao notif : notificacoes){
			notif.setVisualizado(true);
			notificacaoSB.insert(notif);
		}
		getNotificacoesV2();
	}

	public int getNotiNaoVisu() {
		return notiNaoVisu;
	}

	public void setNotiNaoVisu(int notiNaoVisu) {
		this.notiNaoVisu = notiNaoVisu;
	}

	public int getQtdNotif() {
		return qtdNotif;
	}

	public void setQtdNotif(int qtdNotif) {
		this.qtdNotif = qtdNotif;
	}

	public void setNotificacoes(List<Notificacao> notificacoes) {
		this.notificacoes = notificacoes;
	}
	
	public List<Notificacao> getNotificacoes(){
		return notificacoes;
	}

}
