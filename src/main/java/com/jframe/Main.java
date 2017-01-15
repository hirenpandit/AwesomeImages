package com.jframe;

import java.io.File;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jframe.util.JFilePicker;

public class Main {

	private static JFilePicker filePicker;
	private static JButton btnBing;
	private static JButton btnSpotlight;
	private static JPanel panel;

	public static void main(String args[]) {

		btnBing = createBingButton();
		btnSpotlight = createSpotlightButton();

		filePicker = new JFilePicker("Choose Path to save", "Browse");
		initializeFrame(filePicker, btnBing, btnSpotlight);

	}

	private static void initializeFrame(JFilePicker filePicker, JButton btnBing,
			JButton btnSpotlight) {

		panel = new JPanel();

		GroupLayout layout = new GroupLayout(panel);
		// panel.setLayout(layout);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		filePicker.setMode(JFilePicker.MODE_SAVE);
		filePicker.setMode(JFilePicker.MODE_SAVE);
		JFrame frame = new JFrame("Download Awesome Images!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// btnBing.setSize(50, 20);
		// btnSpotlight.setSize(50, 20);

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

		// panel.add(filePicker);
		// panel.add(btnBing);
		// panel.add(btnSpotlight);
		// panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		frame.setContentPane(panel);
		frame.setSize(600, 200);
		frame.setVisible(true);
	}

	private static JButton createSpotlightButton() {
		JButton btnSpotlight = new JButton("Download Spotlight Images");
		btnSpotlight.addActionListener(event -> {
			btnSpotlight.setEnabled(false);
			DownloadSpotlightImages images = new DownloadSpotlightImages();
			try {
				images.downloadImages();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			btnSpotlight.setEnabled(true);
		});
		return btnSpotlight;
	}

	private static JButton createBingButton() {
		JButton btnBing = new JButton("Download Bing Images");
		btnBing.addActionListener(event -> {
			btnBing.setEnabled(false);
			DownloadBingImages images = new DownloadBingImages();
			String savePath = filePicker.getSelectedFilePath();
			System.out.println("Selected Path::::" + savePath);
			images.downloadImages(savePath + File.separator);
			btnBing.setEnabled(true);
		});
		return btnBing;
	}

}
