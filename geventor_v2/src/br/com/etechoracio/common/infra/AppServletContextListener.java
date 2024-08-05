package br.com.etechoracio.common.infra;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AppServletContextListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) { }

	public void contextInitialized(ServletContextEvent arg0) {
		System.getProperties().put("org.apache.el.parser.COERCE_TO_ZERO", "false");
	}

}
