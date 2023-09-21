package org.kataCalculator;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Примеры использования
        try {
            String inputExpression = "1 + 2";
            String result = calc(inputExpression);
            System.out.println("Output: " + result);
        } catch (Exception e) {
            System.out.println("Output: " + e.getMessage());
        }
    }

    public static String calc(String input) throws Exception {
        String[] tokens = input.split(" ");

        if (tokens.length != 3) {
            throw new Exception("throws Exception //т.к. формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
        }

        String operand1 = tokens[0];
        String operator = tokens[1];
        String operand2 = tokens[2];

        boolean isArabic = operand1.matches("[1-9]|10") && operand2.matches("[1-9]|10");
        boolean isRoman = operand1.matches("[IVXLCDM]+") && operand2.matches("[IVXLCDM]+");

        if (!(isArabic || isRoman)) {
            throw new Exception("throws Exception //т.к. используются одновременно разные системы счисления");
        }

        if (isArabic && isRoman) {
            throw new Exception("throws Exception //т.к. используются одновременно разные системы счисления");
        }

        int num1 = isArabic ? Integer.parseInt(operand1) : romanToArabic(operand1);
        int num2 = isArabic ? Integer.parseInt(operand2) : romanToArabic(operand2);

        int result;
        switch (operator) {
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                if (isRoman && result <= 0) {
                    throw new Exception("throws Exception //т.к. в римской системе нет отрицательных чисел");
                }
                break;
            case "*":
                result = num1 * num2;
                break;
            case "/":
                if (num2 == 0) {
                    throw new Exception("throws Exception //т.к. деление на ноль запрещено");
                }
                result = num1 / num2;
                break;
            default:
                throw new Exception("throws Exception //т.к. недопустимый оператор");
        }

        return isArabic ? Integer.toString(result) : arabicToRoman(result);
    }

    public static int romanToArabic(String roman) {
        // Реализация преобразования римских чисел в арабские
        int result = 0;
        int prevValue = 0;

        for (int i = roman.length() - 1; i >= 0; i--) {
            int currentValue = romanNumerals.get(String.valueOf(roman.charAt(i)));

            if (currentValue < prevValue) {
                result -= currentValue;
            } else {
                result += currentValue;
            }

            prevValue = currentValue;
        }

        return result;
    }

    private static final int[] arabicValues = {100, 90, 50, 40, 10, 9, 5, 4, 1};
    private static final Map<String, Integer> romanNumerals = new HashMap<>();

    public static String arabicToRoman(int number) {
        // Реализация преобразования арабских чисел в римские
        StringBuilder roman = new StringBuilder();

        for (int i = 0; i < arabicValues.length; i++) {
            while (number >= arabicValues[i]) {
                roman.append(romanNumerals.get(i));
                number -= arabicValues[i];
            }
        }

        return roman.toString();
    }


    static {
        romanNumerals.put("I", 1);
        romanNumerals.put("IV", 4);
        romanNumerals.put("V", 5);
        romanNumerals.put("IX", 9);
        romanNumerals.put("X", 10);
        romanNumerals.put("XL", 40);
        romanNumerals.put("L", 50);
        romanNumerals.put("XC", 90);
        romanNumerals.put("C", 100);
    }

    ;
}
