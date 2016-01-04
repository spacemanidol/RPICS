package examples;

import neural.FFNN;
import neural.NeuralUtils;

public class NHot {

	public static void main(String[] args) {
		// This number can't go very high before things start to take forever,
		// remember that it's an exponential.
		int maxBits = 8;
		FFNN n = new FFNN(new int[] { maxBits, maxBits, maxBits });
		n.setGammaTruncation(0.5);
		n.setVerbosity(2);
		n.setDisplayVisually(true, 500, 500, 0);
		double[][] problems = NeuralUtils.getBinaryCombos(maxBits);
		double[][] solutions = new double[problems.length][n.getNumOutputs()];
		for (int i = 0; i < problems.length; i++) {
			for (int j = 0; j < n.getNumOutputs(); j++) {
				int num = 0;
				for (int k = 0; k < problems[i].length; k++) {
					if (problems[i][k] == 1) {
						num++;
					}
				}
				if (num == 1) {
					solutions[i][j] = 1;
				}
			}
		}
		n.trainTillPerfection(problems, solutions);
	}
}
