package com.ruleoftech.lab.fotorest;

import java.io.Serializable;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.graphicimage.GraphicImage;
import org.primefaces.component.outputpanel.OutputPanel;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ruleoftech.lab.fotorest.model.GalleryAlbum;
import com.ruleoftech.lab.fotorest.model.GalleryImage;
import com.ruleoftech.lab.fotorest.model.GalleryImageDataModel;
import com.ruleoftech.lab.fotorest.model.Image;
import com.ruleoftech.lab.fotorest.services.RestService;

@SuppressWarnings("serial")
@SessionScoped
@Named
public class BrowserBean implements Serializable {

	private static final Logger LOGGER = LoggerFactory.getLogger(BrowserBean.class);

	@Inject
	@Named("restService")
	private RestService service;

	private GalleryImage selectedImage;
	private GalleryImageDataModel dataModel;

	private String query;
	private String credits;

	private OutputPanel imagePanel;

	@PostConstruct
	public void init() {
		LOGGER.trace("{'method':'init'}");
		System.out.print("{'method':'init'}");

		if (dataModel == null) {
			this.dataModel = new GalleryImageDataModel(service.hotImages());
		}
		credits = service.getCredits();
		imagePanel = new OutputPanel();
	}

	public void hot() {
		FacesMessage msg = new FacesMessage("Fetching hot images");
		FacesContext.getCurrentInstance().addMessage(null, msg);

		this.dataModel = new GalleryImageDataModel(service.hotImages());
	}

	public void random() {
		FacesMessage msg = new FacesMessage("Fetching random images");
		FacesContext.getCurrentInstance().addMessage(null, msg);

		this.dataModel = new GalleryImageDataModel(service.randomImages());
	}

	public void search() {
		LOGGER.trace("{'method':'search', 'debug':''}");

		FacesMessage msg = new FacesMessage("Searching images");
		FacesContext.getCurrentInstance().addMessage(null, msg);

		this.dataModel = new GalleryImageDataModel(service.searchImages(query));
	}

	public void onRowSelect(SelectEvent event) {
		selectedImage = (GalleryImage) event.getObject();
		imagePanel = new OutputPanel();

		if (selectedImage != null) {
			LOGGER.trace("{'method':'photoList.valueChange', 'debug':'{}'}", selectedImage.toString());
			if (!selectedImage.isIs_album()) {
				// Show original image if smaller than large thumbnail (640x640)
				// selectedImage.setLink(getExternalResourceFromUrl(selectedImage.getLink(), selectedImage.getWidth(),
				// selectedImage.getHeight()));
				GraphicImage image = new GraphicImage();
				image.setUrl(getExternalResourceFromUrl(selectedImage.getLink(), selectedImage.getWidth(),
						selectedImage.getHeight()));
				imagePanel.getChildren().add(image);
			} else {
				String[] tokens = selectedImage.getLink().split("\\/(?=[^\\/]+$)");
				GalleryAlbum album = service.getGalleryAlbum(tokens[1]);
				LOGGER.trace("{'method':'onRowSelect.album', 'debug':'{}'}", album.toString());

				for (Image i : Arrays.asList(album.getImages())) {
					GraphicImage image = new GraphicImage();
					image.setUrl(getExternalResourceFromUrl(i.getLink(), i.getWidth(), i.getHeight()));
					image.setStyle("padding: 5px; clear: both;");
					imagePanel.getChildren().add(image);

					HtmlOutputText title = new HtmlOutputText();
					title.setValue(i.getTitle());
					title.setStyle("padding: 5px; display: block;");
					imagePanel.getChildren().add(title);
				}
			}
		}
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
			// Get the image thumbnail
			String[] tokens = url.split("\\.(?=[^\\.]+$)");
			return tokens[0] + "l" + "." + tokens[1];
		} else {
			// LOGGER.trace("{'method':'photoList.valueChange', 'debug':'Showing original image'}");
			return url;
		}
	}

	public GalleryImage getSelectedImage() {
		return selectedImage;
	}

	public void setSelectedImage(GalleryImage selectedImage) {
		this.selectedImage = selectedImage;
	}

	public GalleryImageDataModel getDataModel() {
		return dataModel;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getCredits() {
		return credits;
	}

	public OutputPanel getImagePanel() {
		return imagePanel;
	}

	public void setImagePanel(OutputPanel imagePanel) {
		this.imagePanel = imagePanel;
	}

}
