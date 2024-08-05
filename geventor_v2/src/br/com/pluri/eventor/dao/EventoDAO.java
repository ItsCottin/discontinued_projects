package br.com.pluri.eventor.dao;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.etechoracio.common.dao.BaseDAO;
import br.com.pluri.eventor.model.Evento;
import br.com.pluri.eventor.model.Usuario;

@Repository
public interface EventoDAO extends BaseDAO<Evento> {
	
	public List<Evento> findByUsuario (Usuario usuario);
	
	public Evento findByTitulo(String titulo);
	
	@Query("select e from Evento e " +
		   " where e.usuario.id != :idUsu")
	public List<Evento> findAllEventoMenosMeus(@Param("idUsu") Long idUsu);
	
	@Query(value = "select * from TBL_G_EVENTO where ID_USUA != :idUsu and DATAINICIO_EVEN > GETDATE()", nativeQuery = true)
	public List<Evento> findAllEventoMenosMeusRecen(@Param("idUsu") Long idUsu);
	
	@Query(value = "SELECT * FROM TBL_G_EVENTO WHERE ID_USUA != :idUsu AND DATAINICIO_EVEN > GETDATE() AND EXISTS (SELECT ID_EVEN FROM TBL_G_ATIVIDADE WHERE ORGANIZACAO_ATIVI = :tpAtiv AND TBL_G_ATIVIDADE.ID_EVEN = TBL_G_EVENTO.ID_EVEN)", nativeQuery = true)
	public List<Evento> findAllEventoMenosMeusComAtivExistPorTipoAtivRecen(@Param("idUsu") Long idUsu, @Param("tpAtiv") String tpAtiv);
	
	@Query(value = "select MIN(datainicio_even) as proximoevento from TBL_G_EVENTO where datainicio_even >= GETDATE() and id_usua = :idUsu",
			nativeQuery = true)
	public Timestamp getDataProxEventoDoUsuLogado(@Param("idUsu") Long idUsu);
	
	@Query(value = "select * from TBL_G_EVENTO where ID_USUA = :idUsu and DATAINICIO_EVEN > GETDATE()", nativeQuery = true)
	public List<Evento> findRecenEventosByUsuario (@Param("idUsu") Long idUsu);
	
	@Query(value = "SELECT COUNT(*) FROM TBL_G_USUARIO_ATIVIDADE WHERE STATUS IN ('Pendente', 'Aprovado') AND ID_ATIVI IN " + 
    "(SELECT ID_ATIVI FROM TBL_G_ATIVIDADE WHERE ID_EVEN IN " +
    "(SELECT ID_EVEN FROM TBL_G_EVENTO WHERE ID_EVEN = :idEven))", nativeQuery = true)
	public int qtdInscritoInEvento (@Param("idEven") Long idEven);
	
	@Query(value = "SELECT COUNT(*) FROM TBL_G_USUARIO_ATIVIDADE WHERE STATUS IN ('Pendente', 'Aprovado') AND ID_ATIVI IN " + 
	"(SELECT ID_ATIVI FROM TBL_G_ATIVIDADE WHERE ORGANIZACAO_ATIVI = :tipoAtiv AND ID_EVEN IN " +
	"(SELECT ID_EVEN FROM TBL_G_EVENTO WHERE ID_EVEN = :idEven))", nativeQuery = true)
	public int qtdInscritoInEventoPorTipoAtiv (@Param("idEven") Long idEven, @Param("tipoAtiv") String tipoAtiv);
	
	@Query(value = "SELECT * FROM TBL_G_EVENTO WHERE GUID_EVEN = :guid", nativeQuery = true)
	public Evento findByGUID(@Param("guid") String guid);
	
}
