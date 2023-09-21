package org.kataCalculator;
import java.util.Scanner;
public class Main {
    private static final String ARAB_REGEX = "[1-9]|10";
    private static final String ROMAN_REGEX = "[IVXLCDM]+";

    public static void main(String[] args) throws Exception {
            Scanner scan = new Scanner(System.in);
            String input = scan.nextLine();
            String result = calc(input);
            System.out.println("Результат: " + result);
            scan.close();



    }


    //Для возможности расширения класса можно вынести операции в отдельные сущности
    private static String calc(String input) throws Exception {
        int result;
        String[] tokens = input.split(" ");
        CustomCalculatorException.Validate(tokens);
        String operand1 = tokens[0];
        String operator = tokens[1];
        String operand2 = tokens[2];

        boolean isArabic = operand1.matches(ARAB_REGEX)&& operand2.matches(ARAB_REGEX);
        int num1 = isArabic ? Integer.parseInt(operand1) : Convertor.romanToArabic(operand1);
        int num2 = isArabic ? Integer.parseInt(operand2) : Convertor.romanToArabic(operand2);

        switch (operator) {
            case "+" -> result = num1 + num2;
            case "-" -> result = num1 - num2;
            case "*" -> result = num1 * num2;
            case "/" -> result = num1 / num2;
            default -> throw new CustomCalculatorException("Недопустимый оператор");
        }
        return isArabic ? Integer.toString(result) : Convertor.arabicToRoman(result);
    }

    //Utility класс конвертор вынесен в отдельную логику удобен для расширения и использования
    private static class Convertor{
        private static int romanToArabic(String s) {
            int ans = 0, num = 0;
            for (int i = s.length()-1; i >= 0; i--) {
                switch (s.charAt(i)) {
                    case 'I' -> num = 1;
                    case 'V' -> num = 5;
                    case 'X' -> num = 10;
                    case 'L' -> num = 50;
                    case 'C' -> num = 100;
                    case 'D' -> num = 500;
                    case 'M' -> num = 1000;
                }
                if (4 * num < ans) ans -= num;
                else ans += num;
            }
            return ans;
        }
        private static String arabicToRoman(int num) {
            final int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
            final String[] symbols = {"M",  "CM", "D",  "CD", "C",  "XC", "L",
                    "XL", "X",  "IX", "V",  "IV", "I"};
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < values.length; ++i) {
                if (num == 0)
                    break;
                while (num >= values[i]) {
                    num -= values[i];
                    sb.append(symbols[i]);
                }
            }

            return sb.toString();
        }
    }
    //Обработка ошибок вынесена из логики
    private static class CustomCalculatorException extends Exception {
        private CustomCalculatorException(String message) {
            super(message);
        }
        private static void Validate(String[] tokens) throws Exception {
            if (tokens.length != 3) {
                throw new CustomCalculatorException(
                        (tokens.length == 1 ?
                                "Cтрока не является математической операцией" :
                                "Формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)"));
            }
            String operand1 = tokens[0];
            String operand2 = tokens[2];
            boolean isArabic = operand1.matches("[1-9]|10") && operand2.matches("[1-9]|10");
            boolean isRoman = operand1.matches("[IVXLCDM]+") && operand2.matches("[IVXLCDM]+");
            if (!(isArabic || isRoman) || (isArabic && isRoman)) {
                throw new CustomCalculatorException("Используются одновременно разные системы счисления");
            }
            if (Convertor.romanToArabic(operand1)-Convertor.romanToArabic(operand2)<=0 && operand1.matches(ROMAN_REGEX) && operand2.matches(ROMAN_REGEX)){
                throw new CustomCalculatorException("В римской системе нет отрицательных чисел");
            }
        }
    }
}

