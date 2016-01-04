package examples;

import neural.FFNN;
import neural.NeuralUtils;

public class OneHot {

	public static void main(String[] args) {
		// This number can't go very high before things start to take forever,
		// remember that it's an exponential.
		int maxBits = 8;
		FFNN n = new FFNN(new int[] { maxBits, maxBits, maxBits, 1 });
		n.setGammaTruncation(0.5);
		n.setVerbosity(2);
		n.setDisplayVisually(true, 500, 500, 0);
		double[][] problems = NeuralUtils.getBinaryCombos(maxBits);
		double[][] solutions = new double[problems.length][1];
		for (int i = 0; i < problems.length; i++) {
			int num1s = 0;
			for (int j = 0; j < problems[i].length; j++) {
				if (problems[i][j] == 1) {
					num1s++;
				}
			}
			if (num1s == 1) {
				solutions[i][0] = 1;
			}
		}
		n.trainTillPerfection(problems, solutions);
	}
}
