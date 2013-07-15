package com.ruleoftech.lab.fotorest.cdi;

import javax.inject.Inject;

import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
// If using JEE6 (problems with UI id)
// @WebServlet(urlPatterns = "/*", initParams = { @WebInitParam(name = "UIProvider", value =
// "com.ruleoftech.lab.fotorest.jee6.Jee6UIProvider") })
public class Jee6VaadinServlet extends VaadinServlet {

	@Inject
	private UI ui;

	public UI getUI() {
		return ui;
	}

}
