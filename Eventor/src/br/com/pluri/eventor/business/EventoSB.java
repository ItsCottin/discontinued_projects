package br.com.pluri.eventor.business;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.etechoracio.common.business.BaseSB;
import br.com.pluri.eventor.dao.EventoDAO;
import br.com.pluri.eventor.model.Evento;
import br.com.pluri.eventor.model.Usuario;
import br.com.pluri.eventor.utils.DataTimeUtils;

@Service
public class EventoSB extends BaseSB {
	
	private EventoDAO eventoDAO;
	
	//private boolean dataValida = false;
	
	@Override
	protected void postConstructImpl() {
		eventoDAO = getDAO(EventoDAO.class);
	}
	
	// M001 - insert do evento
	@Transactional(propagation=Propagation.REQUIRED)
	public void insert(Evento evento, Long idUsuarioLogado){
		evento.setUsuario(new Usuario(idUsuarioLogado));
		evento.setDataInicio(merge(evento.getDataInicio(), evento.getHoraInicio()));
		evento.setDataFim(merge(evento.getDataFim(), evento.getHoraFim()));
		if (evento.isSiteProprio()){
			evento.setSite("www.evento.pluri.com.br");
		}
		eventoDAO.save(evento);
	}
	
	private Date merge(Date data, Date hora) {
		return DataTimeUtils.merge(data, hora);
	}
	
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Evento> findEventosByUsuario(Long  idUsuarioLogado){
		return eventoDAO.findByUsuario(new Usuario(idUsuarioLogado));
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void delete(Evento exclui){
		eventoDAO.delete(exclui);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void edit(Evento edit){
		edit.setDataInicio(merge(edit.getDataInicio(), edit.getHoraInicio()));
		edit.setDataFim(merge(edit.getDataFim(), edit.getHoraFim()));
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	 public Evento findById(Long id){
		 Evento resultado = eventoDAO.findOne(id);
		 resultado.setHoraInicio(resultado.getDataInicio());
		 resultado.setHoraFim(resultado.getDataFim());
		 return resultado;
	 }
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void editeEvento (Evento evento){
		evento.setDataInicio(merge(evento.getDataInicio(), evento.getHoraInicio()));
		evento.setDataFim(merge(evento.getDataFim(), evento.getHoraFim()));
		if (evento.isSiteProprio()){
			evento.setSite("www.evento.pluri.com.br");
		}
		eventoDAO.save(evento);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Evento> findAll(){
		return eventoDAO.findAll();
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public Evento findByTitulo(String titulo){
		return eventoDAO.findByTitulo(titulo);
	}
	
}
