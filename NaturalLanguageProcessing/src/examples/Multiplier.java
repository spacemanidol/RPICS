package examples;

import neural.FFNN;
import neural.NeuralUtils;

public class Multiplier {
	public static void main(String[] args) {
		int bits = 3;
		FFNN n = new FFNN(new int[] { bits * 2, bits * bits, bits * bits,
				bits * 2 });
		n.setGammaTruncation(0.5);
		n.setVerbosity(2);
		n.setDisplayVisually(true, 500, 500, 2);
		double[][] problems = NeuralUtils.getBinaryCombos(bits * 2);
		double[][] solutions = new double[problems.length][];
		for (int i = 0; i < problems.length; i++) {
			solutions[i] = new double[n.getNumOutputs()];
			NeuralUtils.feedInt(NeuralUtils.toInt(problems[i], 0, bits)
					* NeuralUtils.toInt(problems[i], bits, bits * 2),
					solutions[i]);
		}
		n.trainTillPerfection(problems, solutions);
	}
}
