package examples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import neural.FFNN;

public class DigitLearner2 {

	private static int step = 4;

	public static double[][][] parseProblems(String file) {
		ArrayList<Integer> solutionList = new ArrayList<Integer>();
		ArrayList<double[]> problemList = new ArrayList<double[]>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(file)));
			String line;
			while ((line = br.readLine()) != null) {
				String[] numbers = line.split(" ");
				solutionList.add((int) Double.parseDouble(numbers[0]));
				problemList.add(new double[(int) ((numbers.length - 1) / Math.pow(step, 2))]);
				for (int i = 1; i < numbers.length; i += step) {
					if ((i / step) % step == 0) {
						problemList.get(problemList.size() - 1)[(i - 1) / step] = (Double.parseDouble(numbers[i]) + 1) / 2;
					}
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double[][] problems = new double[problemList.size()][];
		problemList.toArray(problems);
		double[][] solutions = new double[solutionList.size()][10];
		for (int i = 0; i < solutionList.size(); i++) {
			solutions[i][solutionList.get(i)] = 1;
		}
		return new double[][][] { problems, solutions };
	}

	public static void main(String[] args) {
		FFNN n = new FFNN(new int[] { 256 / step, 10 });
		n.setGammaTruncation(0.5);
		n.setVerbosity(2);
		n.setDisplayVisually(true, 500, 500, 0);
		double[][][] parse = parseProblems("digits1.txt");
		double[][] problems = parse[0];
		double[][] solutions = parse[1];
		n.trainTillPerfection(problems, solutions);
	}
}
