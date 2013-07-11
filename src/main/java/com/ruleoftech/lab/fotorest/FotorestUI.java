package com.ruleoftech.lab.fotorest;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Title("Fotorest")
// @CDIUI(value = "fotorest")
@SessionScoped
@PreserveOnRefresh
@Theme("default")
@SuppressWarnings("serial")
public class FotorestUI extends UI {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(FotorestUI.class);

	private final FormLayout searchForm = new FormLayout();
	private final TextField searchField = new TextField();
	private final Table photoList = new Table();

	@Inject
	private RestService service;

	@Override
	protected void init(VaadinRequest request) {
		initLayout();
		initSearch();
	}

	private void initLayout() {
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout);

		HorizontalLayout top = new HorizontalLayout();
		top.addComponent(searchForm);
		top.setWidth("100%");
		layout.addComponent(top);
		top.setExpandRatio(searchForm, 1);

		VerticalLayout content = new VerticalLayout();
		content.setSizeFull();

		VerticalLayout left = new VerticalLayout();
		VerticalLayout right = new VerticalLayout();

		HorizontalSplitPanel split = new HorizontalSplitPanel();
		split.addComponent(left);
		split.addComponent(right);
		left.addComponent(photoList);

		left.setSizeFull();
		left.setExpandRatio(photoList, 1);
		photoList.setSizeFull();

		content.addComponent(split);

		layout.addComponent(content);
	}

	private void initSearch() {
		searchField.setInputPrompt("Search photos");
		searchField.setWidth("100%");
		searchForm.addComponent(searchField);

		searchField.addShortcutListener(new ShortcutListener("Default action",
				ShortcutAction.KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				LOGGER.trace("{'method':'searchField', 'value':'"
						+ searchField.getValue() + "'}");
				searchForm.addComponent(new Label(service.search(searchField
						.getValue())));
			}
		});
	}

}
