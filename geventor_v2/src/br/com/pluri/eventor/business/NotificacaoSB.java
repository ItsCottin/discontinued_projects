package br.com.pluri.eventor.business;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.etechoracio.common.business.BaseSB;
import br.com.pluri.eventor.dao.NotificacaoDAO;
import br.com.pluri.eventor.model.Evento;
import br.com.pluri.eventor.model.Notificacao;

@Service
public class NotificacaoSB extends BaseSB {

	private NotificacaoDAO notificacaoDAO;
	
	@Override
	protected void postConstructImpl() {
		notificacaoDAO = getDAO(NotificacaoDAO.class);
	}
	
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Notificacao> findAllNotificacaoByUsuario(Long idUsuario){
		return notificacaoDAO.findAllNotificacaoByUsuario(idUsuario);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void insert(Notificacao notifi){
		notifi.setDatacad(getDateAlter());
		notificacaoDAO.save(notifi);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void delete(Notificacao exclui){
		notificacaoDAO.delete(exclui);
	}
}
