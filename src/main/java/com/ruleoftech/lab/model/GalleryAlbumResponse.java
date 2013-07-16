package com.ruleoftech.lab.model;

public class GalleryAlbumResponse {

	private GalleryAlbum data;
	private Boolean success;
	private Integer status;

	public GalleryAlbum getData() {
		return data;
	}

	public void setData(GalleryAlbum data) {
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

	@Override
	public String toString() {
		return "GalleryAlbumResponse [data=" + data + ", success=" + success + ", status=" + status + "]";
	}

}
