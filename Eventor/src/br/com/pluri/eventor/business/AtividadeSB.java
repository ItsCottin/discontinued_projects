package br.com.pluri.eventor.business;


import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.etechoracio.common.business.BaseSB;
import br.com.pluri.eventor.dao.AtividadeDAO;
import br.com.pluri.eventor.model.Atividade;
import br.com.pluri.eventor.model.Evento;
import br.com.pluri.eventor.utils.DataTimeUtils;

@Service
public class AtividadeSB extends BaseSB {
	
	
	private AtividadeDAO atividadeDAO;
	
	@Override
	protected void postConstructImpl() {
		atividadeDAO = getDAO(AtividadeDAO.class);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void insert(Atividade atividade, Long idEvento){
		atividade.setEvento(new Evento(idEvento));
		atividade.setDataInicio(merge(atividade.getDataInicio(), atividade.getHoraInicio()));
		atividade.setDataFim(merge(atividade.getDataFim(), atividade.getHoraFim()));
		atividadeDAO.save(atividade);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Atividade> findByEventos (Long idEvento){
		return atividadeDAO.findByEventos(idEvento);
	}
	
	private Date merge(Date data, Date hora) {
		return DataTimeUtils.merge(data, hora);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void delete(Atividade exclui){
		atividadeDAO.delete(exclui);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	 public Atividade findById(Long id){
		 Atividade resultado = atividadeDAO.findOne(id);
		 resultado.setHoraInicio(resultado.getDataInicio());
		 resultado.setHoraFim(resultado.getDataFim());
		 return resultado;
	 }

}
