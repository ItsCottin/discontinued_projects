package br.com.pluri.eventor.business;

import org.springframework.stereotype.Service;

import br.com.etechoracio.common.business.BaseSB;
import br.com.pluri.eventor.dao.DistritoDAO;
import br.com.pluri.eventor.model.Distrito;

@Service
public class DistritoSB extends BaseSB {

	private DistritoDAO distritoDAO;
	
	@Override
	protected void postConstructImpl() {
		distritoDAO = getDAO(DistritoDAO.class);
	}
	
	public Distrito findById(Long idDistrito){
		return distritoDAO.findById(idDistrito);
	}
}
