package com.ruleoftech.lab;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ruleoftech.lab.model.GalleryImage;
import com.ruleoftech.lab.service.RestService;

@SuppressWarnings("serial")
public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(HomePage.class);

	@SpringBean
	private RestService restService;

	public HomePage(final PageParameters parameters) {
		super(parameters);

		Form<Void> form = new Form<Void>("form");
		initActions(form);

		form.add(new Label("credits", restService.getCredits()));
		form.add(new Label("wicketVersion", getApplication().getFrameworkSettings().getVersion()));
	}

	private void initActions(Form<?> form) {

		form.add(new AjaxButton("hotButton") {
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				LOGGER.trace("{'method':'hotButton.onSubmit'}");
				List<GalleryImage> gi = restService.hotImages();
			}
		});
		form.add(new Button("randomButton"));
		form.add(new TextField<String>("search", new Model<String>()));
		add(form);
	}
}
