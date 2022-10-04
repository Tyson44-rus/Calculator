import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calc {
    final public Pattern pattern = Pattern.compile("-?\\d+([.,]\\d+)?");
    public Vector <Operator> ops;

    Calc(Vector <Operator> ops)  {
        this.ops = ops;
    };

    public Vector<Token> Tokenize(String expression) throws Exception {
        Vector <Token> tokens = new Vector<>();

        int i = 0;
        while (i < expression.length()) {
            char tmp = expression.charAt(i);
            if (tmp == ' ' || tmp == '\t') {
                i++;
            } else if (tmp == '(') {
                tokens.add(new Token("(", i, Ops.Open));
                i++;
            } else if (tmp == ')') {
                tokens.add(new Token(")", i, Ops.Close));
                i++;
            } else {
                boolean found = false;
                for (Operator op : ops)
                    if (expression.startsWith(op.Name, i)) {
                        found = true;
                        tokens.add(new Token(op.Name, i, Ops.Op));
                        i += op.Name.length();
                        break;
                    }
                if (!found) {
                    Matcher matcher = pattern.matcher(expression);
                    if (matcher.find(i) && matcher.start() == i) {
                        tokens.add(new Token(matcher.group(), i, Ops.Num));
                        i += matcher.group().length();
                    } else {
                        throw new Exception("Не удалось опознать символ. \n" + "Позиция символа - " + i);
                    }
                }
            }
        }
        return tokens;
    }

    public void CorrectTokens(Vector<Token> tokens) {
        Token p = tokens.get(0);
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).type == Ops.Op && tokens.get(i).operation.equals("-")) {
                if ((p.type != Ops.Num && p.type != Ops.Close)) {
                    int pos = tokens.indexOf(tokens.get(i));
                    tokens.remove(pos);
                    tokens.add(pos, new Token("-1", pos + 1, Ops.Num));
                    tokens.add(pos + 1, new Token("*", pos + 2, Ops.Op));
                }
            } else if (tokens.get(i).type == Ops.Op && tokens.get(i).operation.equals("+")) {
                if ((p.type != Ops.Num && p.type != Ops.Close)) {
                    int pos = tokens.indexOf(tokens.get(i));
                    tokens.remove(pos);
                    tokens.add(pos, new Token("1", pos + 1, Ops.Num));
                    tokens.add(pos + 1, new Token("*", pos + 2, Ops.Op));
                }
            }
            p = tokens.get(i);
        }
    }

    public Vector<Token> Postfix(Vector<Token> infix) throws Exception {
        Stack<Token> op = new Stack<>();
        Vector<Token> result = new Vector<>();

        for (Token token : infix) {
            if (token.type == Ops.Num) {
                result.add(token);
            } else if (token.type == Ops.Open) {
                op.push(token);
            } else if (token.type == Ops.Close) {
                while (op.peek().type != Ops.Open) {
                    result.add(op.pop());
                }
                op.pop();
            } else if (token.type == Ops.Op) {
                while (!op.isEmpty() && getOpFromToken(op.peek()).Priority >= getOpFromToken(token).Priority) {
                    result.add(op.pop());
                }
                op.push(token);
            }
        }
        while (!op.isEmpty()) {
            result.add(op.pop());
        }
        return result;
    }

    public double PerformCalc(Vector<Token> postfix) throws Exception {
        // Обратная польская запись, алгоритм
        Stack<Double> Arguments = new Stack<>();
        Double[] args = new Double[2];
        for (Token t : postfix)
            if (t.type == Ops.Num) {
                Double v = Double.parseDouble(t.operation);
                Arguments.push(v);
            } else {
                int nArg = 2;
                for (int i = nArg - 1; i >= 0; i--) {
                    args[i] = Arguments.pop();
                }
                Operator tmp = getOpFromToken(t);
                Function<Double[], Double> func = tmp.func;
                Double res = func.apply(args);
                Arguments.push(res);
            }
        if (Arguments.size() == 1)
            return Arguments.pop();
        else
            throw new Exception("Выражение имеет один аргумент");
    }

    public Operator getOpFromToken(Token tmp) throws Exception {
        // Пробегаемся по списку операторов и ищем подходящий
        if (tmp.type == Ops.Op) {
            for (Operator op : ops) {
                if (op.Name.equals(tmp.operation)) {
                    return op;
                }
            }
        } else
            return ops.get(0);
        throw new Exception("Не удалось найти в списке операторов");
    }
}
