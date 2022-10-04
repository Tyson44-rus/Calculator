enum Ops {
    Op, Num, Open, Close
}

public class Token {
    String operation;
    int pos;
    Ops type;

    Token(String op, int pos, Ops type) {
        this.operation = op;
        this.pos = pos;
        this.type = type;
    };
}
