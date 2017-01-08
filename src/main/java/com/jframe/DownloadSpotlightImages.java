package com.jframe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DownloadSpotlightImages {

	public void downloadImages() throws IOException {
		File source = new File(
				"C:\\Users\\user\\AppData\\Local\\Packages\\Microsoft.Windows.ContentDeliveryManager_cw5n1h2txyewy\\LocalState\\Assets");
		File dest = new File("C:\\Users\\user\\Pictures\\AwesomeImages\\");
		File[] files = source.listFiles();

		for (File file : files) {
			FileInputStream input = new FileInputStream(file);
			File f = new File(dest.getAbsolutePath() + "\\" + file.getName() + ".jpg");
			FileOutputStream output = new FileOutputStream(f);
			if (file.length() / 1024 > 100) {
				byte[] bFile = new byte[(int) file.length()];
				input.read(bFile);
				input.close();
				output.write(bFile);
			}
		}
	}

}
