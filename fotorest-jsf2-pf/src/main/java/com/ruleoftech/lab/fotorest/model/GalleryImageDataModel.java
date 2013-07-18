package com.ruleoftech.lab.fotorest.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GalleryImageDataModel extends ListDataModel<GalleryImage> implements SelectableDataModel<GalleryImage> {
	private static final Logger LOGGER = LoggerFactory.getLogger(GalleryImageDataModel.class);

	public GalleryImageDataModel() {
	}

	public GalleryImageDataModel(List<GalleryImage> data) {
		super(data);
	}

	@Override
	public GalleryImage getRowData(String rowKey) {
		LOGGER.trace("{'method':'getRowData', 'debug':'{}'}", rowKey);
		// In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data

		List<GalleryImage> images = (List<GalleryImage>) getWrappedData();

		for (GalleryImage image : images) {
			if (image.getId().equals(rowKey)) {
				return image;
			}
		}

		return null;
	}

	@Override
	public Object getRowKey(GalleryImage image) {
		return image.getId();
	}
}
