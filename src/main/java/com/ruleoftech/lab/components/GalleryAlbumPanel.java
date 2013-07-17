package com.ruleoftech.lab.components;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ruleoftech.lab.model.ImgurImage;

@SuppressWarnings("serial")
public class GalleryAlbumPanel extends Panel {
	private static final Logger LOGGER = LoggerFactory.getLogger(GalleryAlbumPanel.class);

	public GalleryAlbumPanel(String id, ListModel<ImgurImage> model) {
		super(id, model);
		LOGGER.trace("{'method':'GalleryAlbumPanel.constructor'}");
		add(newList(model));
	}

	private Component newList(IModel<List<ImgurImage>> listModel) {
		List<ImgurImage> images = listModel.getObject();

		final WebMarkupContainer list = new WebMarkupContainer("galleryImages");
		final ListView<ImgurImage> items = new ListView<ImgurImage>("items", images) {
			private static final long serialVersionUID = 1L;

			@Override
			protected ListItem<ImgurImage> newItem(int index, IModel<ImgurImage> model) {
				ListItem<ImgurImage> item = super.newItem(index, model);
				return item;
			}

			@Override
			protected void populateItem(ListItem<ImgurImage> item) {
				Label label = new Label("title", new PropertyModel<String>(item.getModel(), "title"));
				item.add(label);

				WebComponent image = new ExternalImage("image", "link");
				item.add(image);
			}
		};

		list.add(items);
		items.setReuseItems(true);
		items.setOutputMarkupId(true);

		return list;
	}

}
