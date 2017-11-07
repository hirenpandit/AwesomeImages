package com.jframe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.ws.http.HTTPException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.common.CommonConstants;

@Component
public class DownloadBingImages {

	private static final Logger logger = LoggerFactory.getLogger(DownloadBingImages.class);

	private byte[] fileBytes;

	public void downloadImages(String path) {
		String uri;
		int i = 0;
		ExecutorService executor = Executors.newFixedThreadPool(10);
		List<Future<String>> futures = new ArrayList<>();
		do {
			uri = CommonConstants.BING_IMAGE_BASE_URL.replace("idx=0", "idx=" + i);

			try {
				CloseableHttpClient httpClient = HttpClientBuilder.create().build();
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
				InputStreamReader inputStreamReader = new InputStreamReader((response.getEntity().getContent()), Charset.defaultCharset());
				BufferedReader br = new BufferedReader(inputStreamReader);

				String output;
				if ((output = br.readLine()) != null) {
					JSONObject jsonObject = new JSONObject(output);
					JSONObject imageObject = new JSONObject(jsonObject.getJSONArray("images").getString(0));

					String imageURL = imageObject.getString("url");
					String imageName = imageURL.replace("/az/hprichbg/rb/", "");

					File file = new File(path + imageName);
					if (!file.exists()) {
						String url = "http://www.bing.com" + imageURL;
						Callable<String> callable = new Callable<String>() {
							@Override
							public String call() {
								try {
									downloadImage(url, file);
									return "success";
								} catch (IOException e) {
									logger.error("ERROR while downloading",e);
									return "failed";
								}
							}
						};
						Future<String> submit = executor.submit(callable);
						futures.add(submit);
						logger.info("BingImage Saved: {}", file.getName());
					} else {
						logger.info("File {} already exist", file.getName());
					}

				}

				httpClient.close();

			} catch (Exception ex) {
				logger.info("---Error reading file from path: {}", ex);
				break;
			}

			i++;
		} while (StringUtils.isNotBlank(uri) && i < 20);
		logger.info("All tasks are completed... shutting down executor service");
		executor.shutdown();

	}

	private void downloadImage(String url, File file) throws IOException {
		logger.info("Downloading file: {}", file.getName());
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet getRequest = new HttpGet(url);
		getRequest.addHeader("accept", CommonConstants.ACCEPT_OCTET_STREAM);

		HttpResponse response = httpClient.execute(getRequest);

		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}
		try (FileOutputStream output = new FileOutputStream(file)) {
			byte[] bytes = IOUtils.toByteArray(response.getEntity().getContent());
			fileBytes = bytes;
			IOUtils.write(bytes, output);
			logger.info("File: {} downloaded", file.getName());
		} catch (Exception e) {
			logger.error("Error downloading image from Bing: {}", e);
		}

	}
	
	/*public void testMultipart() throws ClientProtocolException{
		
		logger.info("TESTING MULTIPART>>>>");
		
		HttpPost request = new HttpPost("http://localhost:8098/testMultipart");
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		HttpClient httpClient = HttpClientBuilder.create().build();
		
		File f = new File("/home/hiren/Pictures/AeoniumLeaf_EN-US7200082197_1920x1080.jpg");
		try(FileInputStream fis = new FileInputStream(f);){
			fileBytes = new byte[(int)f.length()];
			fis.read(fileBytes);
			fis.close();
	        builder.addBinaryBody("file", fileBytes);
	        
	        HttpEntity params= builder.build();

	        request.setEntity(params);
	        request.addHeader("content-type","multipart/mixed");

	        HttpResponse response = httpClient.execute(request);
	        logger.info("Response: {}", response);
		} catch(IOException e){
			logger.error("error getting file bytes: {}", e);
		}
	}*/
}
