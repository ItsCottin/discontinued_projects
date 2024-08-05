package br.com.etechoracio.common.business;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import br.com.pluri.eventor.utils.DataTimeUtils;

/**
 * Criado o m�todo 'getDateAlter()' para retornar a data do sistema para utilizar em m�todos de altera��o e inclus�o de registro.
 * 
 * <pre>
 * Last Modified  $Date: 2023/04/15 $
 * Last Modified by $Author: Rodrigo Cotting $
 * </pre>
 * 
 * @author Rog�rio de Morais
 * @version $Revision: 1.1 $
 */
public abstract class BaseSB {
	
	@PersistenceContext
	private EntityManager em;

	private RepositoryFactorySupport factory;
	

	protected abstract void postConstructImpl();
	
	@PostConstruct
	void PostconstructCaller() {
		factory = new JpaRepositoryFactory(em);
		postConstructImpl();
	}

	protected <T> T getDAO(Class<T> clazz) {
		List<Class<?>> lista = Arrays.asList(clazz.getInterfaces());
		for (Class<?> cls : lista) {
			if (cls.getSimpleName().contains("Custom")) {
				String classe = mountClassImpl(cls);
				try {
					return factory.getRepository(clazz, 
												 Class.forName(classe).getConstructor(EntityManager.class)
												 .newInstance(em));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		return (T) factory.getRepository(clazz);
	}

	private String mountClassImpl(Class<?> value) {
		StringBuilder url = new StringBuilder();
		url.append(value.getCanonicalName());
		int start = url.length() - (value.getSimpleName().length());
		url.replace(start, url.length(), "");
		url.append("impl.");
		url.append(value.getSimpleName().replaceAll("Custom", "Impl"));
		return url.toString();
	}

	protected void clearEntityManagerSession() {
		if (em != null) {
			em.clear();
		}
	}

	protected void flushEntityManagerSession() {
		if (em != null) {
			em.flush();
		}
	}
	
	protected Date getDateAlterOld(){
		LocalDateTime agora = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
		ZoneOffset offset = ZoneOffset.of("-03:00");
		Instant instant = agora.toInstant(offset);
		return Date.from(instant);
	}
	
	protected Date getDateAlter(){
		LocalDateTime agora = LocalDateTime.now();
	    ZoneId zoneId = ZoneId.of("UTC");
	    ZonedDateTime zdt = agora.atZone(zoneId);
	    return Date.from(zdt.toInstant());
	}
	

	
	protected Date merge(Date data, Date hora) {
		return DataTimeUtils.merge(data, hora);
	}

}
