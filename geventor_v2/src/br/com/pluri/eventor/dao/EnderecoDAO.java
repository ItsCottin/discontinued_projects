package br.com.pluri.eventor.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.etechoracio.common.dao.BaseDAO;
import br.com.pluri.eventor.model.Endereco;

@Repository
public interface EnderecoDAO extends BaseDAO<Endereco> {
	
	@Query("select e from Endereco e " +
			" inner join e.cidade c " +
			" inner join c.estado s " +
			" where e.cep = :cep")
	public Endereco findCidadeAndEstadoByCEP (@Param("cep") String cep);
	
	@Query("select e from Endereco e" +
		" where e.cep = :cep")
	public Endereco findEnderecoByCEP (@Param("cep") String cep);
	
	
	@Query("select e from Endereco e " +
		" where e.cidade.id = :idCid and e.cep = :cep")
	public List<Endereco> findEnderecoByIdCidAndCEP (@Param("idCid") Long idCid, @Param("cep") String cep);
	
	@Query(value = "select * from address_searchs where city_id = :idCid", nativeQuery = true)
	public List<Endereco> findEnderecoByIdCid (@Param("idCid") Long idCid);
	
	@Query(value = "select distinct (a.ddd) from address_searchs a "
			+ "inner join cities c on a.city_id = c.id where c.id = :idCid", nativeQuery = true)
	public List<Integer> findDDDInnerJoinCity (@Param("idCid") Long idCid);
	
}
