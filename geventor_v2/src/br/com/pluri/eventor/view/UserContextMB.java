package br.com.pluri.eventor.view;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import lombok.Getter;
import lombok.Setter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.etechoracio.common.view.BaseMB;
import br.com.pluri.eventor.model.Usuario;
import br.com.pluri.eventor.security.business.model.UsuarioAutenticado;

@Getter
@Setter
@ManagedBean
@SessionScoped
public class UserContextMB extends BaseMB {
	
	private String user;
	private String password;
	private String username;
	private boolean logado;
	private boolean noLogado = true;
	private boolean modoLogin;
	
	@ManagedProperty(name="authenticationManager", value="#{authenticationManager}")
	private AuthenticationManager authenticationManager;
	
	public static enum OUTCOME {
		SUCCESS, FAILURE;
	}
	
	public void afterAuthentication() {
		UsuarioAutenticado usuarioAuteticado = getCurrentUser();
		username = usuarioAuteticado.getUsername();
		password = null;
		logado = true;
		noLogado = false;
	}
	
	public void setInfoLogin(Usuario usu) {
		this.user = usu.getLogin();
		this.password = usu.getSenha();
	}
	
	
	public void doLogin() {
		try{
			UsernamePasswordAuthenticationToken authentication = 
				new UsernamePasswordAuthenticationToken(this.getUser(),this.getPassword());
			SecurityContextHolder.getContext().setAuthentication
				(authenticationManager.authenticate(authentication));
			afterAuthentication();
			navigate(PAGE_HOME);
		}catch(AuthenticationException e){
			showErrorMessage(e.getMessage());
		}
	}	
	
	
	
}
