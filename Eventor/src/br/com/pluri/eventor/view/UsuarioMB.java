package br.com.pluri.eventor.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;

import lombok.Getter;
import lombok.Setter;
import br.com.etechoracio.common.view.BaseMB;
import br.com.pluri.eventor.business.UsuarioSB;
import br.com.pluri.eventor.business.exception.CPFNotValidException;
import br.com.pluri.eventor.business.exception.LoginJaCadastradoException;
import br.com.pluri.eventor.business.exception.SenhaInvalidaException;
import br.com.pluri.eventor.model.Usuario;

@Getter
@Setter
@Controller
@Scope("view")
public class UsuarioMB extends BaseMB {

	@Autowired
	private UsuarioSB usuarioSB;
	private List<Usuario> usuarios;
	private List<Usuario> usuariosSelecionados;
	private Usuario editUsuario = new Usuario();	
	
	public void doSave(){
		try{
			usuarioSB.insert(editUsuario);
			showInfoMessage("Usuario cadastrado com sucesso");
			navigate("PAGE_LOGIN");
		}catch (LoginJaCadastradoException e2){
			showErrorMessage(e2.getMessage());
			validationFailed();
		}
	}
	
	public void findAll(){
		this.usuarios = usuarioSB.findAll();
	}
	
	@PostConstruct
	public void infoUserLogado(){
		if (getCurrentUserId()!=null){
			editUsuario = usuarioSB.findById(getCurrentUserId());
		}
	}
	
	public void doEdit() throws SenhaInvalidaException, CPFNotValidException{
		usuarioSB.editeUsuario(editUsuario);
	}
	
	/*public String logout() {
		String navegacao = "";
		try {
			authHelper.deslogar();
			navegacao = "logout";
		} catch(Exception e) {
		}
		return navegacao;
	}
	
	public void deslogar() {
		SecurityContext sc = SecurityContextHolder.getContext();
		Authentication authentication = sc.getAuthentication();
		if (authentication.getPrincipal() instanceof Usuario) {
			final HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();	
			logoutHandler.setInvalidateHttpSession(true);
			logoutHandler.logout(request, null, authentication);				
		}
	}*/
}
