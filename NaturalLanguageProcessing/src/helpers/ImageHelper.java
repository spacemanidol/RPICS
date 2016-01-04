package helpers;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class ImageHelper {

	public static BufferedImage loadImage(String file) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(file));
		} catch (IOException e) {
		}
		return img;
	}

	@SuppressWarnings("serial")
	public static void showImage(final BufferedImage image) {
		JFrame frame = new JFrame();
		frame.setContentPane(new Container() {
			public void paint(Graphics g) {
				g.drawImage(image, 0, 0, null);
			};
		});
		frame.getContentPane().setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		frame.pack();
		frame.setVisible(true);
	}
}
