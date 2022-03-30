package com.mitya.calculator.helper;

import android.util.Log;

public class Expression {
    private double firstNumber;
    private char operator;
    private double secondNumber;

    public Expression(String str) {
        int count = 0;
        int multiplier = 1;
        if (str.charAt(0) == '—') {
            multiplier = -1;
            count++;
        }

        if (str.charAt(0) == '√') {
            operator = '√';
            firstNumber = Double.parseDouble(str.substring(1));
            return;
        }

        StringBuilder numberString = new StringBuilder();
        while (true) {
            if (isAnOperator(str.charAt(count))) {
                firstNumber = Double.parseDouble(numberString.toString());
                break;
            }
            numberString.append(str.charAt(count));
            count++;

        }

        firstNumber *= multiplier;

        operator = str.charAt(count);
        count++;

        numberString = new StringBuilder();
        while (true) {
            if (count == str.length()) {
                secondNumber = Double.parseDouble(numberString.toString());
                break;
            }
            numberString.append(str.charAt(count));
            count++;
        }
    }


    public static boolean isAlreadyAnExpression(String str) {
        for (char c : str.toCharArray()) {
            if (isAnOperator(c)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAnOperator(char c) {
        return c == '%' || c == '÷' || c == '√' || c == '×' || c == '-' || c == '+';
    }

    public double calculate() {
        if (operator == '√') {
            return Math.sqrt(firstNumber);
        } else {
            switch (operator) {
                case '%':
                    return firstNumber % secondNumber;
                case '÷':
                    return firstNumber / secondNumber;
                case '×':
                    return firstNumber * secondNumber;
                case '-':
                    return firstNumber - secondNumber;
                case '+':
                    return firstNumber + secondNumber;
                default:
                    Log.e("Calculate", "Error with the calculation. The operator is unknown");
                    return -1;
            }
        }
    }
}