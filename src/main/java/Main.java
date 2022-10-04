import java.util.Scanner;
import java.util.Vector;
import java.util.EmptyStackException;
import java.util.regex.Pattern;

public class Main {
    private static final Pattern inputRegexes = Pattern.compile("[a-zA]");
    public static void main(String[] args){
		try {
        String flag = "y";
        Scanner s = new Scanner(System.in);
        do {
            
                System.out.println("Введите выражение:");
                String str = s.nextLine();
                if (str.isEmpty())
                    throw new Exception("Пустая строка");
                if (inputRegexes.matcher(str).find())
                    throw new Exception("Строка имеет неподходяшие символы");
                Vector <Operator> ops = new Vector<>();
                ops.add(new Operator("None", false, 2, 0, null)); 
                ops.add(new Operator("+", false, 2, 1, Operator::Plus));
                ops.add(new Operator("-", false, 2, 1, Operator::Minus));
                ops.add(new Operator("*", false, 2, 2, Operator::Multiply));
                ops.add(new Operator("/", false, 2, 2, Operator::Divide));
                System.out.println("Стартуем...");
                Calc calculator = new Calc(ops);
                Vector <Token> t;
                System.out.println("Токенизируем символы...");
                t = calculator.Tokenize(str);
                System.out.println("Предварительная подготовка (Коррекция токенов)...");
                calculator.CorrectTokens(t);
                System.out.println("Преобразование выражения в постфиксную запись...");
                t = calculator.Postfix(t);
                System.out.println("Вычисление выражения: " + str);
                System.out.println("Результат:" + calculator.PerformCalc(t));
            System.out.println("Хотите продолжить? (y/n): ");
            flag = s.nextLine();

        } while(flag.equals("y"));
		} catch (Exception e) {
                System.out.println("Ошибка: " + e);
            }
    }
}
