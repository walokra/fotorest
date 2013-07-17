package com.ruleoftech.lab.components;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.model.Model;

/**
 * External image component.
 * see: https://cwiki.apache.org/confluence/display/WICKET/How+to+load+an+external+image
 * 
 */
public class ExternalImage extends WebComponent {
	private static final long serialVersionUID = 1L;

	public ExternalImage(String id, String imageUrl) {
		super(id);
		add(new AttributeModifier("src", new Model<String>(imageUrl)));
		setVisible(!(imageUrl == null || imageUrl.equals("")));
	}

	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		checkComponentTag(tag, "img");
	}

}
