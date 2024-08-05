package br.com.pluri.eventor.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.etechoracio.common.dao.BaseDAO;
import br.com.pluri.eventor.model.Atividade;

@Repository
public interface AtividadeDAO extends BaseDAO<Atividade> {
	
	@Query("select a from Atividade a " +
			" inner join a.evento e " +
			" where e.id = :id")
	public List<Atividade> findByEventos (@Param("id") Long idEvento);
	
	public Atividade findById (Long id);
	
}
