package br.com.pluri.eventor.business;

import org.springframework.stereotype.Service;

import br.com.etechoracio.common.business.BaseSB;
import br.com.pluri.eventor.business.exception.UsuarioNaoAutenticadoException;
import br.com.pluri.eventor.business.util.PasswordUtils;
import br.com.pluri.eventor.dao.UsuarioDAO;
import br.com.pluri.eventor.model.Usuario;
import br.com.pluri.eventor.security.business.model.UsuarioAutenticado;

@Service
public class AcessoSB extends BaseSB {

	private UsuarioDAO usuarioDAO;
	
	@Override
	protected void postConstructImpl() {
		usuarioDAO = getDAO(UsuarioDAO.class);
	}
	
	public UsuarioAutenticado autenticar(String login, String senha) throws UsuarioNaoAutenticadoException{
		Usuario user = usuarioDAO.findByLoginAndSenha(login, PasswordUtils.criptografarMD5(senha));
		if (user == null){
			throw new UsuarioNaoAutenticadoException();
		}
		return new UsuarioAutenticado(user.getId(), user.getNome(), user.getCpfCnpj());
	}

}
