package com.ruleoftech.lab.model;

import java.io.Serializable;

public class GalleryImageResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private GalleryImage[] data;
	private Boolean success;
	private Integer status;

	public GalleryImage[] getData() {
		return data;
	}

	public void setData(GalleryImage[] data) {
		this.data = data;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
