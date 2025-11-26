package model.expressions;

import exceptions.ADTException;
import exceptions.ExpException;
import model.prg.adt.MyIDictionary;
import model.prg.adt.MyIHeap;
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
    public IValue eval(MyIDictionary<String, IValue> dict, MyIHeap<Integer, IValue> hp) throws ExpException, ADTException {
        if (!(e1.eval(dict, hp) instanceof IntValue))
            throw new ExpException("The first expression is not an integer! ");
        if (!(e2.eval(dict, hp) instanceof IntValue))
            throw new ExpException("The second expression is not an integer! ");
        if (!Objects.equals(op, '+') && !Objects.equals(op, '*') && !Objects.equals(op, '/') && !Objects.equals(op, '-'))
            throw new ExpException("Invalid operator! ");
        switch (op) {
            case '+' -> {
                return new IntValue(((IntValue) e1.eval(dict, hp)).getValue() + ((IntValue) e2.eval(dict, hp)).getValue());
            }
            case '-' -> {
                return new IntValue(((IntValue) e1.eval(dict, hp)).getValue() - ((IntValue) e2.eval(dict, hp)).getValue());
            }
            case '*' -> {
                return new IntValue(((IntValue) e1.eval(dict, hp)).getValue() * ((IntValue) e2.eval(dict, hp)).getValue());
            }
            case '/' -> {
                if (((IntValue) e2.eval(dict, hp)).getValue() == 0) throw new ExpException("Division by zero error !");
                return new IntValue(((IntValue) e1.eval(dict, hp)).getValue() / ((IntValue) e2.eval(dict, hp)).getValue());
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
