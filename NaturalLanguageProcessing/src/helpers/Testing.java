package helpers;

import neural.NeuralUtils;

public class Testing {

	public static void main(String[] args) {
		double[][] binaryCombos = NeuralUtils.getBinaryCombos(10);
		int specialGroups = 0;
		for (double[] d : binaryCombos) {
			int num1Groups = 0;
			boolean inOne = false;
			for (int i = 0; i < d.length; i++) {
				if (inOne && d[i] != 1) {
					num1Groups++;
				}
				inOne = d[i] == 1;
			}
			if (inOne) {
				num1Groups++;
			}
			if (num1Groups < 3) {
				specialGroups++;
			}
			for (double e : d) {
				System.out.print((int) e);
			}
			System.out.println(":::" + num1Groups);
		}
		System.out.println(specialGroups);
	}

}
