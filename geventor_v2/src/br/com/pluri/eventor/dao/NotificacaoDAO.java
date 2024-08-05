package br.com.pluri.eventor.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.etechoracio.common.dao.BaseDAO;
import br.com.pluri.eventor.model.Notificacao;

@Repository
public interface NotificacaoDAO extends BaseDAO<Notificacao> {
	
	@Query(value = "SELECT * FROM TBL_G_NOTIFICACAO WHERE ID_USUA = :idUsuario", nativeQuery = true)
	public List<Notificacao> findAllNotificacaoByUsuario(@Param ("idUsuario") Long idUsuario);

}
