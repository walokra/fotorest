package com.ruleoftech.lab.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.ruleoftech.lab.exception.BusinessException;
import com.ruleoftech.lab.model.CreditsResponse;
import com.ruleoftech.lab.model.GalleryAlbum;
import com.ruleoftech.lab.model.GalleryAlbumResponse;
import com.ruleoftech.lab.model.GalleryImage;
import com.ruleoftech.lab.model.GalleryImageResponse;

@Service
public class RestService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RestService.class);

	public List<GalleryImage> searchImages(String query) {
		LOGGER.trace("{'method':'searchImages', 'params':{'query':'{}'}}", query);

		return listImages(query, false);
	}

	public List<GalleryImage> hotImages() {
		LOGGER.trace("{'method':'hotImages'}");

		return listImages(null, false);
	}

	public List<GalleryImage> randomImages() {
		LOGGER.trace("{'method':'randomImages'}");

		return listImages(null, true);
	}

	private List<GalleryImage> listImages(String query, boolean random) {
		LOGGER.trace("{'method':'listImages', 'params':{'search':'{}', 'random':{}}}", query, random);

		Properties p = readProps();

		List<GalleryImage> result = new ArrayList<GalleryImage>();

		try {
			// Apache CXF
			// http://cxf.apache.org/docs/jax-rs-client-api.html
			WebClient client = WebClient.create(p.get("baseurl").toString());
			if (random) {
				client.path(p.get("gallery.random").toString());
			} else if (query != null && !query.isEmpty()) {
				client.path(p.get("gallery.search").toString());
				client.query("q", query);
			} else {
				client.path(p.get("gallery.hot").toString());
			}
			client.header("Authorization", "Client-ID " + p.get("client.id").toString());
			client.accept(MediaType.APPLICATION_JSON);
			String json = client.get(String.class);
			LOGGER.trace("{'method':'listImages', 'Uri':'{}'", client.getCurrentURI() + "}");

			// Parse JSON to Java objects
			Gson gson = new Gson();
			GalleryImageResponse giResponse = gson.fromJson(json, GalleryImageResponse.class);
			result = Arrays.asList(giResponse.getData());
			LOGGER.trace("{'method':'listImages', 'result':{'success':" + giResponse.getSuccess() + ", 'status':"
					+ giResponse.getStatus() + ", 'items':" + result.size() + "}}");
			return result;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage(), e);
		}
	}

	public GalleryAlbum getGalleryAlbum(String galleryId) {
		LOGGER.trace("{'method':'getGalleryAlbum'}");

		Properties p = readProps();
		GalleryAlbum result = new GalleryAlbum();

		try {
			// Apache CXF
			WebClient client = WebClient.create(p.get("baseurl").toString());
			client.path(p.get("gallery.album").toString() + "/" + galleryId);
			client.header("Authorization", "Client-ID " + p.get("client.id").toString());
			client.accept(MediaType.APPLICATION_JSON);
			String json = client.get(String.class);
			LOGGER.trace("{'method':'getGalleryAlbum', 'Uri':'{}'", client.getCurrentURI() + "}");

			// Parse JSON to Java objects
			Gson gson = new Gson();
			GalleryAlbumResponse gaResponse = gson.fromJson(json, GalleryAlbumResponse.class);
			result = gaResponse.getData();
			LOGGER.trace("{'method':'getGalleryAlbum', 'result':{'success':" + gaResponse.getSuccess() + ", 'status':"
					+ gaResponse.getStatus() + ", 'items':" + gaResponse.getData().getImages_count() + "}}");

			return result;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage(), e);
		}
	}

	public String getCredits() {
		Properties p = readProps();

		try {
			// Apache CXF
			WebClient client = WebClient.create("https://api.imgur.com/3/credits");
			// client.path("bookstore/books");
			client.header("Authorization", "Client-ID " + p.get("client.id").toString());
			client.accept(MediaType.APPLICATION_JSON);
			String json = client.get(String.class);
			LOGGER.trace("{'method':'getCredits', 'response':{}}", json);

			Gson gson = new Gson();
			CreditsResponse response = gson.fromJson(json, CreditsResponse.class);
			// LOGGER.trace("{'method':'getCredits', 'result':{'success':{}, 'status':{}}}",
			// response.getSuccess(), response.getStatus());
			StringBuilder sb = new StringBuilder();
			sb.append("client=");
			sb.append(response.getData().getClientRemaining());
			sb.append(", user=");
			sb.append(response.getData().getUserRemaining());
			return sb.toString();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage(), e);
		}
	}

	private Properties readProps() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream stream = classLoader.getResourceAsStream("/runtime.properties");

		Properties p = new Properties();
		if (stream == null) {
			// File not nound
			LOGGER.trace("{'method':'readProps', 'debug':'File not nound'}");
		} else {
			try {
				p.load(stream);
				// for (Entry<Object, Object> e : p.entrySet()) {
				// LOGGER.trace("props: " + e.getKey() + "=" + e.getValue());
				// }
				if (p.get("client.id") == null || ((String) p.get("client.id")).length() == 0) {
					throw new BusinessException("Missing client.id value in runtime.properties.");
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
				throw new BusinessException(e.getMessage(), e);
			}
		}
		return p;
	}

}
