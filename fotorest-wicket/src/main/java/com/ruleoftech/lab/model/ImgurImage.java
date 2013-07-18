package com.ruleoftech.lab.model;

import java.io.Serializable;

/**
 * Imgur's Image Data model, see: https://api.imgur.com/models/image.
 */
public class ImgurImage implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String title;
	private String description;
	private Long datetime;
	private String type;
	private Boolean animated;
	private Integer width;
	private Integer height;
	private Integer size;
	private Integer views;
	private Integer bandwidth;
	private String deletehash;
	private String link;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getDatetime() {
		return datetime;
	}

	public void setDatetime(Long datetime) {
		this.datetime = datetime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getAnimated() {
		return animated;
	}

	public void setAnimated(Boolean animated) {
		this.animated = animated;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getViews() {
		return views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}

	public Integer getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(Integer bandwidth) {
		this.bandwidth = bandwidth;
	}

	public String getDeletehash() {
		return deletehash;
	}

	public void setDeletehash(String deletehash) {
		this.deletehash = deletehash;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public String toString() {
		return "Image [id=" + id + ", title=" + title + ", description=" + description + ", datetime=" + datetime
				+ ", type=" + type + ", animated=" + animated + ", width=" + width + ", height=" + height + ", size="
				+ size + ", views=" + views + ", bandwidth=" + bandwidth + ", deletehash=" + deletehash + ", link="
				+ link + "]";
	}
}
