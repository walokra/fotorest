package com.ruleoftech.lab;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start
 * class.
 * 
 * @see com.ruleoftech.lab.Start#main(String[])
 */
public class WicketApplication extends WebApplication {
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage() {
		return HomePage.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init() {
		super.init();

		// add your configuration here
		getDebugSettings().setDevelopmentUtilitiesEnabled(true);

		// Integrate Spring with Wicket
		getComponentInstantiationListeners().add(new SpringComponentInjector(this));

		// Strip wicket tags from the HTML rendering as suggested with wicket-jquery-ui
		this.getMarkupSettings().setStripWicketTags(true); // IMPORTANT!
	}
}
