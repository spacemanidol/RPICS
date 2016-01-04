package examples;

import helpers.ImageHelper;

import java.awt.image.BufferedImage;

import neural.FFNN;

public class LineRecognizer {

	public static double[][][] parse(BufferedImage image) {
		double[][] problems = new double[image.getHeight()][image.getWidth()];
		double[][] solutions = new double[image.getHeight()][1];
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				problems[i][j] = image.getRGB(j, i) == -16777216 ? 1 : 0;
			}
			solutions[i] = new double[] { i / (image.getHeight() / 2) };
		}
		return new double[][][] { problems, solutions };
	}

	public static void main(String[] args) {
		String imageFile = "line.bmp";
		BufferedImage image = ImageHelper.loadImage(imageFile);
		double[][][] parse = parse(image);
		FFNN n = new FFNN(new int[] { image.getWidth(), 1 });
		n.setGammaTruncation(0.01);
		n.setVerbosity(2);
		n.setDisplayVisually(true, 500, 500, 0);
		n.trainTillPerfection(parse[0], parse[1]);
	}
}
