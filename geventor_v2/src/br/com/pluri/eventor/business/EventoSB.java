package br.com.pluri.eventor.business;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.etechoracio.common.business.BaseSB;
import br.com.pluri.eventor.business.exception.ExisteAtividadeVinculadaException;
import br.com.pluri.eventor.dao.EventoDAO;
import br.com.pluri.eventor.dao.EventoDAOImpl;
import br.com.pluri.eventor.model.Atividade;
import br.com.pluri.eventor.model.Evento;
import br.com.pluri.eventor.model.Usuario;

@Service
public class EventoSB extends BaseSB {
	
	private EventoDAO eventoDAO;
	
	private EventoDAOImpl eventoDAOimpl;
	
	@Override
	protected void postConstructImpl() {
		eventoDAO = getDAO(EventoDAO.class);
		eventoDAOimpl = new EventoDAOImpl();
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void insert(Evento evento, Long idUsuarioLogado){
		evento.setUsuario(new Usuario(idUsuarioLogado));
		evento.setDataAlter(getDateAlter());
		eventoDAO.save(evento);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Evento> findEventosByUsuario(Long  idUsuarioLogado){
		return eventoDAO.findByUsuario(new Usuario(idUsuarioLogado));
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Evento> findRecenEventosByUsuario(Long  idUsuarioLogado){
		return eventoDAO.findRecenEventosByUsuario(idUsuarioLogado);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public int qtdInscritoInEvento(Long  idEven) throws SQLException{
		int bigInt = eventoDAO.qtdInscritoInEvento(idEven);
		return bigInt;
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public int qtdInscritoInEventoPorTipoAtiv(Long  idEven, String tipoAtiv) throws SQLException{
		int bigInt = eventoDAO.qtdInscritoInEventoPorTipoAtiv(idEven, tipoAtiv);
		return bigInt;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void delete(Evento exclui){
		eventoDAO.delete(exclui);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	 public Evento findById(Long id){
		 Evento resultado = eventoDAO.findOne(id);
		 return resultado;
	 }
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	 public Evento findByGUID(String guid){
		 Evento resultado = eventoDAO.findByGUID(guid);
		 return resultado;
	 }
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Evento> findAll(){
		return eventoDAO.findAll();
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Evento> findAllEventoMenosMeus(Long idUsu){
		return eventoDAO.findAllEventoMenosMeus(idUsu);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Evento> findAllEventoMenosMeusComAtivExistPorTipoAtivRecen(Long idUsu, String tpAtiv){
		List<Evento> obj = eventoDAO.findAllEventoMenosMeusComAtivExistPorTipoAtivRecen(idUsu, tpAtiv);
		return obj;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Evento> findAllEventoMenosMeusRecen(Long idUsu){
		return eventoDAO.findAllEventoMenosMeusRecen(idUsu);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public Evento findByTitulo(String titulo){
		return eventoDAO.findByTitulo(titulo);
	}
	
	public Date getDataProxEventoDoUsuLogado(Long idUsu){
		return eventoDAO.getDataProxEventoDoUsuLogado(idUsu);
	} 
}
