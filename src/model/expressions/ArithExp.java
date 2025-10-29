package model.expressions;

import exceptions.ExpException;
import model.prg.adt.MyIDictionary;
import model.values.IValue;
import model.values.IntValue;

import java.util.Objects;

public class ArithExp implements IExp {
    IExp e1;
    IExp e2;
    char op;

    public ArithExp(char gop, IExp ge1, IExp ge2) {
        e1 = ge1;
        e2 = ge2;
        op = gop;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> dict) throws ExpException {
        if (!(e1.eval(dict) instanceof IntValue)) throw new ExpException("The first expression is not an integer! ");
        if (!(e2.eval(dict) instanceof IntValue)) throw new ExpException("The second expression is not an integer! ");
        if (!Objects.equals(op, '+') && !Objects.equals(op, '*') && !Objects.equals(op, '/') && !Objects.equals(op, '-'))
            throw new ExpException("Invalid operator! ");
        switch (op) {
            case '+' -> {
                return new IntValue(((IntValue) e1.eval(dict)).getValue() + ((IntValue) e2.eval(dict)).getValue());
            }
            case '-' -> {
                return new IntValue(((IntValue) e1.eval(dict)).getValue() - ((IntValue) e2.eval(dict)).getValue());
            }
            case '*' -> {
                return new IntValue(((IntValue) e1.eval(dict)).getValue() * ((IntValue) e2.eval(dict)).getValue());
            }
            case '/' -> {
                if (((IntValue) e2.eval(dict)).getValue() == 0) throw new ExpException("Division by zero error !");
                return new IntValue(((IntValue) e1.eval(dict)).getValue() / ((IntValue) e2.eval(dict)).getValue());
            }
        }
        return null;
    }

    @Override
    public IExp deepCopy() {
        return new ArithExp(op, e1.deepCopy(), e2.deepCopy());
    }

    public String toString() {
        return e1.toString() + op + e2.toString();
    }
}
