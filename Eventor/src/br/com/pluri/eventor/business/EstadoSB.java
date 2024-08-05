package br.com.pluri.eventor.business;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.etechoracio.common.business.BaseSB;
import br.com.pluri.eventor.dao.EstadoDAO;
import br.com.pluri.eventor.model.Estado;

@Service
public class EstadoSB extends BaseSB {

	private EstadoDAO estadoDAO;
	
	@Override
	protected void postConstructImpl() {
		estadoDAO = getDAO(EstadoDAO.class);
	}
	
	public List<Estado> findAll(){
		return estadoDAO.findAll();
	}

}
