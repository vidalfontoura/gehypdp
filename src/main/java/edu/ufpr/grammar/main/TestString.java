/*
 * Copyright 2016, Charter Communications,  All rights reserved.
 */
package edu.ufpr.grammar.main;

import HyPDP.ExpressionExecutor;

/**
 *
 *
 * @author vfontoura
 */
public class TestString {
	public static void main(String[] args) {
		String input = "( ( 1.0 - ( ( ( 1.0 / 0.0 ) + 200.0 * 2.0 * 0.0 ) * 1.0 ) - 1.0 ) + 2.0 / (1.0 * 0.0) )";
		input = input.replace(" ", "");
		System.out.println(input);

		double calculate = ExpressionExecutor.calculate(input);
		System.out.println(calculate);
		while (input.contains("(") && input.contains(")")) {
			input = input.replace("/0.0", "/ 0.001");
			System.out.println(input);
			int indexLastOpeningParantesis = input.lastIndexOf("(");
			String substring = input.substring(indexLastOpeningParantesis);
			int index1 = substring.indexOf("(");
			int index2 = substring.indexOf(")");
			substring = substring.substring(index1, index2 + 1);

			System.out.println(substring);

			String partialResult = String.valueOf(ExpressionExecutor.calculate(substring.replace("/0.0", "/0.001")));

			input = input.replace(substring, partialResult);

			System.out.println(input);
		}

	}
}
