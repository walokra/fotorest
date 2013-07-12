package com.ruleoftech.lab.fotorest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.ejb.Stateless;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.ruleoftech.lab.fotorest.model.CreditsResponse;
import com.ruleoftech.lab.fotorest.model.GalleryImage;
import com.ruleoftech.lab.fotorest.model.GalleryImageResponse;

@Stateless
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
			// RESTeasy
			ClientRequest req = new ClientRequest(p.get("baseurl").toString() + "/{gallery}");
			if (random) {
				req.pathParameter("gallery", p.get("gallery.random").toString());
			} else if (query != null && !query.isEmpty()) {
				req.pathParameter("gallery", p.get("gallery.search").toString());
				req.queryParameter("q", query);
			} else {
				req.pathParameter("gallery", p.get("gallery.hot").toString());
			}
			req.header("Authorization", "Client-ID " + p.get("client.id").toString());
			req.accept("application/json");
			LOGGER.trace("{'method':'listImages', 'Uri':'{}'", req.getUri() + "}");

			ClientResponse<String> res = req.get(String.class);
			String json = res.getEntity();

			// Parse JSON to Java objects
			Gson gson = new Gson();
			GalleryImageResponse giResponse = gson.fromJson(json, GalleryImageResponse.class);
			result = Arrays.asList(giResponse.getData());
			LOGGER.trace("{'method':'listImages', 'result':{'success':" + giResponse.getSuccess() + ", 'status':"
					+ giResponse.getStatus() + ", 'items':" + result.size() + "}}");

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		return result;
	}

	public String getCredits() {
		Properties p = readProps();

		try {
			// RESTEasy
			ClientRequest req = new ClientRequest("https://api.imgur.com/3/credits");
			req.header("Authorization", "Client-ID " + p.get("client.id").toString());
			req.accept("application/json");

			ClientResponse<String> res = req.get(String.class);
			String json = res.getEntity();

			LOGGER.trace("{'method':'getCredits', 'response':{}}", json);

			Gson gson = new Gson();
			CreditsResponse response = gson.fromJson(json, CreditsResponse.class);
			// LOGGER.trace("{'method':'getCredits', 'result':{'success':{}, 'status':{}}}", response.getSuccess(),
			// response.getStatus());
			StringBuilder sb = new StringBuilder();
			sb.append("client=");
			sb.append(response.getData().getClientRemaining());
			sb.append(", user=");
			sb.append(response.getData().getUserRemaining());
			return sb.toString();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return "";
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
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		return p;
	}

}
