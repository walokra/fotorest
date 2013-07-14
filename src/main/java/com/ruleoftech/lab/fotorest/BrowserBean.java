package com.ruleoftech.lab.fotorest;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ruleoftech.lab.fotorest.model.GalleryAlbum;
import com.ruleoftech.lab.fotorest.model.GalleryImage;
import com.ruleoftech.lab.fotorest.model.GalleryImageDataModel;
import com.ruleoftech.lab.fotorest.services.RestService;

@SessionScoped
@Named
public class BrowserBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(BrowserBean.class);

	@Inject
	private RestService service;

	private GalleryImage selectedImage;
	private GalleryImageDataModel dataModel;

	private String search;

	@PostConstruct
	public void init() {
		LOGGER.trace("{'method':'init'}");
		System.out.print("{'method':'init'}");

		this.dataModel = new GalleryImageDataModel(service.hotImages());
	}

	public void hot() {
		FacesMessage msg = new FacesMessage("Fetching hot images");
		FacesContext.getCurrentInstance().addMessage(null, msg);

		// TODO: actually get hot images and update datamodel
	}

	public void random() {
		FacesMessage msg = new FacesMessage("Fetching random images");
		FacesContext.getCurrentInstance().addMessage(null, msg);

		// TODO: actually get random images and update datamodel
	}

	public void onRowSelect(SelectEvent event) {
		LOGGER.trace("{'method':'onRowSelect'}");
		selectedImage = (GalleryImage) event.getObject();

		if (selectedImage != null) {
			LOGGER.trace("{'method':'photoList.valueChange', 'debug':'{}'}", selectedImage.toString());
			if (!selectedImage.isIs_album()) {
				// Show original image if smaller than large thumbnail (640x640)
				selectedImage.setLink(getExternalResourceFromUrl(selectedImage.getLink(), selectedImage.getWidth(),
						selectedImage.getHeight()));
			} else {
				String[] tokens = selectedImage.getLink().split("\\/(?=[^\\/]+$)");
				GalleryAlbum album = service.getGalleryAlbum(tokens[1]);
				LOGGER.trace("{'method':'onRowSelect.album', 'debug':'{}'}", album.toString());

				// for (com.ruleoftech.lab.fotorest.model.Image i : Arrays.asList(album.getImages())) {
				// Image image = new Image();
				// image.setSource(getExternalResourceFromUrl(i.getLink(), i.getWidth(), i.getHeight()));
				// }
				FacesMessage facesMessage = new FacesMessage("This is a message");
				facesMessage.setSeverity(FacesMessage.SEVERITY_INFO);
				FacesContext.getCurrentInstance().addMessage(null, facesMessage);
			}
		}
	}

	public void onRowUnselect(UnselectEvent event) {
		FacesMessage msg = new FacesMessage("Image unselected", ((GalleryImage) event.getObject()).getTitle());

		FacesContext.getCurrentInstance().addMessage(null, msg);
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

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

}
