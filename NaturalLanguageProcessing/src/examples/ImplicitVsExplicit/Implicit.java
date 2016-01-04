package examples.ImplicitVsExplicit;

import neural.*;

public class Implicit {
	public static void main(String[] args) {
		// String in = "name";
		// String out = "Timmy";
		// int inputBits = 8 * in.length();
		// int outputBits = 8 * out.length();
		// GeneralNN n = new GeneralNN(inputBits, outputBits, 10, 1);
		// n.setGammaTruncation(0.1);
		// n.setVerbosity(1);
		// n.setDisplayVisually(true, 800, 500, 0);
		//
		// double[][] problems = new double[1000][];
		// double[][] solutions = new double[problems.length][];
		// problems[0] = new double[inputBits];
		// solutions[0] = new double[outputBits];
		// NeuralUtils.feedString(in, problems[0]);
		// NeuralUtils.feedString(out, solutions[0]);
		// for (int i = 1; i < problems.length; i++) {
		// problems[i] = NeuralUtils.getRandomBinaryArray(inputBits);
		// solutions[i] = new double[outputBits];
		// }
		// n.trainTillPerfection(problems, solutions);
		String in = "name";
		String out = "Timmy";
		int inputBits = 8 * in.length();
		int outputBits = 8 * out.length();
		FFNN n = new FFNN(new int[] { inputBits, 10, 10, outputBits });
		n.setGammaTruncation(0.1);
		n.setVerbosity(2);
		n.setDisplayVisually(true, 800, 500, 0);

		double[][] problems = new double[1000][];
		double[][] solutions = new double[problems.length][];
		problems[0] = new double[inputBits];
		solutions[0] = new double[outputBits];
		NeuralUtils.feedString(in, problems[0]);
		NeuralUtils.feedString(out, solutions[0]);
		for (int i = 1; i < problems.length; i++) {
			problems[i] = NeuralUtils.getRandomBinaryArray(inputBits);
			solutions[i] = new double[outputBits];
		}
		n.trainTillPerfection(problems, solutions);
	}
}
