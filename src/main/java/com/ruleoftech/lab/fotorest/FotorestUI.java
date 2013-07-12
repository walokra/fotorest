package com.ruleoftech.lab.fotorest;

import java.util.ResourceBundle;

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
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

@Title("Fotorest")
@SessionScoped
@PreserveOnRefresh
@Theme("default")
@SuppressWarnings("serial")
public class FotorestUI extends UI {

	private static final Logger LOGGER = LoggerFactory.getLogger(FotorestUI.class);

	private final HorizontalLayout header = new HorizontalLayout();
	private final VerticalLayout content = new VerticalLayout();
	private final Button listButton = new Button("Hot");
	private final Button randomButton = new Button("Rnd");
	private final TextField searchField = new TextField();
	private final Table photoList = new Table();
	private final VerticalLayout left = new VerticalLayout();

	private final VerticalLayout right = new VerticalLayout();
	private final Embedded image = new Embedded();
	private final Label credits = new Label();

	private final BeanContainer<String, GalleryImage> images = new BeanContainer<String, GalleryImage>(
			GalleryImage.class);
	private final ResourceBundle bundle = ResourceBundle.getBundle("resources");

	@Inject
	private RestService service;

	@Override
	protected void init(VaadinRequest request) {
		initLayout();
		initFotoList();

	}

	private void initLayout() {
		// main container
		VerticalLayout container = new VerticalLayout();
		container.setSizeFull();
		setContent(container);
		container.setMargin(true);

		// header
		container.addComponent(header);
		initHeader();

		// main content which contains photolist and image panel
		content.setSizeFull();
		container.addComponent(content);

		// split panel in main content for photolist and image panel
		HorizontalSplitPanel split = new HorizontalSplitPanel();
		content.addComponent(split);

		// header is 1:10 of the full container
		// container.setExpandRatio(header, 1);
		container.setExpandRatio(content, 1);
		content.setExpandRatio(split, 1);

		// Adding panels to split container
		split.addComponent(left);
		split.addComponent(right);
		left.addComponent(photoList);

		left.setSizeFull();
		left.setExpandRatio(photoList, 1); // photolist expands
		photoList.setSizeFull();

		split.setSplitPosition(33f);
		split.setSizeFull();

		// setting footer for version info
		initFooter();

		// set container for gallery data
		images.setBeanIdProperty("id");

		// creating right side's image panel
		right.addComponent(image);
		right.setExpandRatio(image, 1);
		image.setSizeFull();
		right.setHeight("100%");
		right.setMargin(true);

		// show credits
		credits.setValue(service.getCredits());
	}

	private void initHeader() {
		header.setWidth("100%");

		VerticalLayout headerContent = new VerticalLayout();

		HorizontalLayout titleLayout = new HorizontalLayout();
		Label title = new Label(bundle.getString("title"));
		title.setStyleName(Reindeer.LABEL_H1);
		titleLayout.addComponent(title);
		titleLayout.setWidth("100%");

		HorizontalLayout actionLayout = new HorizontalLayout();
		// Create list button and search field
		initSearch(actionLayout);
		actionLayout.setWidth("33%");
		actionLayout.setSpacing(true);

		HorizontalLayout infoLayout = new HorizontalLayout();
		infoLayout.setWidth("33%");

		headerContent.addComponent(titleLayout);
		headerContent.addComponent(actionLayout);
		headerContent.addComponent(infoLayout);

		header.addComponent(headerContent);
	}

	private void initFooter() {
		HorizontalLayout footerLayout = new HorizontalLayout();
		footerLayout.setWidth("33%");

		credits.setWidth("100%");
		footerLayout.addComponent(credits);

		Label version = new Label(bundle.getString("version"));
		footerLayout.addComponent(version);
		Label sep = new Label(" | ");
		sep.setWidth(Sizeable.SIZE_UNDEFINED, Unit.PERCENTAGE);
		footerLayout.addComponent(sep);
		version.setWidth(Sizeable.SIZE_UNDEFINED, Unit.PERCENTAGE);
		Label build = new Label(bundle.getString("version.build"));
		footerLayout.addComponent(build);
		build.setWidth(Sizeable.SIZE_UNDEFINED, Unit.PERCENTAGE);

		footerLayout.setExpandRatio(credits, 1);

		content.addComponent(footerLayout);
	}

	private void initSearch(HorizontalLayout actionLayout) {
		listButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				LOGGER.trace("{'method':'listButton.buttonClick'}");
				images.removeAllItems();
				for (GalleryImage i : service.hotImages()) {
					images.addBean(i);
					images.addNestedContainerProperty("link");
				}
				// credits.setValue(service.getCredits());
			}
		});
		listButton.setDescription("Show images of today, sorted by popularity");
		actionLayout.addComponent(listButton);

		randomButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				LOGGER.trace("{'method':'randomButton.buttonClick'}");
				images.removeAllItems();
				for (GalleryImage i : service.randomImages()) {
					images.addBean(i);
					images.addNestedContainerProperty("link");
				}
				// credits.setValue(service.getCredits());
			}
		});
		randomButton.setDescription("Show random set of gallery images");
		actionLayout.addComponent(randomButton);

		searchField.setInputPrompt("Search imgur");
		actionLayout.addComponent(searchField);

		searchField.setWidth("100%");
		actionLayout.setExpandRatio(searchField, 1);

		searchField.addShortcutListener(new ShortcutListener("Default action", ShortcutAction.KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				LOGGER.trace("{'method':'searchField', 'value':'" + searchField.getValue() + "'}");
				images.removeAllItems();
				for (GalleryImage i : service.searchImages(searchField.getValue())) {
					images.addBean(i);
					images.addNestedContainerProperty("link");
				}
				// credits.setValue(service.getCredits());
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
					} else {
						// https://vaadin.com/book/-/page/application.notifications.html
						Notification.show("Can't show image", "Gallery image is an album",
								Notification.Type.HUMANIZED_MESSAGE);
					}
					LOGGER.trace("{'method':'photoList.valueChange', 'debug':'{}'}", gi.toString());
				}
			}
		});
	}
}
