import java.util.function.Function;

public class Operator {
    public String Name;
    public Boolean InExpression;
    public int NArgs;
    public int Priority;
    public Function<Double[], Double> func;

    Operator(String name, Boolean in, int N, int p, Function<Double[], Double> func) {
        this.Name = name;
        this.InExpression = in;
        this.NArgs = N;
        this.Priority = p;
        this.func = func;
    }

    public static double Plus(Double[] args){
        return args[0] + args[1];
    }

    public static double Minus(Double[] args){
        return args[0] - args[1];
    }

    public static double Multiply(Double[] args){
        return args[0] * args[1];
    }

    public static double Divide(Double[] args) throws ArithmeticException{
        if(args[1] != 0)
            return args[0] / args[1];
        else
            throw new ArithmeticException("Деление на ноль");
    }
};

