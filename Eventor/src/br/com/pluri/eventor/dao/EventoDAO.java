package br.com.pluri.eventor.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.etechoracio.common.dao.BaseDAO;
import br.com.pluri.eventor.model.Evento;
import br.com.pluri.eventor.model.Usuario;

@Repository
public interface EventoDAO extends BaseDAO<Evento> {
	
	public List<Evento> findByUsuario (Usuario usuario);
	
	public Evento findByTitulo(String titulo);
	
	/*@Query("update Evento e set e.local = ?1, e.dataInicio = ?2, e.dataFim = ?3, "
			+ "e.horaInicio = ?4, e.horaFim = ?5, e.titulo = ?6, "
			+ "e.descricao = ?7, e.twitter = ?8, e.facebook = ?9, "
			+ "e.site = ?10, e.cidade = ?11, e.estado = ?12, e.vagas = ?13, "
			+ "e.responsavel = ?14, e.email = ?15 where e.id = ?16")
	public void updateByEvento (String local, Date dataInicio, Date dataFim, String titulo,
			String descricao, String twitter, String facebook, String site, String cidade,
			String estado, int vagas, String responsavel, String email, Evento edit);
	*/
}
