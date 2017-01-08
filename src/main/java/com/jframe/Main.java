package com.jframe;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {

	public static void main(String args[]) {

		JButton btnBing = createBingButton();
		JButton btnSpotlight = createSpotlightButton();
		initializeFrame(btnBing, btnSpotlight);

	}

	private static void initializeFrame(JButton btnBing, JButton btnSpotlight) {
		JFrame frame = new JFrame("Download Awesome Images!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel(new GridLayout(2, 2));

		panel.add(btnBing);
		panel.add(btnSpotlight);
		panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		frame.setContentPane(panel);
		frame.setSize(200, 200);
		frame.setVisible(true);
	}

	private static JButton createSpotlightButton() {
		JButton btnSpotlight = new JButton("Download Spotlight Images");
		btnSpotlight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				btnSpotlight.setEnabled(false);
				DownloadSpotlightImages images = new DownloadSpotlightImages();
				try {
					images.downloadImages();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				btnSpotlight.setEnabled(true);
			}
		});
		return btnSpotlight;
	}

	private static JButton createBingButton() {
		JButton btnBing = new JButton("Download Bing Images");

		btnBing.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				btnBing.setEnabled(false);
				DownloadBingImages images = new DownloadBingImages();
				images.downloadImages();
				btnBing.setEnabled(true);
			}
		});
		return btnBing;
	}

}
