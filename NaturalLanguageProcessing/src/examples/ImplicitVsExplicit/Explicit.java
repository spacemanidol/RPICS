package examples.ImplicitVsExplicit;

import neural.FFNN;
import neural.NeuralUtils;

public class Explicit {
	public static void main(String[] args) {
		for (int stage = 0; stage < 2; stage++) {
			FFNN n = null;
			String string = null;
			double[][] problems = null;
			double[][] solutions = null;
			if (stage == 0) {
				string = "name";
				n = new FFNN(new int[] { 8 * string.length(), 10, 1 });
				n.setGammaTruncation(0.1);
				n.setVerbosity(2);
				n.setDisplayVisually(true, 800, 500, 0);

				problems = new double[1000][];
				solutions = new double[problems.length][];
				problems[0] = new double[8 * string.length()];
				solutions[0] = new double[] { 1 };
				NeuralUtils.feedString(string, problems[0]);
				for (int i = 1; i < problems.length; i++) {
					problems[i] = NeuralUtils.getRandomBinaryArray(8 * string
							.length());
					solutions[i] = new double[] { 0 };
				}
			} else if (stage == 1) {
				string = "Timmy";
				n = new FFNN(new int[] { 1, 8 * string.length() });
				n.setGammaTruncation(0.1);
				n.setVerbosity(2);
				n.setDisplayVisually(true, 800, 500, 0);

				problems = new double[2][];
				solutions = new double[problems.length][];
				problems[0] = new double[] { 1 };
				solutions[0] = new double[8 * string.length()];
				NeuralUtils.feedString(string, solutions[0]);
				problems[1] = new double[] { 0 };
				solutions[1] = new double[8 * string.length()];
			}
			n.trainTillPerfection(problems, solutions);
		}
	}
}
