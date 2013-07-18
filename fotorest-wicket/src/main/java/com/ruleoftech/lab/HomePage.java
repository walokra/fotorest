package com.ruleoftech.lab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.googlecode.wicket.jquery.ui.panel.JQueryFeedbackPanel;
import com.ruleoftech.lab.components.ExternalImage;
import com.ruleoftech.lab.components.GalleryImageDataProvider;
import com.ruleoftech.lab.components.LinkPropertyColumn;
import com.ruleoftech.lab.exception.BusinessException;
import com.ruleoftech.lab.model.GalleryAlbum;
import com.ruleoftech.lab.model.GalleryImage;
import com.ruleoftech.lab.model.ImgurImage;
import com.ruleoftech.lab.service.RestService;

@SuppressWarnings("serial")
public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(HomePage.class);

	private final FeedbackPanel feedback;
	private List<GalleryImage> galleryImages;
	private final Label galleryImageTitle;
	private final Model<String> galleryImageTitleModel;
	private RefreshingView<ImgurImage> imageListView;
	private List<IModel<ImgurImage>> images;
	private final Form<Void> form;

	@SpringBean
	private RestService restService;

	public HomePage(final PageParameters parameters) {
		super(parameters);

		form = new Form<Void>("form");
		initActions();

		// Add feedbackpanel for showing messages
		feedback = new JQueryFeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		form.add(feedback);

		// init list to contain some images
		try {
			galleryImages = restService.hotImages();
		} catch (BusinessException e) {
			error(e.getMessage());
		}
		initTable();

		// set image panel components
		galleryImageTitleModel = new Model<String>();
		galleryImageTitle = new Label("galleryImageTitle", galleryImageTitleModel);
		galleryImageTitle.setOutputMarkupId(true);
		form.add(galleryImageTitle);
		createGalleryImagesPanel();

		initFooter();

		add(form);
	}

	private void initActions() {

		form.add(new AjaxButton("hotButton") {
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				LOGGER.trace("{'method':'hotButton.onSubmit'}");
				info("Fetching hot images");
				try {
					galleryImages = restService.hotImages();
				} catch (BusinessException e) {
					error(e.getMessage());
				}
				target.add(form);
			}
		});
		form.add(new AjaxButton("randomButton") {
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				LOGGER.trace("{'method':'randomButton.onSubmit'}");
				info("Fetching random images");
				try {
					galleryImages = restService.randomImages();
				} catch (BusinessException e) {
					error(e.getMessage());
				}
				target.add(form);
			}
		});

		final TextField<String> search = new TextField<String>("search", new Model<String>());
		form.add(search);

		AjaxButton searchButton = new AjaxButton("searchButton") {
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				LOGGER.trace("{'method':'searchButton.onSubmit'}");
				info("Searching the gallery");
				try {
					galleryImages = restService.searchImages(search.getValue());
				} catch (BusinessException e) {
					error(e.getMessage());
				}
				target.add(form);
			}
		};
		form.add(searchButton);
		form.setDefaultButton(searchButton);
	}

	private void initFooter() {
		String credits = "";
		try {
			credits = restService.getCredits();
		} catch (BusinessException e) {
			error(e.getMessage());
		}
		form.add(new Label("credits", credits));

		form.add(new Label("wicketVersion", getApplication().getFrameworkSettings().getVersion()));
	}

	private void initTable() {
		// DataTable
		// resources for working with datatable:
		// http://www.packtpub.com/article/apache-wicket-displaying-data-using-datatable
		final AjaxFallbackDefaultDataTable<GalleryImage, String> table = new AjaxFallbackDefaultDataTable<GalleryImage, String>(
				"datatable", createColumns(), createDataProvider(), 50);
		form.add(table);
	}

	private ISortableDataProvider<GalleryImage, String> createDataProvider() {

		return new GalleryImageDataProvider<GalleryImage>() {
			@Override
			public List<GalleryImage> getData() {
				return galleryImages;
			}
		};
	}

	private List<IColumn<GalleryImage, String>> createColumns() {
		List<IColumn<GalleryImage, String>> columns = new ArrayList<IColumn<GalleryImage, String>>();
		columns.add(new AbstractColumn<GalleryImage, String>(new Model<String>("Title"), "title") {
			@Override
			public void populateItem(Item<ICellPopulator<GalleryImage>> cellItem, String componentId,
					final IModel<GalleryImage> rowModel) {
				Label label = new Label(componentId, new PropertyModel<GalleryImage>(rowModel, "title"));
				label.add(new AjaxEventBehavior("onclick") {
					@Override
					protected void onEvent(AjaxRequestTarget target) {
						GalleryImage gi = rowModel.getObject();
						galleryImageTitleModel.setObject(gi.getTitle());
						target.add(galleryImageTitle);
						images = new ArrayList<IModel<ImgurImage>>();

						// Setting gallery image or gallery album images
						if (gi.isIs_album()) {
							String[] tokens = gi.getLink().split("\\/(?=[^\\/]+$)");
							GalleryAlbum album = new GalleryAlbum();
							try {
								album = restService.getGalleryAlbum(tokens[1]);
								LOGGER.trace("{'method':'table.title.onClick.album', 'debug':'{}'}", album.toString());

								for (ImgurImage i : Arrays.asList(album.getImages())) {
									i.setLink(getExternalResourceFromUrl(i.getLink(), i.getWidth(), i.getHeight()));
									Model<ImgurImage> img = new Model<ImgurImage>();
									img.setObject(i);
									images.add(img);
								}
							} catch (BusinessException be) {
								error(be.getMessage());
							}
						} else {
							LOGGER.trace("{'method':'table.title.onClick.image', 'debug':'{}'}", gi.toString());
							ImgurImage i = new ImgurImage();
							BeanUtils.copyProperties(gi, i);
							i.setTitle(null);
							i.setLink(getExternalResourceFromUrl(i.getLink(), i.getWidth(), i.getHeight()));
							Model<ImgurImage> img = new Model<ImgurImage>();
							img.setObject(i);
							images.add(img);
						}
						target.add(form);
					}

				});
				cellItem.add(label);
			}
		});
		// TODO: could also use ExternalLink
		columns.add(new LinkPropertyColumn<GalleryImage>(new Model<String>("Link"), "link", "link") {
			@Override
			public void onClick(Item<ICellPopulator<GalleryImage>> item, String componentId,
					IModel<GalleryImage> model, AjaxRequestTarget target) {
				LOGGER.trace("{'method':'table.link.onClick'}");
				target.appendJavaScript("window.open('" + model.getObject().getLink() + "')");
			}
		});

		return columns;
	}

	private void createGalleryImagesPanel() {
		images = new ArrayList<IModel<ImgurImage>>();
		imageListView = new RefreshingView<ImgurImage>("galleryAlbumPanel") {

			@Override
			protected Iterator<IModel<ImgurImage>> getItemModels() {
				return images.iterator();
			}

			@Override
			protected void populateItem(Item<ImgurImage> item) {
				Label label = new Label("title", new PropertyModel<String>(item.getModelObject(), "title"));
				label.setOutputMarkupPlaceholderTag(true);
				if (item.getModelObject().getTitle() == null) {
					label.setVisible(false);
				} else {
					label.setVisible(true);
				}
				item.add(label);

				WebComponent image = new ExternalImage("image", item.getModelObject().getLink());
				image.setOutputMarkupPlaceholderTag(true);
				item.add(image);
			}
		};
		imageListView.setOutputMarkupId(true);
		form.add(imageListView);
	}

	/**
	 * Parsing the image link and creating externalresource.
	 * 
	 * @param url
	 *            link to image like http://i.imgur...
	 * @param width
	 *            image's width
	 * @param height
	 *            image's height
	 * @return ExternalResource
	 */
	private String getExternalResourceFromUrl(String url, Integer width, Integer height) {
		if (width > 640 || height > 640) {
			// LOGGER.trace("{'method':'photoList.valueChange', 'debug':'Showing large thumbnail'}");
			// Get the image thumbnail url
			String[] tokens = url.split("\\.(?=[^\\.]+$)");
			return tokens[0] + "l" + "." + tokens[1];
		} else {
			// LOGGER.trace("{'method':'photoList.valueChange', 'debug':'Showing original image'}");
			return url;
		}
	}
}
