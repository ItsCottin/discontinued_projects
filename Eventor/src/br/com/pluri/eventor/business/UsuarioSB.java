package br.com.pluri.eventor.business;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.etechoracio.common.business.BaseSB;
import br.com.pluri.eventor.business.exception.CPFNotValidException;
import br.com.pluri.eventor.business.exception.LoginJaCadastradoException;
import br.com.pluri.eventor.business.exception.SenhaInvalidaException;
import br.com.pluri.eventor.business.util.PasswordUtils;
import br.com.pluri.eventor.dao.UsuarioDAO;
import br.com.pluri.eventor.model.Usuario;
import br.com.pluri.eventor.validator.ValidaCPF;

@Service
public class UsuarioSB extends BaseSB {
	
	private ValidaCPF validaCPF = new ValidaCPF();
	
	private UsuarioDAO usuarioDAO;
	
	private Usuario resultValidarSenha = new Usuario();

	@Override
	protected void postConstructImpl() {
		usuarioDAO = getDAO(UsuarioDAO.class);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void insert(Usuario usuario) throws LoginJaCadastradoException {
		List<Usuario> result = usuarioDAO.findByLogin(usuario.getLogin());
		if (CollectionUtils.isEmpty(result)) {
			String senhaCriptografada = PasswordUtils.criptografarMD5(usuario
					.getSenha());
			usuario.setSenha(senhaCriptografada);
			usuarioDAO.save(usuario);
		} else {
			throw new LoginJaCadastradoException("Login já cadastrado");
		}
	}

	// TODO buscar apenas usuarios que não estão relacionados no evento e que
	// não seja o usuario logado
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<Usuario> findAll() {
		return usuarioDAO.findAll();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void editeUsuario(Usuario usuario) throws SenhaInvalidaException, CPFNotValidException {
		usuario.setCpfCnpj(usuario.getCpfCnpj().replace(".", ""));
		usuario.setCpfCnpj(usuario.getCpfCnpj().replace("-", ""));
		if (usuario.getOldsenha().equals(usuario.getSenha())) {
			throw new SenhaInvalidaException("Senha já usada !");
		} else if (validarSenhaOld(usuario)) {
			if(validaCPF.isCPF(usuario.getCpfCnpj())){
				usuarioDAO.save(usuario);
			}else{
				throw new CPFNotValidException("CPF Inválido");
			}
		} else {
			throw new SenhaInvalidaException("Senha incorreta !");
		}
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public Usuario findById(Long idUsuarioLogado) {
		return usuarioDAO.findById(idUsuarioLogado);
	}

	public boolean validarSenhaOld(Usuario usuario) {
		resultValidarSenha = usuarioDAO.findByLoginAndSenha(usuario.getLogin(), PasswordUtils.criptografarMD5(usuario.getOldsenha()));
		if(resultValidarSenha!=null){
			return true;
		} else {
			return false;
		}
	}
}
