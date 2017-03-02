package com.jframe;

import java.io.File;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jframe.util.JFilePicker;

@Component
public class MyApplication {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MyApplication.class);

	@Autowired
	private DownloadBingImages bingImages;

	private JFilePicker filePicker;
	private JButton btnBing;
	private JButton btnSpotlight;
	private JPanel panel;

	public void initializeView() {
		btnBing = createBingButton();
		btnSpotlight = createSpotlightButton();

		filePicker = new JFilePicker("Choose Path to save", "Browse");
		initializeFrame(filePicker, btnBing, btnSpotlight);
	}

	private void initializeFrame(JFilePicker filePicker, JButton btnBing,
			JButton btnSpotlight) {

		panel = new JPanel();

		GroupLayout layout = new GroupLayout(panel);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		filePicker.setMode(JFilePicker.MODE_SAVE);
		filePicker.setMode(JFilePicker.MODE_SAVE);
		JFrame frame = new JFrame("Download Awesome Images!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addComponent(filePicker)
				.addGroup(layout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(btnBing).addComponent(btnSpotlight)));

		layout.setVerticalGroup(
				layout.createSequentialGroup()
						.addGroup(layout
								.createParallelGroup(
										GroupLayout.Alignment.BASELINE)
								.addComponent(filePicker).addComponent(btnBing)
								.addComponent(btnSpotlight)));

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
				LOGGER.error("Error downloading image: {}", e1.getMessage());
			}
			btnSpotlight.setEnabled(true);
		});
		return btnSpotlight;
	}

	private JButton createBingButton() {
		JButton btnBing = new JButton("Download Bing Images");
		btnBing.addActionListener(event -> {
			btnBing.setEnabled(false);
			String savePath = filePicker.getSelectedFilePath();
			LOGGER.info("saving to path: {}", savePath);
			bingImages.downloadImages(savePath + File.separator);
			btnBing.setEnabled(true);
		});
		return btnBing;
	}

}
