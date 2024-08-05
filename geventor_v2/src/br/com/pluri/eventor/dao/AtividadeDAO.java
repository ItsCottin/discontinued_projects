package br.com.pluri.eventor.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.etechoracio.common.dao.BaseDAO;
import br.com.pluri.eventor.model.Atividade;

@Repository
public interface AtividadeDAO extends BaseDAO<Atividade> {
	
	public Atividade findById (Long id);
	
	@Query("select a from Atividade a where a.nome = :nome")
	public Atividade findByNome (@Param("nome") String nome);
	
	@Query("select a from Atividade a " +
			" inner join a.evento e " +
			" where e.id = :id")
	public List<Atividade> findByIdEven (@Param("id") Long idEvento);
	
	@Query("select a from Atividade a " +
			" inner join a.evento e " +
			" where e.id = :id and a.organizacao = :tpAtiv")
	public List<Atividade> findByIdEvenAndTipoAtiv (@Param("id") Long idEvento, @Param("tpAtiv") String tpAtiv);
	
	@Query("select a from Atividade a " +
			" inner join a.evento e " +
			" where e.usuario.id = :id")
	public List<Atividade> findAllAtividadeByUsuario (@Param("id") Long idUsu);
	
	@Query(value = "SELECT COUNT(*) FROM TBL_G_USUARIO_ATIVIDADE WHERE STATUS IN ('Pendente', 'Aprovado') AND ID_ATIVI = :idAtiv", nativeQuery = true)
	public int qtdInscritoInAtividade (@Param("idAtiv") Long idAtiv);
	
	@Query(value = "SELECT COUNT(*) FROM TBL_G_USUARIO_ATIVIDADE WHERE STATUS = :status AND ID_ATIVI = :idAtiv", nativeQuery = true)
	public int qtdInscritoSitInAtividade (@Param("status") String status, @Param("idAtiv") Long idAtiv);
}
