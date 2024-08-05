package br.com.pluri.eventor.business.util;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.util.DigestUtils;

public class PasswordUtils {
	
	private static final Random RANDOM = new SecureRandom();
	
	public static String criptografarMD5 (String senha) {
		return senha != null ? DigestUtils.md5DigestAsHex(senha.getBytes()) : null;
	}
}
