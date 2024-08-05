package br.com.pluri.eventor.security.business.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import br.com.pluri.eventor.business.AcessoSB;
import br.com.pluri.eventor.business.exception.UsuarioNaoAutenticadoException;

public class CustomAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	private AcessoSB acessoService;
	
	public Authentication authenticate(Authentication authentication){
		try{
			UsuarioAutenticado user = acessoService.autenticar(
					(String) authentication.getPrincipal(),
					(String) authentication.getCredentials());
			return new UsernamePasswordAuthenticationToken(user,
					user.getPassword(), user.getAuthorities());
		}catch(UsuarioNaoAutenticadoException e){
			throw new BadCredentialsException(e.getMessage());
		}
	}

	public boolean supports(Class<? extends Object> arg0) {
		return arg0.equals(UsernamePasswordAuthenticationToken.class);
	}

}
