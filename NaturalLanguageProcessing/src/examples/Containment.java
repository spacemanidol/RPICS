package examples;

import neural.FFNN;
import neural.NeuralUtils;

public class Containment {
	public static void main(String[] args) {
		FFNN n = new FFNN(new int[] { 64, 16, 1 });
		n.setGammaTruncation(0.1);
		n.setVerbosity(2);
		n.setDisplayVisually(true, 800, 500, 0);

		double[][] problems = new double[1000][];
		double[][] solutions = new double[problems.length][];
		for (int i = 0; i < problems.length; i++) {
			String string = NeuralUtils.randomString(8, 'a', 'z' + 1);
			problems[i] = NeuralUtils.toArray(string);
			if (string.contains("q")) {
				solutions[i] = new double[] { 1 };
			} else {
				solutions[i] = new double[] { 0 };
			}
		}
		n.trainTillPerfection(problems, solutions);
		// Scanner s = new Scanner(System.in);
		// while (s.hasNext()) {
		// String in = s.nextLine();
		// if (in.length() > 8) {
		// in = in.substring(0, 8);
		// }
		// while (in.length() < 8) {
		// in = in + " ";
		// }
		// n.calculateOutput(NeuralUtils.toArray(in));
		// System.out.println(n.getResults());
		// }
		// s.close();
	}
}
