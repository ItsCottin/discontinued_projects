package br.com.pluri.eventor.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.etechoracio.common.dao.BaseDAO;
import br.com.pluri.eventor.model.Usuario;

@Repository
public interface UsuarioDAO extends BaseDAO<Usuario> {
	
	public Usuario findByLoginAndSenha (String login, String senha);
	
	public List<Usuario> findByLogin (String login);
	
	public Usuario findById (Long id);
}
