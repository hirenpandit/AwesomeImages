package com.jframe.util;

import java.io.File;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jframe.DownloadBingImages;
import com.jframe.DownloadSpotlightImages;

@Component
public class DownloadImages {

	private static final Logger logger = LoggerFactory.getLogger(DownloadImages.class);

	@Autowired
	private DownloadBingImages bingImages;

	private JFilePicker filePicker;

	public void initView() {
		/*try {
			bingImages.testMultipart();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		JButton btnBing = createBingButton();
		JButton btnSpotlight = createSpotlightButton();

		filePicker = new JFilePicker("Choose Path to save", "Browse");
		initializeFrame(filePicker, btnBing, btnSpotlight);
	}

	private void initializeFrame(JFilePicker filePicker, JButton btnBing, JButton btnSpotlight) {
		JPanel panel = new JPanel();
		GroupLayout layout = new GroupLayout(panel);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		filePicker.setMode(JFilePicker.MODE_SAVE);
		filePicker.setMode(JFilePicker.MODE_SAVE);
		JFrame frame = new JFrame("Download Awesome Images!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(filePicker).addGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(btnBing).addComponent(btnSpotlight)));

		layout.setVerticalGroup(
				layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(filePicker).addComponent(btnBing).addComponent(btnSpotlight)));

		frame.setContentPane(panel);
		frame.setSize(600, 200);
		frame.setVisible(true);
	}

	private JButton createSpotlightButton() {
		JButton btnSpotlight = new JButton("Download Spotlight Images");
		btnSpotlight.addActionListener(event -> {
			btnSpotlight.setEnabled(false);
			DownloadSpotlightImages images = new DownloadSpotlightImages();
			try {
				images.downloadImages();
			} catch (IOException e1) {
				logger.error("Error downloading spotlight images: {}", e1);
			}
			btnSpotlight.setEnabled(true);
		});
		return btnSpotlight;
	}

	private JButton createBingButton() {
		logger.info("Downloading Bing images!");
		JButton btnBing = new JButton("Download Bing Images");
		btnBing.addActionListener(event -> {
			btnBing.setEnabled(false);
			String savePath = filePicker.getSelectedFilePath();
			logger.info("Selected Path: {}", savePath);
			long start = System.currentTimeMillis()/1000;
			bingImages.downloadImages(StringUtils.join(savePath, File.separator));
			logger.info("Time Taken: {} seconds", ((System.currentTimeMillis()/1000) - start));
			btnBing.setEnabled(true);
		});
		return btnBing;
	}

}
