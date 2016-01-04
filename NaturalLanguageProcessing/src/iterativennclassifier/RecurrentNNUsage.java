package nlp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Scanner;

import neural.RecurringNN;

public class RecurrentNNUsage {
	public static HashSet<String> getTokens(String[][] inputs) {
		HashSet<String> ret = new HashSet<String>();
		for (String[] input : inputs) {
			for (String token : input) {
				ret.add(token);
			}
		}
		return ret;
	}

	public static Hashtable<String, Integer> getIndices(HashSet<String> tokens) {
		Hashtable<String, Integer> ret = new Hashtable<String, Integer>();
		int index = 0;
		for (String token : tokens) {
			ret.put(token, index);
			index++;
		}
		return ret;
	}

	public static double[][] convertToProblem(String[] input, Hashtable<String, Integer> indices) {
		double[][] problem = new double[input.length][indices.size()];
		for (int j = 0; j < input.length; j++) {
			problem[j][indices.get(input[j])] = 1;
		}
		return problem;
	}

	public static double[][][] getProblems(String[][] inputs, Hashtable<String, Integer> indices) {
		double[][][] problems = new double[inputs.length][][];
		for (int i = 0; i < inputs.length; i++) {
			problems[i] = convertToProblem(inputs[i], indices);
		}
		return problems;
	}

	public static double[][] getSolutions(int numOutputs, int[] answers) {
		double[][] solutions = new double[answers.length][numOutputs];
		for (int i = 0; i < answers.length; i++) {
			solutions[i][answers[i]] = 1;
		}
		return solutions;
	}

	private static String clean(String input) {
		input = input.toLowerCase();
		String[] rep = { ".", "?" };
		for (int i = 0; i < rep.length; i++) {
			input = input.replace(rep[i], "");
		}
		return input;
	}

	@SuppressWarnings("unchecked")
	private static ArrayList<Object>[] parseProblems(String file) {
		ArrayList<Integer> answers = new ArrayList<Integer>();
		ArrayList<String[]> problems = new ArrayList<String[]>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(file)));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.length() > 0) {
					answers.add(Integer.parseInt(line.substring(1, line.indexOf(":"))));
					problems.add(clean(line.substring(line.indexOf(':') + 1, line.length())).split(" "));
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList[] { problems, answers };
	}

	public static void main(String[] args) {
		ArrayList<Object>[] parse = parseProblems("traindata.txt");

		String[][] inputs = new String[parse[0].size()][];
		parse[0].toArray(inputs);
		int[] answers = new int[parse[1].size()];
		int numOutputs = -1;
		for (int i = 0; i < parse[1].size(); i++) {
			answers[i] = (Integer) parse[1].get(i);
			numOutputs = Math.max(numOutputs, answers[i] + 1);
		}
		Hashtable<String, Integer> indices = getIndices(getTokens(inputs));

		int numInputs = getTokens(inputs).size();

		double[][][] problems = getProblems(inputs, indices);
		double[][] solutions = getSolutions(numOutputs, answers);

		RecurringNN n = new RecurringNN(numInputs, numOutputs, 10);
		n.setGammaTruncation(0.1);
		n.setVerbosity(2);

		n.trainTillPerfection(problems, solutions);

		Scanner s = new Scanner(System.in);
		while (s.hasNext()) {
			String input = s.nextLine();
			double[][] test = convertToProblem(input.split(" "), indices);
			n.calculateOutput(test);
			for (int i = 0; i < n.getNumOutputs(); i++) {
				System.out.println(n.outputNeurons[i].getAdjustedGamma(0));
			}
		}
		s.close();
	}
}
