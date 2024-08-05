package br.com.pluri.eventor.security.business.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UsuarioAutenticado implements UserDetails{
	
	private String username;
	private String password;
	private String cpf;
	private Long id;
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;
	private boolean enabled = true;

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}
	
	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return true;
	}
	
	public UsuarioAutenticado(Long id, String username, String cpf){
		this.id = id;
		this.username = username;
		this.cpf = cpf;
	}
	
	public Long getId(){
		return id;
	}
	
	public String getCpf(){
		return cpf;
	}
}
