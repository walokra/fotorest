package com.ruleoftech.lab.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Imgur Gallery Album Data model, see: https://api.imgur.com/models/gallery_album.
 */
public class GalleryAlbum implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String title;
	private String description;
	private Long datetime;
	private String cover;
	private String account_url;
	private String privacy;
	private String layout;
	private Integer views;
	private String link;
	private Integer ups;
	private Integer downs;
	private Integer score;
	private Boolean is_album;
	private String vote;
	private Integer images_count;

	private ImgurImage[] images;

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

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getAccount_url() {
		return account_url;
	}

	public void setAccount_url(String account_url) {
		this.account_url = account_url;
	}

	public String getPrivacy() {
		return privacy;
	}

	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public Integer getViews() {
		return views;
	}

	public void setViews(Integer views) {
		this.views = views;
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

	public Boolean getIs_album() {
		return is_album;
	}

	public void setIs_album(Boolean is_album) {
		this.is_album = is_album;
	}

	public String getVote() {
		return vote;
	}

	public void setVote(String vote) {
		this.vote = vote;
	}

	public Integer getImages_count() {
		return images_count;
	}

	public void setImages_count(Integer images_count) {
		this.images_count = images_count;
	}

	public ImgurImage[] getImages() {
		return images;
	}

	public void setImages(ImgurImage[] images) {
		this.images = images;
	}

	@Override
	public String toString() {
		return "GalleryAlbum [id=" + id + ", title=" + title + ", description=" + description + ", datetime="
				+ datetime + ", cover=" + cover + ", account_url=" + account_url + ", privacy=" + privacy + ", layout="
				+ layout + ", views=" + views + ", link=" + link + ", ups=" + ups + ", downs=" + downs + ", score="
				+ score + ", is_album=" + is_album + ", vote=" + vote + ", images_count=" + images_count + ", images="
				+ Arrays.toString(images) + "]";
	}
}
