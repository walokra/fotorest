package com.ruleoftech.lab.components;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilteredPropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilter;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

@SuppressWarnings("serial")
public abstract class LinkPropertyColumn<T> extends FilteredPropertyColumn<T, String> {

	public LinkPropertyColumn(IModel<String> displayModel, IModel<T> labelModel) {
		super(displayModel, null);
	}

	public LinkPropertyColumn(final IModel<String> displayModel, final String sortProperty,
			final String propertyExpression) {
		super(displayModel, propertyExpression, sortProperty);
	}

	public LinkPropertyColumn(IModel<String> displayModel, String propertyExpressions) {
		super(displayModel, propertyExpressions);
	}

	@Override
	public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
		cellItem.add(new LinkPanel(cellItem, componentId, rowModel));
	}

	/**
	 * Override this method to react to link clicks. Your own/internal row id
	 * will most likely be inside the model.
	 */
	public abstract void onClick(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> model,
			AjaxRequestTarget target);

	public class LinkPanel extends Panel {

		public LinkPanel(final Item<ICellPopulator<T>> cellItem, final String componentId, final IModel<T> model) {
			super(componentId);

			AjaxLink<T> link = new AjaxLink<T>("link") {

				@Override
				public void onClick(AjaxRequestTarget target) {
					LinkPropertyColumn.this.onClick(cellItem, componentId, model, target);
				}
			};
			link.add(new Label("label", getDataModel(model)));
			add(link);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.wicket.extensions.markup.html.repeater.data.table.filter.
	 * IFilteredColumn#getFilter(java.lang.String,
	 * org.apache.wicket.extensions.markup
	 * .html.repeater.data.table.filter.FilterForm)
	 */
	@Override
	public Component getFilter(String componentId, FilterForm<?> form) {
		return new TextFilter<T>(componentId, getFilterModel(form), form);
	}

	protected IModel<T> getFilterModel(final FilterForm<?> form) {
		return new PropertyModel<T>(form.getDefaultModel(), getPropertyExpression());
	}
}
