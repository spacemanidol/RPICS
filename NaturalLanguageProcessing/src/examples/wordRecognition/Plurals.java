package examples.wordRecognition;

import java.util.ArrayList;

import neural.FFNN;
import neural.NeuralUtils;

public class Plurals {

	public static void main(String[] args) {
		FFNN n = new FFNN(new int[] { getRecognitionData("").length, 10, 1 });
		n.setGammaTruncation(0.1);
		n.setVerbosity(2);
		n.setDisplayVisually(true, 500, 500, 0);
		String[] problemStrings = {
				"silk",
				"cow",
				"cat",
				"potato",

				"silks",
				"cows",
				"cats",
				"potatoes"
		};
		double[][] solutions = new double[problemStrings.length][];
		for (int i = 0; i < problemStrings.length; i++) {
			solutions[i] = new double[] { (int) (i / (problemStrings.length / 2)) };
		}

		double[][] problems = new double[problemStrings.length][];
		for (int i = 0; i < problemStrings.length; i++) {
			problems[i] = getRecognitionData(problemStrings[i]);
		}
		n.trainTillPerfection(problems, solutions);
	}

	private static double[] getRecognitionData(String s) {
		String lower = s.toLowerCase();
		ArrayList<Double> ret = new ArrayList<Double>();

		for (int i = 0; i < 26; i++) {
			ret.add(NeuralUtils.toDouble(lower.contains("" + (char) (i + 97))));
		}
		ret.add(NeuralUtils.toDouble(lower.endsWith("s")));

		double[] retArr = new double[ret.size()];
		for (int i = 0; i < ret.size(); i++) {
			retArr[i] = ret.get(i);
		}
		return retArr;
	}
}
