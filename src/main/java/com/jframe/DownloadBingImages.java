package com.jframe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.ws.http.HTTPException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.common.CommonConstants;

@Component
public class DownloadBingImages {

	private static final Logger logger = LoggerFactory.getLogger(DownloadBingImages.class);

	@PostConstruct
	public void construct() {
		logger.info("~~~~~~~~~~~~~~~~~~~Bean {} is created~~~~~~~~~~~~~~~~~~", getClass().getName());
	}

	public void downloadImages(String path) {
		String uri;
		int i = 0;
		ExecutorService executor = Executors.newFixedThreadPool(10);
		do {
			uri = CommonConstants.BING_IMAGE_BASE_URL.replace("idx=0", "idx=" + i);

			try {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpGet getRequest = new HttpGet(uri);
				getRequest.addHeader("accept", CommonConstants.ACCEPT_JSON);

				HttpResponse response = httpClient.execute(getRequest);

				if (response.getStatusLine().getStatusCode() != 200) {
					throw new HTTPException(response.getStatusLine().getStatusCode());
					/*
					 * throw new RuntimeException( "Failed : HTTP error code : "
					 * + response.getStatusLine().getStatusCode());
					 */
				}

				BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

				String output;
				logger.debug("Output from Server:");
				if ((output = br.readLine()) != null) {
					JSONObject jsonObject = new JSONObject(output);
					JSONObject imageObject = new JSONObject(jsonObject.getJSONArray("images").getString(0));

					logger.info("JSONObject: {}", imageObject.getString("url"));

					String imageURL = imageObject.getString("url");
					String imageName = imageURL.replace("/az/hprichbg/rb/", "");

					logger.info("FILE PATH: {}", StringUtils.join(path, imageName));

					File file = new File(path + imageName);
					if (!file.exists()) {
						String url = "http://www.bing.com" + imageURL;
						executor.submit(new Runnable() {
							@Override
							public void run() {
								try {
									downloadImage(url, file);
								} catch (IOException e) {
									logger.error("ERROR while downloading",e);
								}
							}
						});
						logger.info("BingImage Saved: {}", file.getName());
					} else {
						logger.info("File {} already exist", file.getName());
					}

				}

				httpClient.getConnectionManager().shutdown();

			} catch (Exception ex) {
				logger.info("---Error reading file from path: {}", ex);
				break;
			}

			i++;
		} while (StringUtils.isNotBlank(uri) && i < 20);

	}

	private void downloadImage(String url, File file) throws IOException {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(url);
		getRequest.addHeader("accept", CommonConstants.ACCEPT_OCTET_STREAM);

		HttpResponse response = httpClient.execute(getRequest);

		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}
		try (FileOutputStream output = new FileOutputStream(file)) {
			byte[] bytes = IOUtils.toByteArray(response.getEntity().getContent());
			IOUtils.write(bytes, output);
		} catch (Exception e) {
			logger.error("Error downloading image from Bing: {}", e);
		}

	}

	@PreDestroy
	public void destroy() {
		logger.info("~~~~~~~~~~~~~~~~~~~Bean {} is being destroyed~~~~~~~~~~~~~~~~~~", getClass().getName());
	}
}
