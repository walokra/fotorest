package com.ruleoftech.lab.fotorest.model;

public class GalleryImageResponse {

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
