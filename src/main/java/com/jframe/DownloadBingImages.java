package com.jframe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class DownloadBingImages {
	private String path;

	public void downloadImages(String savePath) {

		this.path = savePath;
		String uri = "";
		int i = 0;
		do {
			uri = "http://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=en-US"
					.replace("idx=0", "idx=" + i);

			try {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpGet getRequest = new HttpGet(uri);
				getRequest.addHeader("accept", "application/json");

				HttpResponse response = httpClient.execute(getRequest);

				if (response.getStatusLine().getStatusCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
							+ response.getStatusLine().getStatusCode());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(
						(response.getEntity().getContent())));

				String output;
				System.out.println("Output from Server .... \n");
				if ((output = br.readLine()) != null) {
					JSONObject jsonObject = new JSONObject(output);
					JSONObject imageObject = new JSONObject(
							jsonObject.getJSONArray("images").getString(0));
					System.out.println(
							"JSONObject: " + imageObject.getString("url"));

					String imageURL = imageObject.getString("url");
					String imageName = imageURL.replace("/az/hprichbg/rb/", "");
					System.out.println("FILE PATH: " + (path + imageName));
					File file = new File(path + imageName);
					if (!file.exists()) {
						String url = "http://www.bing.com" + imageURL;
						downloadImage(url, file);
						System.out
								.println("BingImage Saved: " + file.getName());
					} else {
						System.out.println(
								"File " + file.getName() + " already exist");
					}

				}

				httpClient.getConnectionManager().shutdown();

			} catch (Exception ex) {
				ex.printStackTrace();
			}

			i++;
		} while (!uri.equals("") && i < 20);

	}

	private void downloadImage(String url, File file)
			throws ClientProtocolException, IOException {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(url);
		getRequest.addHeader("accept", "application/octet-stream");

		HttpResponse response = httpClient.execute(getRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatusLine().getStatusCode());
		}
		try (FileOutputStream output = new FileOutputStream(file)) {
			byte[] bytes = IOUtils
					.toByteArray(response.getEntity().getContent());
			IOUtils.write(bytes, output);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
