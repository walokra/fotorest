package com.ruleoftech.lab.fotorest;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ruleoftech.lab.fotorest.model.GalleryImage;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Title("Fotorest")
@SessionScoped
@PreserveOnRefresh
@Theme("default")
@SuppressWarnings("serial")
public class FotorestUI extends UI {

	private static final Logger LOGGER = LoggerFactory.getLogger(FotorestUI.class);

	private final HorizontalLayout top = new HorizontalLayout();
	private final Button listButton = new Button("Hot");
	private final Button randomButton = new Button("Rnd");
	private final TextField searchField = new TextField();
	private final Table photoList = new Table();
	private final VerticalLayout left = new VerticalLayout();

	private final VerticalLayout right = new VerticalLayout();
	private final Embedded image = new Embedded();
	private final HorizontalLayout rightBottom = new HorizontalLayout();
	private final Label credits = new Label();

	BeanContainer<String, GalleryImage> images = new BeanContainer<String, GalleryImage>(GalleryImage.class);
	// Container images = new IndexedContainer();

	@Inject
	private RestService service;

	@Override
	protected void init(VaadinRequest request) {
		initLayout();
		initFotoList();
	}

	private void initLayout() {
		HorizontalSplitPanel split = new HorizontalSplitPanel();
		setContent(split);

		split.addComponent(left);
		split.addComponent(right);
		left.addComponent(top);
		left.addComponent(photoList);

		// Create list button and search field
		initSearch();

		left.setSizeFull();
		left.setExpandRatio(photoList, 1);
		photoList.setSizeFull();

		split.setSplitPosition(33f);
		split.setSizeFull();

		// set container
		images.setBeanIdProperty("id");

		right.addComponent(image);
		right.addComponent(rightBottom);
		right.setExpandRatio(image, 1);
		image.setSizeFull();
		right.setMargin(true);

		rightBottom.addComponent(credits);
	}

	private void initSearch() {
		listButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				LOGGER.trace("{'method':'listButton.buttonClick'}");
				images.removeAllItems();
				for (GalleryImage i : service.hotImages()) {
					images.addBean(i);
					images.addNestedContainerProperty("link");
				}
				credits.setValue(service.getCredits());
			}
		});
		top.addComponent(listButton);

		randomButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				LOGGER.trace("{'method':'randomButton.buttonClick'}");
				images.removeAllItems();
				for (GalleryImage i : service.randomImages()) {
					images.addBean(i);
					images.addNestedContainerProperty("link");
				}
				credits.setValue(service.getCredits());
			}
		});
		top.addComponent(randomButton);

		searchField.setInputPrompt("Search imgur");
		top.addComponent(searchField);

		top.setWidth("100%");
		searchField.setWidth("100%");
		top.setExpandRatio(searchField, 1);

		searchField.addShortcutListener(new ShortcutListener("Default action", ShortcutAction.KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				LOGGER.trace("{'method':'searchField', 'value':'" + searchField.getValue() + "'}");
				images.removeAllItems();
				for (GalleryImage i : service.searchImages(searchField.getValue())) {
					images.addBean(i);
					images.addNestedContainerProperty("link");
				}
				credits.setValue(service.getCredits());
			}
		});
	}

	private void initFotoList() {
		photoList.setContainerDataSource(images);
		photoList.setVisibleColumns(new String[] { "title", "link" });
		photoList.setSelectable(true);
		photoList.setImmediate(true);
		photoList.setColumnExpandRatio("title", 2);
		photoList.setColumnExpandRatio("link", 2);

		photoList.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				Object itemId = photoList.getValue();
				if (itemId != null) {
					GalleryImage gi = (GalleryImage) ((BeanItem) photoList.getItem(itemId)).getBean();
					LOGGER.trace("{'method':'photoList.valueChange', 'value':'" + gi.getUrl() + "'}");
					if (!gi.isIs_album()) {
						String[] tokens = gi.getUrl().split("\\.(?=[^\\.]+$)");
						ExternalResource img = new ExternalResource(tokens[0] + "l" + "." + tokens[1]);
						image.setSource(img);
						right.addComponent(image);
					}
					LOGGER.trace("{'method':'photoList.valueChange', 'debug':'is_album=" + gi.isIs_album() + "'}");
				}
			}
		});
	}
}
