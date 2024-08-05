package br.com.etechoracio.common.view;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.tabview.TabView;
import org.primefaces.context.RequestContext;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.etechoracio.common.model.BaseORM;
import br.com.pluri.eventor.model.DiferencaData;
import br.com.pluri.eventor.security.business.model.UsuarioAutenticado;
import br.com.pluri.eventor.utils.DataTimeUtils;
/**
 * Base para todos os ManagedBeans(MBs) da Aplica��o.
 * 
 * <pre>
 * Last Modified  $Date: 2015/10/28 19:22:45 $
 * Last Modified by $Author: Rog�rio de Morais $
 * </pre>
 * 
 * @author Rog�rio de Morais
 * @version $Revision: 1.1 $
 */
public abstract class BaseMB {
	protected static final String RECORD_INSERT_SUCCESS = "Registro inserido com sucesso.";
	protected static final String RECORD_UPDATE_SUCCESS = "Registro alterado com sucesso.";
	protected static final String RECORD_REMOVE_SUCCESS = "Exclus�o realizada com sucesso.";
	protected static final String SK_USER_CTX = "user_context";

	protected static final String CONTEXT_PAGES = "/pages";
	public static final String PAGE_HOME = CONTEXT_PAGES + "/inicio.xhtml";
	public static final String PAGE_LOGIN = "/index.xhtml";

	@PostConstruct
	final void postConstructCaller() {
		postConstruct();
	}

	/**
	 * Metodo chamado ap�s o Contrutor da Classe.
	 */
	protected void postConstruct() {
	}

	protected final FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	protected final void showInsertMessage() {
		showInfoMessage(RECORD_INSERT_SUCCESS);
	}

	protected final void showUpdateMessage() {
		showInfoMessage(RECORD_UPDATE_SUCCESS);
	}

	protected final void showDeleteMessage() {
		showInfoMessage(RECORD_REMOVE_SUCCESS);
	}

	protected final void showErrorMessage(String summary) {
		showErrorMessage(summary, false);
	}

	protected final void showErrorMessage(String identificador, String summary) {
		showErrorMessage(identificador, summary, false);
	}

	protected final void showErrorMessage(String summary, boolean useAsKey) {
		showErrorMessage(null, summary, useAsKey);
	}

	protected final void showErrorMessage(String summary, boolean useAsKey,
			Object... params) {
		showErrorMessage(null, summary, useAsKey, params);
	}

	protected final void showErrorMessage(String identificador, String summary,
			boolean useAsKey) {
		showErrorMessage(identificador, summary, useAsKey,
				new Object[] { null });
	}

	protected final void showErrorMessage(String identificador, String summary,
			boolean useAsKey, Object... params) {
		showMessage(identificador, FacesMessage.SEVERITY_ERROR, summary,
				useAsKey, params);
	}

	protected final void showInfoMessage(String summary) {
		showInfoMessage(summary, false);
	}

	protected final void showInfoMessage(String identificador, String summary) {
		showInfoMessage(identificador, summary, false);
	}

	protected final void showInfoMessage(String summary, boolean useAsKey) {
		showInfoMessage(null, summary, useAsKey);
	}

	protected final void showInfoMessage(String summary, boolean useAsKey,
			Object... params) {
		showInfoMessage(null, summary, useAsKey, params);
	}

	protected final void showInfoMessage(String identificador, String summary,
			boolean useAsKey) {
		showInfoMessage(identificador, summary, useAsKey, new Object[] { null });
	}

	protected final void showInfoMessage(String identificador, String summary,
			boolean useAsKey, Object... params) {
		showMessage(identificador, FacesMessage.SEVERITY_INFO, summary,
				useAsKey, params);
	}

	protected final void showWarnMessage(String summary) {
		showWarnMessage(summary, false);
	}

	protected final void showWarnMessage(String summary, boolean useAsKey) {
		showWarnMessage(summary, useAsKey, new Object[] { null });
	}

	protected final void showWarnMessage(String summary, boolean useAsKey,
			Object... params) {
		showMessage(null, FacesMessage.SEVERITY_WARN, summary, useAsKey, params);
	}

	protected final void showMessage(FacesMessage.Severity severity,
			String summary, boolean useAsKey) {
		showMessage(null, severity, summary, useAsKey, new Object[] { null });
	}

	protected final void showMessage(String identificador,
			FacesMessage.Severity severity, String summary, boolean useAsKey,
			Object... params) {
		if (useAsKey) {
			summary = MessageBundleLoader.getMessage(summary, params);
		}
		getFacesContext().addMessage(identificador,
				new FacesMessage(severity, summary, null));

		showMessagePostInvoke();
	}

	protected void showMessagePostInvoke() {
	}

	protected final String getMessage(String key) {
		return MessageBundleLoader.getMessage(key);
	}

	protected final String getMessage(String key, Object... params) {
		return MessageBundleLoader.getMessage(key, params);
	}

	protected final void showUnselectedItemMessage(String name, String action) {
		String msg = MessageBundleLoader.getMessage(
				"abstractMB.item.nao.selecionado",
				new Object[] { name, action });
		showErrorMessage(msg);
	}

	protected final void showRequiredPropertyMessage(String fieldName) {
		String msg = MessageBundleLoader.getMessage(
				"javax.faces.component.UIInput.REQUIRED",
				new Object[] { fieldName });
		showErrorMessage(msg);
	}

	protected String getRealPath(String relPath) {
		String path = relPath;
		try {
			URL url = getFacesContext().getExternalContext().getResource(
					relPath);
			if (url != null) {
				path = url.getPath();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return path;
	}

	/**
	 * <p>
	 * Adiciona um parametro de callback no {@link RequestContext}.
	 * </p>
	 * 
	 * @param name
	 *            - nome do par�metro.
	 * @param value
	 *            - valor do par�metro.
	 */
	protected void addCallbackParam(String name, Object value) {
		RequestContext requestContext = RequestContext.getCurrentInstance();
		requestContext.addCallbackParam(name, value);
	}

	public Date getToday() {
		return new Date();
	}

	/**
	 * Retorna o id do objeto passado.
	 * 
	 * @param <B>
	 *            - Objeto desejado
	 * @param b
	 *            - Objeto
	 * @return Identificador do objeto
	 */
	protected <B extends BaseORM> Long extrairId(B b) {
		return b == null ? null : (Long) b.getId();
	}

	/**
	 * Verifica se um dos par�metros foi preenchido.
	 * 
	 * @param parameters
	 *            Lista de par�metros
	 * @return
	 */
	protected boolean isSelectOne(Object... parameters) {
		for (Object object : parameters) {
			if (object != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param key
	 * @param objectToSession
	 */
	protected final void putSession(String key, Object objectToSession) {
		putSession(key, objectToSession, false);
	}

	/**
	 * @param key
	 * @param objectToSession
	 */
	protected final void putSession(String key, Object objectToSession,
			boolean create) {
		HttpServletRequest request = getRequest();
		request.getSession(create).setAttribute(key, objectToSession);
	}

	/**
	 * @return
	 */
	protected final HttpServletRequest getRequest() {
		return (HttpServletRequest) getFacesContext().getExternalContext()
				.getRequest();
	}

	/**
	 * Retorna os identificadores dos grupos selecionados.
	 * 
	 * @param <B>
	 *            - Classe que implementa {@link BaseORM}
	 * @param tipo
	 *            - Vetor de {@link BaseORM}
	 * @return Lista de identificadores
	 */
	protected <B extends BaseORM> List<Long> extrairIds(B[] tipo) {
		List<Long> ids = new ArrayList<Long>();
		if (tipo.length == 0) {
			throw new RuntimeException(MessageBundleLoader.getMessage("critica.noregistro"));
		}
		for (int x = 0; x < tipo.length; x++) {
			ids.add((Long) tipo[x].getId());
		}
		return ids;
	}

	/**
	 * Retorna os identificadores dos grupos selecionados.
	 * 
	 * @param <B>
	 *            - Classe que implementa {@link BaseORM}
	 * @param tipo
	 *            - Vetor de {@link BaseORM}
	 * @return Lista de identificadores
	 */
	protected <B extends BaseORM> List<Long> extrairIds(List<B> list) {
		List<Long> ids = new ArrayList<Long>();
		if (list == null || list.isEmpty()) {
			throw new RuntimeException(MessageBundleLoader.getMessage("critica.noregistro"));
		}
		for (B b : list) {
			ids.add((Long) b.getId());
		}
		return ids;
	}

	/**
	 * Obt�m um componente do Primefaces atrav�s do seu id.
	 * 
	 * @param idComponent
	 * @return
	 */
	protected UIComponent getComponent(String idComponent) {
		return FacesContext.getCurrentInstance().getViewRoot()
				.findComponent(idComponent);
	}

	/**
	 * Redefine a pagina��o de um {@link DataTable}.
	 * 
	 * @param idDataTable
	 *            - Identificador do {@link DataTable}
	 */
	protected void resetPagination(String idDataTable) {
		DataTable dataTable = (DataTable) getComponent(idDataTable);
		dataTable.setFirst(0);
	}

	/**
	 * Define a aba que ser� exibida no componente.
	 * 
	 * @param index
	 */
	protected void setActiveIndexTab(int index, String idTab) {
		TabView tab = (TabView) getComponent(idTab);
		tab.setActiveIndex(index);
	}

	protected void setActiveIndexTab(String idTab) {
		setActiveIndexTab(0, idTab);
	}
	
	/**
	 * <p>
	 * Adiciona um parametro de callback no {@link RequestContext}.
	 * </p>
	 * 
	 * @param name
	 *            - nome do par�metro.
	 * @param value
	 *            - valor do par�metro.
	 */
	protected void addCallBackParam(String name, Object value) {
		RequestContext requestContext = RequestContext.getCurrentInstance();
		requestContext.addCallbackParam(name, value);
	}
	
	/**
     * 
     */
	protected void validationFailed() {
		addCallBackParam("validationFailed", true);
	}
	
	protected void redirect(String url){
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(url);
		} catch (IOException e) {
			showErrorMessage(MessageBundleLoader.getMessage("critica.redirecionamentopagina"));
			validationFailed();
		}
	}
	
	protected UsuarioAutenticado getCurrentUser(){
		Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (o instanceof UsuarioAutenticado){
			putSession(SK_USER_CTX, o);
			return (UsuarioAutenticado) o;
		}
		return null;
	}
	
	protected Long getCurrentUserId(){
		UsuarioAutenticado user = getCurrentUser();
		if (user != null){
			return user.getId();
		}
		return null;
	}
	
	protected void navigate(String outcome){
		ConfigurableNavigationHandler navigateHandler = (ConfigurableNavigationHandler)
				getFacesContext().getApplication().getNavigationHandler();
		navigateHandler.performNavigation(outcome + "?faces-redirect=true");
	}
	
	protected String formatarData(Date data, String formato) {
		if(data != null){
	        SimpleDateFormat sdf = new SimpleDateFormat(formato);
	        String dataFormatada = sdf.format(data);
	        return dataFormatada;
		} else {
			return "";
		}
    }
	
	protected Date merge(Date data, Date hora) {
		return DataTimeUtils.merge(data, hora);
	}
	
	protected Date getDateNow(){
		LocalDateTime agora = LocalDateTime.now();
	    ZoneId zoneId = ZoneId.of("UTC");
	    ZonedDateTime zdt = agora.atZone(zoneId);
	    return Date.from(zdt.toInstant());
	}
	
	protected boolean isVigente(Date data){
		int result = data.compareTo(getDateNow());
		if(result > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public DiferencaData calcDifDate(Date dataInicio, Date dataFim) {
		
		LocalDateTime inicio = LocalDateTime.ofInstant(dataInicio.toInstant(), ZoneId.of("UTC"));
	    LocalDateTime fim = LocalDateTime.ofInstant(dataFim.toInstant(), ZoneId.of("UTC"));
		
	    Duration duracao = Duration.between(inicio, fim);
	    long diferenca = Math.abs(duracao.toMinutes());

	    if (diferenca >= 1440) { // Mais de um dia
	        diferenca = diferenca / 1440;
	        return new DiferencaData(diferenca, "dias");
	    } else if (diferenca >= 60) { // Mais de uma hora
	        diferenca = diferenca / 60;
	        return new DiferencaData(diferenca, "horas");
	    } else { // Menos de uma hora
	        return new DiferencaData(diferenca, "minutos");
	    }
	}
	
	public boolean dataEstaDentroPeriodo(Date inicio, Date fim, Date compararInicio, Date compararFim){
		if(compararInicio.before(inicio) && compararFim.after(fim)){
			return true;
		}
		else if(compararInicio.after(inicio) && compararInicio.before(fim)){
			return true;
		}
		else if(compararFim.before(fim) && compararFim.after(inicio)){
			return true;
		} else {
			return false;
		}
	}
}
