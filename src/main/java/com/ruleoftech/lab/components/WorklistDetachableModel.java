package com.ruleoftech.lab.components;

import java.io.Serializable;

import org.apache.wicket.model.LoadableDetachableModel;

public class WorklistDetachableModel<T extends Serializable> extends LoadableDetachableModel<T> {
	private static final long serialVersionUID = 3128342347114713480L;
	private final T model;

	public WorklistDetachableModel(T model) {
		this.model = model;
	}

	@Override
	protected T load() {
		return model;
	}
}
