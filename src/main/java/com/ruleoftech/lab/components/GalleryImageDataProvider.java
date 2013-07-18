package com.ruleoftech.lab.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;

public abstract class GalleryImageDataProvider<T extends Serializable> extends SortableDataProvider<T, String> {

	private static final long serialVersionUID = 1L;

	private boolean dataReady;
	private List<T> dataList;

	public GalleryImageDataProvider() {
		super();
		this.dataReady = false;

		// set the default sort
		setSort("firstname", SortOrder.ASCENDING);
	}

	public abstract List<T> getData();

	private List<T> getDataInternal() {
		if (!dataReady) {
			this.dataList = getData();
			this.dataReady = true;
		}
		if (dataList == null) {
			this.dataList = new ArrayList<T>();
		}
		return this.dataList;
	}

	/**
	 * Gets an iterator for the subset of contacts.
	 * 
	 * @param first
	 *            offset for the first row of data to retrieve
	 * @param count
	 *            number of rows to retrieve
	 * @return iterator capable of iterating over {first, first+count} contacts
	 */
	@Override
	public Iterator<T> iterator(long first, long count) {
		List<T> list = getDataInternal();

		return list.subList((int) first, (int) first + (int) count).iterator();
	}

	/**
	 * Converts the object in the collection to its model representation. A good place to wrap the
	 * object in a detachable model.
	 * 
	 * @param object
	 *            The object that needs to be wrapped
	 * @return The model representation of the object
	 */
	@Override
	public IModel<T> model(T application) {
		return new WorklistDetachableModel<T>(application);
	}

	/**
	 * Gets total number of items in the collection.
	 * 
	 * @return total item count
	 */
	@Override
	public long size() {
		this.dataReady = false;
		return getDataInternal().size();
	}

}