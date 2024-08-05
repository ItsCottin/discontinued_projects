package br.com.pluri.eventor.business;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.etechoracio.common.business.BaseSB;
import br.com.pluri.eventor.dao.CidadeDAO;
import br.com.pluri.eventor.model.Cidade;

@Service
public class CidadeSB extends BaseSB {

	private CidadeDAO cidadeDAO;
	
	@Override
	protected void postConstructImpl() {
		cidadeDAO = getDAO(CidadeDAO.class);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Cidade> findByEstado(String estado){
		return cidadeDAO.findByEstado(estado);
	}
}
