package br.com.pluri.eventor.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.etechoracio.common.dao.BaseDAO;
import br.com.pluri.eventor.model.Cidade;

@Repository
public interface CidadeDAO extends BaseDAO<Cidade> {

	@Query("select c from Cidade c " +
			" inner join c.estado e " +
			" where e.uf = :uf")
	public List<Cidade> findByEstado(@Param("uf") String estado);
	
	@Query("select c from Cidade c " +
			" where c.name = :cidade")
	public List<Cidade> findByName(@Param("cidade") String cidade);
	
	public Cidade findById(Long idCidade);
	
}
