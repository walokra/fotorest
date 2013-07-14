package com.ruleoftech.lab.fotorest.model;


public class GalleryImage {
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
	private String link;
	private Integer ups;
	private Integer downs;
	private Integer score;
	private boolean is_album;

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

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Integer getUps() {
		return ups;
	}

	public void setUps(Integer ups) {
		this.ups = ups;
	}

	public Integer getDowns() {
		return downs;
	}

	public void setDowns(Integer downs) {
		this.downs = downs;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public boolean isIs_album() {
		return is_album;
	}

	public void setIs_album(boolean is_album) {
		this.is_album = is_album;
	}

	@Override
	public String toString() {
		return "GalleryImage [id=" + id + ", title=" + title + ", description=" + description + ", datetime="
				+ datetime + ", type=" + type + ", animated=" + animated + ", width=" + width + ", height=" + height
				+ ", size=" + size + ", views=" + views + ", bandwidth=" + bandwidth + ", link=" + link + ", ups="
				+ ups + ", downs=" + downs + ", score=" + score + ", is_album=" + is_album + "]";
	}

}
